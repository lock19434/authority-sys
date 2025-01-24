package uestc.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import uestc.entity.Role;
import uestc.mapper.RoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import uestc.service.RoleService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liulong
 * @since 2025-01-25
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
