package pl.lodz.chatservice.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class RagConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("classpath:/data/vectorstore.json")
    private Resource vectorStoreResource;

    @Value("vectorstore.json")
    private String vectorStoreName;

    @Value("classpath:/docs/pl-study-regulations.txt")
    private Resource faq;

    @Bean
    public VectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        var vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            log.info("Vector Store File Exists, loading...");
            simpleVectorStore.load(vectorStoreFile);
        }
//        else {
//            log.info("Vector Store File Does Not Exist, loading documents...");
//            String fullText;
//            try (InputStream inputStream = faq.getInputStream()) {
//                fullText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
//            }
//            List<String> sections = splitIntoParagraphs(fullText);
//            List<Document> documents = sections.stream()
//                    .map(Document::new)
//                    .toList();
//            simpleVectorStore.add(documents);
//            simpleVectorStore.save(vectorStoreFile);
//        }
        return simpleVectorStore;
    }

    private List<String> splitIntoParagraphs(String text) {
        List<String> sections = new ArrayList<>();

        Pattern pattern = Pattern.compile("(§\\s*\\d+)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);

        int lastIndex = 0;
        while (matcher.find()) {
            int start = matcher.start();
            if (start != 0) {
                sections.add(text.substring(lastIndex, start).trim());
            }
            lastIndex = start;
        }

        if (lastIndex < text.length()) {
            sections.add(text.substring(lastIndex).trim());
        }

        return sections;
    }

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    private File getVectorStoreFile() throws IOException {
        return vectorStoreResource.getFile();
    }

}