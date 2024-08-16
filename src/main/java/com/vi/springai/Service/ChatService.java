package com.vi.springai.Service;

import com.vi.springai.Model.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.parser.OutputParser;
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

    @Value("classpath:/prompts/rag-prompt.st")
    private Resource ragPrompt;

    private final BeanOutputConverter<Response> responseOutputConverter = new BeanOutputConverter<>(Response.class);


    public ChatService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public Response generateChatMessage(String message) {
        String promtMessage = """
                 writ a short note about {topic} and also include few books to refer about the topic
                 {format}
                """;
        String format = responseOutputConverter.getFormat();
        PromptTemplate promptTemplate=new PromptTemplate(promtMessage, Map.of("topic",message,"format",format));

        Prompt prompt =promptTemplate.create();
        String content = chatClient.prompt(prompt)
                .call()
                .content();
        System.out.println(content);
        return responseOutputConverter.convert(content);

    }

    public String generateChatFromDocs(String message){
        List<Document> documents = vectorStore.similaritySearch(SearchRequest
                .query(message)
                .withTopK(2));
        List<String> list = documents.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPrompt,
                Map.of("input",message,"documents",String.join("\n",list)));
        Prompt prompt = promptTemplate.create();
        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
