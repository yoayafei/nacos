package top.yyf.userservice.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yyf.userservice.entity.User;
import top.yyf.userservice.mapper.UserMapper;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}