package uestc.service.impl;

import uestc.entity.User;
import uestc.mapper.UserMapper;
import uestc.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liulong
 * @since 2025-01-25
 */
@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

}
