package uestc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private Long id;                // 编号
    private String name;            // 姓名
    private String avatar;          // 头像
    private String introduction;    // 介绍
    private Object[] roles;         // 角色权限集合

}
