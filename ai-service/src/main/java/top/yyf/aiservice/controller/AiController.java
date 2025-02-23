package top.yyf.aiservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AiController {
    @Value("${ai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public AiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/ask")
    public String askQuestion(@RequestParam String question) {
        String apiUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey); // 添加API密钥

        // 构建请求体（根据通义千问API要求）
        String requestBody = String.format("""
            {
                "model": "qwen-max",
                "input": {
                    "messages": [
                        {"role": "user", "content": "%s"}
                    ]
                }
            }
            """, question.replace("\"", "\\\""));

        // 发送POST请求
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}