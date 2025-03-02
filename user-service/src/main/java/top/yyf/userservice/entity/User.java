package top.yyf.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("t_user")
public class User {
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;
    private String mobile;
    private String password;
    private String userName;
    private String avatarUrl;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
