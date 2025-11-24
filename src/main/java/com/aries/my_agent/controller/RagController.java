package com.aries.my_agent.controller;


import com.aries.my_agent.service.VectorSearchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rag")
@CrossOrigin(origins = "*")
public class RagController {

    private final ChatClient ragChatClient;
    private final VectorSearchService vectorSearchService;


    @Value("classpath:/prompt/rag_prompt_template.txt")
    private Resource ragSystemPromptResource;

    public RagController(ChatClient ragChatClient, VectorSearchService vectorSearchService) {
        this.ragChatClient = ragChatClient;
        this.vectorSearchService = vectorSearchService;
    }

    public record RagQueryRequest(String question) {
    }

    @PostMapping("/ask")
    public String runRag(@RequestBody RagQueryRequest request) {
        List<Document> docs = vectorSearchService.search(request.question());
        String context = docs.stream()
                .map(doc -> "--- 来源: " + doc.getMetadata().get("source") + " ---\n" + doc.getFormattedContent())
                .collect(Collectors.joining("\n\n"));

        PromptTemplate promptTemplate = new PromptTemplate(ragSystemPromptResource);
        Message systemMessage = promptTemplate.createMessage(
                Map.of("documents", context, "input", request.question())
        );

        return ragChatClient.prompt()
                .messages(systemMessage)
                .call()
                .content();
    }
}
