package com.xxl.ai.testApp;

import com.xxl.ai.app.LoveApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @Author: xxl
 * @Date: 2025/7/11 15:09
 */
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员皮卡丘";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让另一半（杰尼龟）更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员皮卡丘，我想让另一半（杰尼龟）更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
//        {
//            "title": "皮卡丘的恋爱报告",
//                "suggestions": [
//                    "加强情感沟通：每天抽出专属时间与杰尼龟交流，倾听他的需求并分享你的感受，避免只聚焦编程话题。",
//                    "创造共享体验：计划共同活动如户外冒险或游戏日，融入他的兴趣（如水系技能训练），提升亲密感。",
//                    "表达欣赏与惊喜：定期用言语或小礼物（如定制能量方块）肯定他的付出，强化情感连接。"
//            ]
//        }
    }

}
