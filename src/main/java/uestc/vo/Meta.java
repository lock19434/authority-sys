package uestc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Meta {
    private String title;
    private String icon;
    private Object[] roles;

}
