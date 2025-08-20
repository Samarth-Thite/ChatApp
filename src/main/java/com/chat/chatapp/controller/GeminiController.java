// package com.chat.chatapp.controller;

// import com.chat.chatapp.service.GeminiClient;
// import org.springframework.web.bind.annotation.*;

// import java.util.Map;

// @RestController
// @RequestMapping("/api/gemini")
// @CrossOrigin(origins = "http://localhost:4200")
// public class GeminiController {

//     private final GeminiClient geminiClient;

//     public GeminiController(GeminiClient geminiClient) {
//         this.geminiClient = geminiClient;
//     }

//     @PostMapping("/generate")
//     public String generate(@RequestBody Map<String, String> request) {
//         String prompt = request.get("prompt");
//         return geminiClient.generateText(prompt).block();
//     }
// }



package com.chat.chatapp.controller;

import com.chat.chatapp.service.GeminiClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "http://localhost:4200")
public class GeminiController {

    private final GeminiClient geminiClient;

    public GeminiController(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    @PostMapping("/generate")
    public Map<String, String> generate(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String response = geminiClient.generateText(prompt).block();
        return Map.of("response", response);
    }
}
