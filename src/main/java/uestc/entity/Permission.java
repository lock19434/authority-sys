package uestc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author liulong
 * @since 2025-01-25
 */
@Getter
@Setter
@ToString
@TableName("sys_permission")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @TableField("label")
    private String label;

    /**
     * 父权限ID
     */
    @TableField("p_id")
    private Long pId;

    /**
     * 父权限名称
     */
    @TableField("p_label")
    private String pLabel;

    /**
     * 权限标识符
     */
    @TableField("code")
    private String code;

    /**
     * 路由地址
     */
    @TableField("path")
    private String path;

    /**
     * 路由名称
     */
    @TableField("name")
    private String name;

    /**
     * 授权路径
     */
    @TableField("url")
    private String url;

    /**
     * 权限类型（0-目录，1-菜单，2-按钮）
     */
    @TableField("type")
    private Integer type;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     *  子菜单列表
     */
    @JsonInclude(JsonInclude.Include.NON_NULL) // 属性为null 不进行序列化操作
    @TableField(exist = false)
    private List<Permission> children = new ArrayList<>();

    @TableField(exist = false)
    private String value; // 用于前端来进行判断当前是目录，菜单，还是按钮。

    @TableField(exist = false)
    private Boolean open; // 是否展开
}
