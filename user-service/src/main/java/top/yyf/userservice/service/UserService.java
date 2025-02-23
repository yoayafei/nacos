package top.yyf.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    public String askAI(String question) {
        String url = "http://ai-service/api/ai/ask";
        return restTemplate.postForObject(url, question, String.class);
    }
}
