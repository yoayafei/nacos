package top.yyf.userservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.yyf.userservice.response.Result;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class UserController {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/user")
    public Result getUserInfo(
            @RequestParam String username,
            @RequestParam String question
    ) {
        try {
            // 编码参数
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
            String encodedQuestion = URLEncoder.encode(question, StandardCharsets.UTF_8);

            // 调用AI服务
            String aiResponse = restTemplate.getForObject(
                    "http://ai-service/ask?username=" + encodedUsername + "&question=" + encodedQuestion,
                    String.class
            );

            // 解析AI响应
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            String answer = rootNode.path("output").path("text").asText();

            // 返回结构化数据
            return new Result(username, answer);

        } catch (Exception e) {
            return new Result("error", "服务调用失败: " + e.getMessage());
        }
    }




//    private final RestTemplate restTemplate;
//
//    public UserController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    @GetMapping("/user")
//    public Result getUserInfo(
//            @RequestParam String username,
//            @RequestParam String question
//    ) {
//        try {
//            // 调用Node.js服务（修改服务名称）
//            String nodeServiceUrl = "http://node-service/ai/ask?username=" + username + "&question=" + question;
//            String response = restTemplate.getForObject(nodeServiceUrl, String.class);
//
//            // 解析响应
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(response);
//            String answer = rootNode.path("answer").asText();
//
//            return new Result(username, answer);
//        } catch (Exception e) {
//            return new Result("error", "Failed to call Node.js service: " + e.getMessage());
//        }
//    }
}