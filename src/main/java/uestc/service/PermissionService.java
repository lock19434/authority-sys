package uestc.service;

import uestc.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liulong
 * @since 2025-01-25
 */
public interface PermissionService extends IService<Permission> {
    List<Permission> findPermissionListByUserId(Long userId);
}
