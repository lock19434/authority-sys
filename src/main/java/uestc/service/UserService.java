package uestc.service;

import uestc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author liulong
 * @since 2025-01-25
 */
public interface UserService extends IService<User> {

    // 根据用户名字查询用户信息
    User findUserByUserName(String userName);
}
