package com.aries.my_agent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class FileTool implements AgentTool{

    public record FilePath(String filePath) {
    }

    public record WriteRequest(String filePath, String content) {
    }

    @Tool(description = "从指定文件路径读取文件内容。输入必须是文件路径。")
    public String readFile(FilePath filePathQuery) {
        String pathStr = filePathQuery.filePath();
        Path path = Path.of(pathStr);

        System.out.println("-> 🔧 调用本地功能：读取文件内容，路径: " + pathStr);

        try {
            if (!Files.exists(path)) {
                return "ERROR: 文件未找到，路径不存在：" + pathStr;
            }
            return Files.readString(path);

        } catch (IOException e) {
            return "ERROR: 读取文件失败，原因：" + e.getMessage();
        }
    }

    @Tool(description = "将指定内容写入指定文件路径。如果文件不存在将创建，如果存在则覆盖其内容。")
    public String writeFile(WriteRequest writeRequest) {
        String pathStr = writeRequest.filePath();
        String content = writeRequest.content();
        Path path = Path.of(pathStr);

        System.out.println("-> 🔧 调用本地功能 (@Tool)：写入文件，路径: " + pathStr);

        try {
            // 写入文件：使用 CREATE 选项创建文件，TRUNCATE_EXISTING 选项覆盖现有内容
            Files.writeString(
                    path,
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return "SUCCESS: 内容已成功写入文件：" + pathStr;

        } catch (IOException e) {
            return "ERROR: 写入文件失败，原因：" + e.getMessage();
        }
    }

    public static void main(String[] args) {
        FileTool instance = new FileTool();
        System.out.println(instance.readFile(new FilePath("data/temp.txt")));

        System.out.println(instance.writeFile(new WriteRequest("data/temp.txt", "this is a new string")));
    }
}
