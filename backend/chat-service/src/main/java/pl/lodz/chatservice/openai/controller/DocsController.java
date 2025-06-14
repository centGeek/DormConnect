package pl.lodz.chatservice.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class DocsController {
    private final ChatClient chatClient;

    public DocsController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore,new SearchRequest()))
                .build();
    }

    @GetMapping("/get-message")
    public Map<String, String> chatResponse(@RequestParam(value = "message", defaultValue = "Czym jest urlop krótkoterminowy?") String message) {
        String modifiedMessage = message + ". Podaj też dokładny artykuł - zacytuj go, na podstawie którego odpowiadasz";
        String content = chatClient.prompt()
                .user(modifiedMessage)
                .call()
                .content();
        System.out.println(content);
        return Map.of("reply", content);
    }

}
