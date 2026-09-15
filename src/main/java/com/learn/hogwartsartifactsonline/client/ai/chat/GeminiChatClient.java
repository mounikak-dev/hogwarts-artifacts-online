package com.learn.hogwartsartifactsonline.client.ai.chat;

import com.learn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GeminiChatClient implements ChatClient {

    private final RestClient restClient;

//    public GeminiChatClient(@Value("${ai.gemini.endpoint}") String endpoint,
//                            @Value("${ai.gemini.apiKey}") String apiKey,
//                            RestClient.Builder restClientBuilder) {
//        this.restClient = restClientBuilder.baseUrl(endpoint)
//                .defaultHeader("x-goog-api-key", apiKey)
//                .build();
//    }

    public GeminiChatClient(
                            RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("endpoint")
                .defaultHeader("x-goog-api-key", "apiKey")
                .build();
    }

    @Override
    public String generate(List<ArtifactDto> artifacts) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Generate a comprehensive summary of the following artifacts in 100 words:\n\n");
        for (int i = 0; i < artifacts.size(); i++) {
            promptBuilder.append("Artifact ").append(i + 1).append(":\n")
                    .append(artifacts.get(i)).append("\n\n");
        }

        Map<String, Object> requestBody = Map.of(
                "model", "gemini-3.6-flash",
                "input", promptBuilder.toString()
        );
        GeminiResponse response = this.restClient.post()
                .uri("/v1beta/interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(GeminiResponse.class);

        return extractSummaryText(response);

    }

    private String extractSummaryText(GeminiResponse response) {
        if (response != null && response.getSteps() != null) {
            for (GeminiResponse.Step step : response.getSteps()) {
                // Look for step with type "model_output"
                if ("model_output".equals(step.getType()) && step.getContent() != null) {
                    for (GeminiResponse.Content content : step.getContent()) {
                        if (content.getText() != null) {
                            return content.getText();
                        }
                    }
                }
            }
        }
        return "No summary generated.";
    }
}
