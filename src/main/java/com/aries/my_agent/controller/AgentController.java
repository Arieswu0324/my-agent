package com.aries.my_agent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/react-agent")
@CrossOrigin(origins = "*")
public class AgentController {

    private final ChatClient agentChatClient;

    public record AgentTaskRequest(String task) {
    }

    public AgentController(@Lazy ChatClient agentChatClient) {
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
