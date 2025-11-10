package com.xxl.ai.app;

import com.xxl.ai.advisor.MyLoggerAdvisor;
import com.xxl.ai.chatmemory.RedisChatMemory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xxl
 * @date 2025/7/16 16:42
 */
@Component
@Slf4j
public class WordImportApp {

    private final ChatClient chatClient;

//    private static final String SYSTEM_PROMPT = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
//            "分析需求：\n" +
//            "{数据分析的需求或者目标}\n" +
//            "原始数据：\n" +
//            "{csv格式的原始数据，用,作为分隔符}\n" +
//            "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
//            "【【【【【\n" +
//            "{前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
//            "【【【【【\n" +
//            "{明确的数据分析结论、越详细越好，不要生成多余的注释}";

    private static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";

    private static final String CHAT_MEMORY_RETRIEVE_SIZE_KEY = "chat_memory_response_size";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 读取 resources 下的 txt 文件并返回字符串
     *
     * @return 文件内容字符串
     * @throws IOException 读取失败时抛出
     */
    @SneakyThrows
    public String queryPrompt() {
        // 1. 加载 resources 下的文件
        ClassPathResource resource = new ClassPathResource("prompt_word_Import.txt");

        // 2. 读取文件内容为字节数组，再转为字符串（指定 UTF-8 编码避免乱码）
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * LoveApp 的构造函数
     *
     * @param dashscopeChatModel
     */
    public WordImportApp(ChatModel dashscopeChatModel) {
        // 初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
        // ‍初始化基于文件的对话记忆
//        String fileDir = System.getProperty("user.dir") + "/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // ‍初始化基于Redis的对话记忆
        RedisChatMemory chatMemory = new RedisChatMemory(redisTemplate);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(queryPrompt())
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义日志 Advisor，可按需开启
                        new MyLoggerAdvisor()
                        // 自定义推理增强 Advisor，可按需开启
//                        new ReReadingAdvisor()
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 恋爱报告类
     *
     * @param title
     * @param suggestions
     */
    public record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * 结构化输出
     *
     * @param message
     * @param chatId
     * @return
     */
//    public WordImportApp.LoveReport doChatWithReport(String message, String chatId) {
//        WordImportApp.LoveReport loveReport = chatClient
//                .prompt()
//                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
//                .user(message)
//                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
//                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
//                .call()
//                .entity(WordImportApp.LoveReport.class);
//        log.info("loveReport: {}", loveReport);
//        return loveReport;
//    }
}
