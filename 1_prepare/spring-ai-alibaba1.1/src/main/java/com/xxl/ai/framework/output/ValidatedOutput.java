package com.xxl.ai.framework.output;

import lombok.Data;

/**
 * @Classname ValidatedOutput
 * @Description 结构化输出验证
 * @Date 2025/12/7 23:55
 * @Created by xxl
 */
@Data
public class ValidatedOutput {

    private String title;
    private Integer rating;

    public void validate() throws IllegalArgumentException {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException("评分必须在1-5之间");
        }
    }

}
