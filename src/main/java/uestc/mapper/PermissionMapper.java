package uestc.mapper;

import uestc.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * @author liulong
 * @since 2025-01-25
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    // 根据用户Id查询权限菜单列表
    List<Permission> findPermissionListByUserId(Long userId);

}

