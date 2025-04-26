package pl.lodz.dormConnect.chat.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DocsController {
    private final ChatClient chatClient;

    public DocsController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore,new SearchRequest()))
                .build();
    }

    @GetMapping("/chat")
    public String chatResponse(@RequestParam(value = "message", defaultValue = "Ile mogę mieć punktów ECTS braku") String message) {
        String modifiedMessage = message + ". Podaj też dokładny artykuł - zacytuj go, na podstawie którego odpowiadasz";
        String content = chatClient.prompt()
                .user(modifiedMessage)
                .call()
                .content();
        System.out.println(content);
        return content;
    }

}
