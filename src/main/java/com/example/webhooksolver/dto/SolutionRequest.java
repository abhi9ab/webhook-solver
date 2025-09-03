package com.example.webhooksolver.dto;

public class SolutionRequest {
    private String finalQuery;
    
    public SolutionRequest() {}
    
    public SolutionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }
    
    // Getters and Setters
    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}