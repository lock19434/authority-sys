package uestc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>
 * 
 * </p>
 *
 * @author liulong
 * @since 2025-01-25
 */
@Getter
@Setter
@ToString
@TableName("sys_department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门名称
     */
    @TableField("name")
    private String name;

    /**
     * 部门电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 部门地址
     */
    @TableField("address")
    private String address;

    /**
     * 父级部门ID
     */
    @TableField("p_id")
    private Long pId;

    /**
     * 父级部门名称
     */
    @TableField("p_name")
    private String pName;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableField("is_delete")
    private Integer isDelete;
}
