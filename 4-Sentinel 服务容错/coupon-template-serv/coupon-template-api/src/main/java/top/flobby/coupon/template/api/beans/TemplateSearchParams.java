package top.flobby.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 查询参数
 * @create : 2023-09-08 15:38
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateSearchParams {
    private Long id;
    private String name;
    private String type;
    private Long shopId;
    private Boolean available;
    private int page;
    private int pageSize;
}
