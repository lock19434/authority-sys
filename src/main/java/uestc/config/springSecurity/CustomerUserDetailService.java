package uestc.config.springSecurity;

import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uestc.entity.Permission;
import uestc.entity.User;
import uestc.service.PermissionService;
import uestc.service.UserService;

import java.util.List;
import java.util.Objects;

/**
 *  用户认证处理器类
 */
@Component
public class CustomerUserDetailService implements UserDetailsService {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误！");
        }
        // 查询该用户的权限列表
        List<Permission> permissionListByUserId = permissionService.findPermissionListByUserId(user.getId());
        user.setPermissionList(permissionListByUserId);

        // 获取权限列表
        List<String> codeList = permissionListByUserId.stream()
                .map(Permission::getCode) // 提取权限编码
                .filter(Objects::nonNull) // 过滤掉空编码
                .toList();
        String[] codeListArray = codeList.toArray(new String[0]);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(codeListArray);
        user.setAuthorities(authorityList);

        return user;
    }
}
