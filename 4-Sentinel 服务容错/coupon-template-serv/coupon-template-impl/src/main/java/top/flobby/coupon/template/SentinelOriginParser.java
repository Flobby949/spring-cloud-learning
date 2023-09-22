package top.flobby.coupon.template;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : Flobby
 * @program : 4-Sentinel 服务容错
 * @description : 来源解析
 * @create : 2023-09-22 14:04
 **/

@Component
@Slf4j
public class SentinelOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        log.info("request = {}, header = {}", httpServletRequest.getParameterMap(), httpServletRequest.getHeaderNames());
        log.info(httpServletRequest.getHeader("SentinelSource"));
        return httpServletRequest.getHeader("SentinelSource");
    }
}
