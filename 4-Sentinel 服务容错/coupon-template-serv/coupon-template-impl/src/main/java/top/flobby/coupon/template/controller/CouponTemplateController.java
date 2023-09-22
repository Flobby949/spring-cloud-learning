package top.flobby.coupon.template.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;
import top.flobby.coupon.template.api.beans.PagedCouponTemplateInfo;
import top.flobby.coupon.template.api.beans.TemplateSearchParams;
import top.flobby.coupon.template.service.CouponTemplateService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : CouponTemplateController
 * @create : 2023-09-08 16:42
 **/

@Slf4j
@RestController
@RequestMapping("/template")
public class CouponTemplateController {
    @Resource
    private CouponTemplateService couponTemplateService;

    /**
     * 新增优惠券
     * @param request {@link CouponTemplateInfo}
     * @return {@link CouponTemplateInfo}
     */
    @PostMapping("/addTemplate")
    public CouponTemplateInfo addTemplate(@Valid @RequestBody CouponTemplateInfo request) {
        log.info("Create coupon template: data={}", request);
        return couponTemplateService.createTemplate(request);
    }

    /**
     * 克隆优惠券
     *
     * @param templateId 模板id
     * @return {@link CouponTemplateInfo}
     */
    @PostMapping("/cloneTemplate")
    public CouponTemplateInfo cloneTemplate(@RequestParam("id") Long templateId) {
        log.info("Clone coupon template: data={}", templateId);
        return couponTemplateService.cloneTemplate(templateId);
    }

    /**
     * 读取优惠券
     *
     * @param id 模板id
     * @return {@link CouponTemplateInfo}
     */
    @GetMapping("/getTemplate")
    @SentinelResource(value = "getTemplate")
    public CouponTemplateInfo getTemplate(@RequestParam("id") Long id) {
        log.info("Load template, id={}", id);
        log.info("getTemplate 被调用！！！！！");
        return couponTemplateService.loadTemplateInfo(id);
    }

    /**
     * 批量获取
     *
     * @param ids 模板id
     * @return {@link Map<Long, CouponTemplateInfo>}
     */
    @GetMapping("/getBatch")
    @SentinelResource(value = "getTemplateInBatch", blockHandler = "getTemplateInBatchBlock", fallback = "getTemplateInBatchFallback")
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(@RequestParam("ids") Collection<Long> ids) {
        // 在限流，降级，熔断情况下，观察log日志输出情况
        log.info("getTemplateInBatch: {}", JSON.toJSONString(ids));
        log.info("getTemplateInBatch 被调用！！！！！");

        // 当Template批量查询ID数量等于3时，验证熔断器
        if (ids.size() == 3) {
            throw new RuntimeException();
        }

        // 增加响应时休眠
        return couponTemplateService.getTemplateInfoMap(ids);
    }

    /**
     * 接口降级方法
     *
     * @param ids ids
     * @param exception error
     * @return {@link Map}<{@link Long}, {@link CouponTemplateInfo}>
     */
    public Map<Long, CouponTemplateInfo> getTemplateInBatchBlock(Collection<Long> ids, BlockException exception) {
        log.info("********************************");
        log.info("接口被限流");
        log.info(ids.toString());
        log.info(exception.getMessage());
        log.info("********************************");
        Map<Long, CouponTemplateInfo> map = new HashMap<>();
        map.put(1L, CouponTemplateInfo.builder()
                .name("限流")
                .desc("限流")
                .build());
        return map;
    }

    public Map<Long, CouponTemplateInfo> getTemplateInBatchFallback(Collection<Long> ids) {
        log.info("********************************");
        log.info("接口被降级");
        log.info(ids.toString());
        log.info("********************************");
        Map<Long, CouponTemplateInfo> map = new HashMap<>();
        map.put(1L, CouponTemplateInfo.builder()
                .name("降级")
                .desc("降级")
                .build());
        return map;
    }

    /**
     * 搜索模板
     *
     * @param request 搜索条件
     * @return {@link PagedCouponTemplateInfo}
     */
    @PostMapping("/search")
    public PagedCouponTemplateInfo search(@Valid @RequestBody TemplateSearchParams request) {
        log.info("search templates, payload={}", request);
        return couponTemplateService.search(request);
    }

    /**
     * 删除优惠券
     *
     * @param id 模板id
     */
    @DeleteMapping("/deleteTemplate")
    public void deleteTemplate(@RequestParam("id") Long id) {
        log.info("Load template, id={}", id);
        couponTemplateService.deleteTemplate(id);
    }
}
