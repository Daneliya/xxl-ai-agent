package com.xxl.prometheus.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxl
 * @date 2025/12/29 09:13
 */
@RestController
public class MicrometerController {

    /**
     * 测试方法
     *
     * @return 返回结果
     */
    @GetMapping(value = "/test")
    public String test() {
        System.out.println(RandomUtil.randomString(10));
        return RandomUtil.randomString(10);
    }

}
