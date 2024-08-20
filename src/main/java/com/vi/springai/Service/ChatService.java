package com.vi.springai.Service;

import com.vi.springai.Model.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final VectorService vectorService;

    @Value("classpath:/prompts/rag-prompt.st")
    private Resource ragPrompt;
    @Value("classpath:/prompts/user-prompt.st")
    private Resource userPrompt;


    private final BeanOutputConverter<Response> responseOutputConverter = new BeanOutputConverter<>(Response.class);

    public ChatService(ChatClient.Builder builder, VectorStore vectorStore, VectorService vectorService) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
        this.vectorService = vectorService;
    }

    public Response generateChatMessage(String message) {
        String format = responseOutputConverter.getFormat();
        PromptTemplate promptTemplate=new PromptTemplate(userPrompt, Map.of("topic",message,"format",format));

        Prompt prompt =promptTemplate.create();
        String content = chatClient.prompt(prompt)
                .call()
                .content();
        System.out.println(content);
        return responseOutputConverter.convert(content);

    }

    public Response generateChatFromDocs(String message){
        vectorService.populateData();
        List<Document> documents = vectorStore.similaritySearch(SearchRequest
                .query(message)
                .withTopK(2));
        List<String> list = documents.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPrompt,
                Map.of("input",message,
                        "documents",String.join("\n",list),
                        "format",responseOutputConverter.getFormat()));
        Prompt prompt = promptTemplate.create();
        String content = chatClient.prompt(prompt)
                .call()
                .content();
        System.out.println(content);
        return responseOutputConverter.convert(
                content
        );
    }
}
