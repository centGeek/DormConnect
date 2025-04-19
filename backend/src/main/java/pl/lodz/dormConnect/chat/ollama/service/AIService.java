package pl.lodz.dormConnect.chat.ollama.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AIService {
    public static final String fileName = "pl-study-regulations.txt";

    @Value("classpath:/docs/"+fileName)
    private Resource fileResource;

    final VectorStore vectorStore;

    @Autowired
    public AIService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    //conversion from .txt do vector store as for now.
    public VectorStore loadDataInVectorStore(){
        TextReader textReader = new TextReader(fileResource);

        textReader.getCustomMetadata().put("filename", fileName);
        var documentList = textReader.get();

        log.info("Total documents counts are : {}", documentList.size());
        List<Document> documents = new TokenTextSplitter().apply(documentList);

        log.info("Total documents counts are : {}", documents.size());

        vectorStore.add(documents);
        return vectorStore;
    }

}
