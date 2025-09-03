package com.example.webhooksolver.listener;

import com.example.webhooksolver.service.WebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);
    
    @Autowired
    private WebhookService webhookService;
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application started successfully. Executing webhook flow...");
        webhookService.executeWebhookFlow();
    }
}