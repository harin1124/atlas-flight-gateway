package org.atlas.flight.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
@EnableConfigurationProperties({ GatewayUpstreamProperties.class, GatewayCorsProperties.class })
public class GatewayConfig {

	private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

	private final GatewayUpstreamProperties upstream;

	public GatewayConfig(GatewayUpstreamProperties upstream) {
		this.upstream = upstream;
	}

	private static ServerRequest logProxyRequest(ServerRequest request, String moduleName) {
		log.info("==============================================");
		log.info("Module     : {}", moduleName);
		log.info("URI        : [{}] {}", request.method(), request.uri());
		return request;
	}

	@Bean
	public RouterFunction<ServerResponse> authRoute() {
		return route("auth_route")
				.route(RequestPredicates.path("/auth/api/**"), http())
				.before(request -> logProxyRequest(request, "auth"))
				.before(uri(upstream.getAuth()))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> customerRoute() {
		return route("customer_route")
				.route(RequestPredicates.path("/customer/api/**"), http())
				.before(request -> logProxyRequest(request, "customer"))
				.before(uri(upstream.getCustomer()))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> coreDataRoute() {
		return route("core_data_route")
				.route(RequestPredicates.path("/core-data/api/**"), http())
				.before(request -> logProxyRequest(request, "core-data"))
				.before(uri(upstream.getCoreData()))
				.build();
	}
}
