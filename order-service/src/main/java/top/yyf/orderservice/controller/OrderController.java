package top.yyf.orderservice.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/order")
    public String createOrder(@RequestParam String username,@RequestParam String product){
        String userserviceUrl = "http://localhost:8081/user?username="+username;
        String productserviceUrl = "http://localhost:8083/product?product="+product;
        String userInfo = restTemplate.getForObject(userserviceUrl, String.class);
        String productInfo = restTemplate.getForObject(productserviceUrl, String.class);
        return "订单创建者是："+userInfo+"订单信息是:"+productInfo;
    }
}
