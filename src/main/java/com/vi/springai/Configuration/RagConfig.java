package com.vi.springai.Configuration;

import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {


    @Bean
    public SimpleVectorStore vectorStore(OllamaEmbeddingModel ollamaEmbeddingModel) {
        return new SimpleVectorStore(ollamaEmbeddingModel);
    }
}
