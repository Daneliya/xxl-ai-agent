package com.xxl.ai.controller;

import com.xxl.ai.app.WordImportApp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Classname ChatController
 * @Description TODO
 * @Date 2025/4/4 23:38
 * @Created by xxl
 */
@RestController
public class ChatController {

    @Resource
    private WordImportApp wordImportApp;

    /**
     * 问答接口
     */
    @PostMapping("/import")
    public void chat(MultipartFile file) {
    }


}
