package top.yyf.userservice.controller;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yyf.userservice.config.YyfProperties;

@RestController
//@RefreshScope
public class TestController {
//    @Value("${yyf.username}")
//    private String username;
//
//    @Value("${yyf.job}")
//    private String job;

    @Resource
    private YyfProperties yyfProperties;

    @GetMapping("/test")
    public String get() {
        return "读取到配置中心的值：" + yyfProperties.getUsername() + "," + yyfProperties.getJob();
    }
}
