package com.example.webhook_app.service;

import com.example.webhook_app.dto.WebhookRequest;
import com.example.webhook_app.dto.WebhookResponse;
import com.example.webhook_app.dto.SolutionRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate restTemplate;

    public WebhookService() {
        this.restTemplate = new RestTemplate();
    }

    public void executeWebhookFlow() {
        try {
            System.out.println("Starting webhook flow...");
            
            // Step 1: Generate webhook
            WebhookResponse webhookResponse = generateWebhook();
            
            if (webhookResponse != null) {
                System.out.println("Webhook generated successfully!");
                
                // Step 2: Get your SQL solution
                String sqlSolution = getSqlSolution();
                
                // Step 3: Submit solution
                submitSolution(webhookResponse.getWebhook(), 
                              webhookResponse.getAccessToken(), 
                              sqlSolution);
            } else {
                System.err.println("Failed to generate webhook!");
            }
        } catch (Exception e) {
            System.err.println("Error in webhook flow: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private WebhookResponse generateWebhook() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        
        WebhookRequest request = new WebhookRequest(
            "Shaurya",             
            "22BAI1173",        
            "shaurya.2022@vitstudent.ac.in"          
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<WebhookResponse> response = 
                restTemplate.postForEntity(url, entity, WebhookResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                WebhookResponse webhookResponse = response.getBody();
                System.out.println("Received webhook URL: " + webhookResponse.getWebhook());
                System.out.println("Received access token: " + webhookResponse.getAccessToken());
                return webhookResponse;
            }
        } catch (Exception e) {
            System.err.println("Failed to generate webhook: " + e.getMessage());
        }
        return null;
    }

    private String getSqlSolution() {
        
        String sqlQuery = "SELECT * FROM your_table_name"; // PLACEHOLDER - REPLACE THIS!
        
        System.out.println("SQL Solution: " + sqlQuery);
        return sqlQuery;
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken); // JWT token

        HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);

        try {
            ResponseEntity<String> response = 
                restTemplate.postForEntity(webhookUrl, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("✅ Solution submitted successfully!");
                System.out.println("Server response: " + response.getBody());
            } else {
                System.err.println("❌ Failed to submit solution. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
        }
    }
}
