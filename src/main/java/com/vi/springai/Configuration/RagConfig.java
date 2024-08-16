package com.vi.springai.Configuration;

import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

@Configuration
public class RagConfig {
    @Value("classpath:/story.txt")
    private Resource story;

    @Bean
    public SimpleVectorStore vectorStore(OllamaEmbeddingModel ollamaEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(ollamaEmbeddingModel);
        TextReader textReader = new TextReader(story);
        textReader.getCustomMetadata()
                .put("filename", story.getFilename());
        List<Document> documents = textReader.get();
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> split = tokenTextSplitter.split(documents);
        simpleVectorStore.add(split);
        simpleVectorStore.save(new File("vectors.json"));
        return simpleVectorStore;
    }
}
