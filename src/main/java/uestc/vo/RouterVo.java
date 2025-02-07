package uestc.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    private String path;        // 路由地址
    private String component;   // 路由对应的组件
    private boolean alwaysShow; // 是否显示
    private String name;        // 路由名称
    private Meta meta;          // 路由meta信息

    private List<RouterVo> children = new ArrayList<>(); // 子路由
}
