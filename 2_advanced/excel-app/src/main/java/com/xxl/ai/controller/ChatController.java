package com.xxl.ai.controller;

import com.xxl.ai.app.ExcelApp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname ChatController
 * @Description TODO
 * @Date 2025/4/4 23:38
 * @Created by xxl
 */
@RestController
public class ChatController {

    @Resource
    private ExcelApp excelApp;

    /**
     * 问答接口
     */
    @GetMapping("/chat")
    public void chat() {
    }


}
