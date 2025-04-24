package pl.lodz.dormConnect.chat.ollama.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.dormConnect.chat.ollama.service.AIService;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@AllArgsConstructor
public class AIController {

    private AIService aiService;
    private ChatClient chatClient;
    @PostMapping("/response")
    public Map<String, String> getResponse(@RequestParam String message){

        VectorStore vectorStore = aiService.loadDataInVectorStore();

        return Map.of("response", chatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .user(message)
                .call()
                .content());

    }
}