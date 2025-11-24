package com.aries.my_agent.splitter;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MarkdownHeaderSplitter implements DocumentTransformer {

    @Override
    public List<Document> apply(List<Document> documents) {
        List<Document> splitDocuments = new ArrayList<>();

        for (Document doc : documents) {
            String content = doc.getFormattedContent();

            // 使用 Lookahead 正则进行切分，保留标题
            String[] sections = content.split("(?=(?m)^### \\d+\\.)");

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;

                // 可选：提取标题作为元数据 (Metadata)
                // 这样有助于检索时过滤，或者让 LLM 知道这段话的主题
                String title = extractTitle(section);

                // 创建新的 Document 对象，继承原文档的 metadata，并添加新 metadata
                Map<String, Object> newMetadata = new java.util.HashMap<>(doc.getMetadata());
                newMetadata.put("section_title", title);

                splitDocuments.add(new Document(section.trim(), newMetadata));
            }
        }
        return splitDocuments;
    }

    // 辅助方法：提取第一行作为标题
    private String extractTitle(String text) {
        int firstNewLine = text.indexOf('\n');
        if (firstNewLine == -1) return text; // 只有一行
        // 去掉 "### " 前缀
        return text.substring(0, firstNewLine).replace("### ", "").trim();
    }
}
