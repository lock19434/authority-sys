package uestc.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import uestc.entity.Permission;
import uestc.vo.Meta;
import uestc.vo.RouterVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *  菜单树工具类
 */
public class MenuTree {

    // 生成路由
    public static List<RouterVo> makeRouter(List<Permission> menuList, Long pid) {
        List<RouterVo> routerVoList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && Objects.equals(item.getPId(), pid))
                .forEach(item -> {
                    // 创建路由信息对象
                    RouterVo routerVo = new RouterVo();
                    routerVo.setName(item.getName());
                    routerVo.setPath(item.getPath());
                    // 判断当前菜单是否是一级菜单
                    if (item.getPId() == 0L) {
                        routerVo.setComponent("Layout");
                        routerVo.setAlwaysShow(true);
                    } else {
                        routerVo.setComponent(item.getUrl());
                        routerVo.setAlwaysShow(false);
                    }
                    // 设置Meta信息
                    Meta meta = new Meta(item.getLabel(), item.getIcon(), item.getCode().split(","));
                    routerVo.setMeta(meta);
                    // 递归生成路由
                    List<RouterVo> children = makeRouter(menuList, item.getId());
                    routerVoList.add(routerVo);
                });
        return routerVoList;
    }
    // 生成菜单树
    public static List<Permission> makeMenuTree(List<Permission> menuList, Long pid) {
        List<Permission> permissionList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item!=null && Objects.equals(item.getPId(), pid))
                .forEach(item -> {
                    // 创建权限菜单对象
                    Permission permission = new Permission();
                    // 将原有属性复制给菜单对象
                    BeanUtils.copyProperties(item, permission);
                    // 获取每一个item对象的字菜单，递归生成菜单树
                    List<Permission> children = makeMenuTree(menuList, item.getId());
                    // 设置字菜单
                    permission.setChildren(children);
                    // 添加到菜单集合
                    permissionList.add(permission);
                });
        return permissionList;
    }
}
