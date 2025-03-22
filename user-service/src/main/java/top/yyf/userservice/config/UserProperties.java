package top.yyf.userservice.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@ConfigurationProperties(prefix = "yyf")
@Component
public class UserProperties {
    private String username;
    private String job;
    private boolean serviceFlag;
}
