package com.example.webhook_app;

import com.example.webhook_app.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebhookAppApplication implements CommandLineRunner {

    private final WebhookService webhookService;

    public WebhookAppApplication(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebhookAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Application started! Executing webhook flow...");
        webhookService.executeWebhookFlow();
    }
}
