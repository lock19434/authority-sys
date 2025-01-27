package uestc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Override
    public User findUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        return baseMapper.selectOne(queryWrapper);
    }
}
