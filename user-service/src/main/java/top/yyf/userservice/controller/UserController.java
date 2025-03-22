package top.yyf.userservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.yyf.userservice.config.UserProperties;
import top.yyf.userservice.entity.User;
import top.yyf.userservice.response.Result;
import top.yyf.userservice.response.UserResponse;
import top.yyf.userservice.service.UserService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@RestController
@RefreshScope

public class UserController {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private  UserProperties userProperties;

    @Autowired
    private UserService userService;

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


    @GetMapping("/user/{id}")
    public UserResponse<User> getUserById(@PathVariable Integer id) {
        if (userProperties.isServiceFlag()){
            System.out.println("服务被调用");
            User user = userService.getById(id);

            if (user != null){
                return new UserResponse<>(200, "成功", user);
            }else {
                return new UserResponse<>(404, "用户不存在", null);
            }
        }else {
            return new UserResponse<>(503, "用户服务正在维护中，请稍后...", null);
        }
    }
}