// package com.chat.chatapp.service;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;

// @Service
// public class GeminiClient {

//     private final WebClient webClient;
//     private final String apiUrl;
//     private final String apiKey;
//     private final ObjectMapper objectMapper;

//     public GeminiClient(
//             @Value("${gemini.api.url}") String apiUrl,
//             @Value("${gemini.api.key}") String apiKey) {
//         this.apiUrl = apiUrl;
//         this.apiKey = apiKey;
//         this.webClient = WebClient.builder().build();
//         this.objectMapper = new ObjectMapper();
//     }

//     public Mono<String> generateText(String prompt) {
//         // Build request body as per Gemini API
//         String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

//         return webClient.post()
//                 .uri(apiUrl + "?key=" + apiKey)
//                 .header("Content-Type", "application/json")
//                 .bodyValue(requestBody)
//                 .retrieve()
//                 .bodyToMono(String.class)
//                 .map(this::extractTextFromResponse);
//     }

//     private String extractTextFromResponse(String response) {
//         try {
//             JsonNode root = objectMapper.readTree(response);
//             JsonNode candidates = root.path("candidates");
//             if (candidates.isArray() && candidates.size() > 0) {
//                 JsonNode textNode = candidates.get(0)
//                         .path("content")
//                         .path("parts")
//                         .get(0)
//                         .path("text");
//                 return textNode.asText();
//             }
//             return "No response from Gemini";
//         } catch (Exception e) {
//             return "Error parsing Gemini response: " + e.getMessage();
//         }
//     }
// }



package com.chat.chatapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeminiClient {

    private final WebClient webClient;
    private final String apiUrl;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public GeminiClient(
            @Value("${gemini.api.url}") String apiUrl,
            @Value("${gemini.api.key}") String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<String> generateText(String prompt) {
        // Build request body as per Gemini API
        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

        return webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                // 👇 transform raw Gemini JSON into just the text
                .map(this::extractTextFromResponse);
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode textNode = candidates.get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text");
                return textNode.asText();
            }
            return "No response from Gemini";
        } catch (Exception e) {
            return "Error parsing Gemini response: " + e.getMessage();
        }
    }
}
