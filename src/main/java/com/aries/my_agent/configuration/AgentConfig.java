package com.aries.my_agent.configuration;

import com.aries.my_agent.tool.AgentTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class AgentConfig {

    //注入所有标记为 @Component 或 @Bean 的工具类实例
    private final List<AgentTool> tools;

    @Value("classpath:/prompt/react_system_prompt_template.txt") // 注入 ReAct 提示词文件
    private Resource reactSystemPromptResource;

    public AgentConfig(List<AgentTool> tools) {
        this.tools = tools;
    }

    @Bean
    public ChatClient agentChatClient(ChatModel chatModel) throws IOException {
        String systemPrompt = loadAndFormatSystemPrompt();

        return ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultTools(tools.toArray())
                .build();
    }

    private String loadAndFormatSystemPrompt() throws IOException {
        String promptTemplate = new String(
                reactSystemPromptResource.getContentAsByteArray(),
                StandardCharsets.UTF_8
        );

        String os = System.getProperty("os.name");
        String fileList = getFileList(".");

        return promptTemplate
                .replace("${operating_system}", os)
                .replace("${file_list}", fileList);
    }

    /**
     * 辅助方法：获取文件列表
     */
    private String getFileList(String directory) {
        try (var stream = Files.list(Path.of(directory))) {
            return stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "Error: Could not list files in directory " + directory;
        }
    }
}
