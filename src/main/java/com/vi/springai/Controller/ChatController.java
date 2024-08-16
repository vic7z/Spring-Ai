package com.vi.springai.Controller;

import com.vi.springai.Model.Response;
import com.vi.springai.Service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat/{query}")
    public Response chat(@PathVariable String query) {
        return chatService.generateChatMessage(query);
    }
    @GetMapping("/chat/rag/{query}")
    public String rag(@PathVariable String query) {
        return chatService.generateChatFromDocs(query);
    }
}
