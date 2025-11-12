package com.aries.my_agent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/react-agent")
public class AgentController {

    private final ChatClient agentChatClient;

    public AgentController(@Lazy ChatClient agentChatClient) {
        this.agentChatClient = agentChatClient;
    }

    /**
     * ReAct Agent 执行入口
     *
     */
    @GetMapping("/run")
    public String runAgent(@RequestParam String task) {

        // ChatClient 会自动处理多轮 Tool 调用，直到 Gemini 返回 <final_answer>
        String finalAnswer = agentChatClient.prompt()
                .user(task) // 传入用户提示词
                .call()
                .content();
        return finalAnswer;
    }
}
