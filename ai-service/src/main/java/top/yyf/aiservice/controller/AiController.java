package top.yyf.aiservice.controller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
// 修改导入语句
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.alibaba.nacos.client.auth.ram.utils.SignUtil.sign;


@RestController
public class AiController {
    @Value("${ai.api-key}")
    private String apiKey;


    @Value("${aggregation.weather.api-url}")
    private String weatherApiUrl;

    @Value("${aggregation.weather.api-key}")
    private String weatherApiKey;

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

    @GetMapping("/weather/history")
    public String getHistoricalWeather(@RequestParam String cityId, @RequestParam String weatherDate) {
        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("key", weatherApiKey);
        params.put("city_id", cityId);
        params.put("weather_date", weatherDate);

        // 构建请求URL
        String requestUrl = weatherApiUrl + "?" + toQueryString(params);

        // 发送GET请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    // 将Map转换为查询字符串
    private String toQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> {
                    try {
                        return entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return entry.getKey() + "=" + entry.getValue();
                    }
                })
                .collect(Collectors.joining("&"));
    }

}