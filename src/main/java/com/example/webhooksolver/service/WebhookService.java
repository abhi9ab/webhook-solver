package com.example.webhooksolver.service;

import com.example.webhooksolver.dto.SolutionRequest;
import com.example.webhooksolver.dto.WebhookRequest;
import com.example.webhooksolver.dto.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {
    
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private static final String WEBHOOK_GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private SqlSolutionService sqlSolutionService;
    
    public void executeWebhookFlow() {
        try {
            // Step 1: Generate webhook
            logger.info("Starting webhook flow...");
            WebhookResponse webhookResponse = generateWebhook();
            
            if (webhookResponse != null && webhookResponse.getWebhook() != null && webhookResponse.getAccessToken() != null) {
                logger.info("Successfully generated webhook: {}", webhookResponse.getWebhook());
                
                // Step 2: Get SQL solution and send it
                String regNo = "REG12347"; // This is from our request
                String sqlSolution = sqlSolutionService.getSqlSolution(regNo);
                int questionNumber = sqlSolutionService.getQuestionNumber(regNo);
                
                logger.info("Solving SQL Question: {}", questionNumber);
                logger.info("SQL Solution: {}", sqlSolution);
                
                // Step 3: Submit solution
                submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), sqlSolution);
                
            } else {
                logger.error("Failed to generate webhook - invalid response");
            }
            
        } catch (Exception e) {
            logger.error("Error in webhook flow: ", e);
        }
    }
    
    private WebhookResponse generateWebhook() {
        try {
            WebhookRequest request = new WebhookRequest("John Doe", "REG12347", "john@example.com");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
            
            logger.info("Sending POST request to generate webhook...");
            ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
                WEBHOOK_GENERATE_URL, entity, WebhookResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Webhook generated successfully");
                return response.getBody();
            } else {
                logger.error("Failed to generate webhook. Status: {}", response.getStatusCode());
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Exception while generating webhook: ", e);
            return null;
        }
    }
    
    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken.replace("Bearer ", ""));
            
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);
            
            logger.info("Submitting solution to webhook: {}", webhookUrl);
            ResponseEntity<String> response = restTemplate.postForEntity(
                webhookUrl, entity, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Solution submitted successfully! Response: {}", response.getBody());
            } else {
                logger.error("Failed to submit solution. Status: {}, Response: {}", 
                    response.getStatusCode(), response.getBody());
            }
            
        } catch (Exception e) {
            logger.error("Exception while submitting solution: ", e);
        }
    }
}