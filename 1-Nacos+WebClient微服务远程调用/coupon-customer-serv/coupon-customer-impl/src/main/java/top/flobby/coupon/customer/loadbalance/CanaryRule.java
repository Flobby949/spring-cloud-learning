package top.flobby.coupon.customer.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static top.flobby.coupon.customer.constant.Constant.TRAFFIC_VERSION;

/**
 * @author : Flobby
 * @program : 1-Nacos+WebClient微服务远程调用
 * @description : 金丝雀测试规则
 * @create : 2023-09-19 12:43
 **/

@Slf4j
public class CanaryRule implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;
    private final String serviceId;

    // 定义一个轮询策略的种子
    final AtomicInteger position;

    public CanaryRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers, String serviceId) {
        this.serviceInstanceListSuppliers = serviceInstanceListSuppliers;
        this.serviceId = serviceId;
        this.position = new AtomicInteger(new Random().nextInt(1000));
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSuppliers
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        log.info("进入金丝雀");
        return supplier.get(request)
                .next()
                .map(serviceInstances ->
                        processInstanceResponse(supplier, serviceInstances, request));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances,
                                                              Request request) {
        Response<ServiceInstance> serviceInstanceResponse = getInstancesResponse(serviceInstances, request);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstancesResponse(List<ServiceInstance> instances, Request request) {
        // 注册中心无可用实例，抛出异常
        if (CollectionUtils.isEmpty(instances)) {
            log.warn("没有实例存活，{}", serviceId);
            return new EmptyResponse();
        }

        // 从请求Header中获取特定的流量打标
        // 注意：以下代码仅适用于 WebClient 调用，如果使用 RestTemplate 或者 Feign 则需要额外适配
        DefaultRequestContext context = (DefaultRequestContext) request.getContext();
        RequestData requestData = (RequestData) context.getClientRequest();
        HttpHeaders headers = requestData.getHeaders();
        String trafficVersion = headers.getFirst(TRAFFIC_VERSION);
        log.info("金丝雀打标值： {}", trafficVersion);
        // 如果没有找到打标标记，或标记为空，则使用RoundRobin规则进行轮询
        if (StringUtils.isBlank(trafficVersion)) {
            // 过滤所有金丝雀测试结点，（metaData有值的结点）
            List<ServiceInstance> noneCanaryInstances = instances.stream()
                    .filter(e ->
                            !e.getMetadata().containsKey(TRAFFIC_VERSION))
                    .toList();
            return getRoundRobinInstance(noneCanaryInstances);
        }

        // 找出所有的金丝雀服务器,用RoundRobin算法挑一台
        List<ServiceInstance> canaryInstances = instances.stream().filter(e -> {
            String trafficVersionInMetadata = e.getMetadata().get(TRAFFIC_VERSION);
            return StringUtils.equalsAnyIgnoreCase(trafficVersionInMetadata, trafficVersion);
        }).toList();
        return getRoundRobinInstance(canaryInstances);
    }

    /**
     * 使用轮询机制获取结点
     * @param instances 实例列表
     * @return 服务实例
     */
    private Response<ServiceInstance> getRoundRobinInstance(List<ServiceInstance> instances) {
        // 如果没有可用结点，则返回空
        if (instances.isEmpty()) {
            log.warn("没有可用服务 {}", serviceId);
            return new EmptyResponse();
        }
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }
}
