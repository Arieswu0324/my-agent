package com.aries.my_agent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/react-agent")
@CrossOrigin(origins = "*")
public class ReActAgentController {

    private final ChatClient agentChatClient;

    public record AgentTaskRequest(String task) {
    }

    public ReActAgentController(ChatClient agentChatClient) {
        this.agentChatClient = agentChatClient;
    }

    /**
     * ReAct Agent 执行入口
     *
     */
    @PostMapping("/run")
    public String runAgent(@RequestBody AgentTaskRequest request) {

        // ChatClient 会自动处理多轮 Tool 调用，直到 Gemini 返回 <final_answer>
        // 传入用户提示词
        return agentChatClient.prompt()
                .user(request.task()) // 传入用户提示词
                .call()
                .content();
    }
}
