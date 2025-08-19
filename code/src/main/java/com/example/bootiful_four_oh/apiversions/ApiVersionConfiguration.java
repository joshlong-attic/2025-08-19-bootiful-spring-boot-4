package com.example.bootiful_four_oh.apiversions;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

//  curl -H "X-API-version: 1.0" http://localhost:8080/hello

@Configuration
class ApiVersionConfiguration implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .setVersionRequired(false)
                .useRequestHeader("X-API-Version")
                .setDefaultVersion("2.0");
    }
}

@Controller
@ResponseBody
@RequestMapping(path = "/hello")
class GreetingsController {

    private final String message = "Hello, World!";

    @GetMapping(version = "1.0")
    String greetingsV1() {
        return this.message;
    }

    @GetMapping(version = "2+")
    Map<String, String> greetings() {
        return Map.of("greetings", this.message);
    }

}