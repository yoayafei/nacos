package top.yyf.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.yyf.userservice.entity.User;
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
