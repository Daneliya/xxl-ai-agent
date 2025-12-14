package com.xxl.ai.framework.example;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.util.function.Function;

/**
 * @Author xxl
 * @Date 2025/12/2 10:14
 */
public class Example03_Tools {

    public static void main(String[] args) {

    }

    /**
     * 编程方式规范 - FunctionToolCallback 构建示例
     */
    public static void programmaticToolSpecification() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather", new WeatherService())
                .description("Get the weather in location")
                .inputType(WeatherRequest.class)
                .build();
    }

    public enum Unit {C, F}

    // ==================== 访问上下文 ====================

    public enum UnitType {CELSIUS, FAHRENHEIT}

    /**
     * 天气服务
     */
    public static class WeatherService implements Function<WeatherRequest, WeatherResponse> {
        @Override
        public WeatherResponse apply(WeatherRequest request) {
            return new WeatherResponse(30.0, Unit.C);
        }
    }

    // ==================== Context（上下文） ====================

    public record WeatherRequest(
            @ToolParam(description = "城市或坐标") String location,
            Unit unit
    ) {
    }

    public record WeatherResponse(double temp, Unit unit) {
    }
}
