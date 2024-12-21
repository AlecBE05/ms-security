package com.codigo.api_gateway.filter;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.InstanceFilter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class GlobalFilterConfiguration {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private EurekaClient eurekaClient;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static final List<String> EXCLUDE_PATHS = List.of("app/authentication/v1/**");

    public class AuthenticationFilter implements GlobalFilter, Ordered{

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            String path = exchange.getRequest().getURI().getPath();

            if(isExcludePath(path)){
                return chain.filter(exchange);
            }
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token==null || token.isEmpty()){
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            String serviceUrl = getServiceUrl("ms-security","app/authentication/v1/validateToken");
            return webClientBuilder.build()
                    .post()
                    .uri(serviceUrl)
                    .header("validate",token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(isValid->{
                        if(Boolean.TRUE.equals(isValid)){
                            return chain.filter(exchange);
                        }else{
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    })
                    .onErrorResume(error->{
                        exchange.getResponse().setStatusCode(HttpStatus.CONFLICT);
                        return exchange.getResponse().setComplete();
                    });
        }

        @Override
        public int getOrder() {
            return 0;
        }

        private boolean isExcludePath(String path) {
            return EXCLUDE_PATHS.stream().anyMatch(pattern -> antPathMatcher.match(pattern,path));
        }

        private String getServiceUrl(String serviceName, String endPoint) {

            InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(serviceName,false);
            return instanceInfo.getHomePageUrl() + endPoint;
        }
    }
}
