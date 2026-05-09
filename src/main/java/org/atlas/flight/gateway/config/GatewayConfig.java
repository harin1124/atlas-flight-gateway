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

import java.net.URI;
import java.util.function.BiFunction;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

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

	private ServerRequest logProxyRequest(ServerRequest request, String moduleName, String upstreamBase) {
		URI base = URI.create(upstreamBase);
		// Same host/port resolution as org.springframework.cloud.gateway.server.mvc.handler.ProxyExchangeHandlerFunction
		URI resolved = UriComponentsBuilder.fromUri(request.uri())
				.scheme(base.getScheme())
				.host(base.getHost())
				.port(base.getPort())
				.build(true)
				.toUri();
		log.info("==============================================");
		log.info("Module     : {}", moduleName);
		log.info("Client URI : [{}] {} (DevTools only shows this hop to the gateway)", request.method(), request.uri());
		log.info("Proxy →    : {} (server-side call; does not appear in the browser Network tab)", resolved);
		return request;
	}

	/**
	 * Proxied services often send their own CORS headers; the browser validates those and they
	 * conflict with {@link CorsWebMvcConfig}. Strip them so only the gateway sets CORS.
	 */
	private static BiFunction<ServerRequest, ServerResponse, ServerResponse> stripUpstreamCorsHeaders() {
		return (request, response) -> {
			var headers = response.headers();
			headers.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
			headers.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
			headers.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
			headers.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
			headers.remove(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS);
			headers.remove(HttpHeaders.ACCESS_CONTROL_MAX_AGE);
			return response;
		};
	}

	@Bean
	public RouterFunction<ServerResponse> authRoute() {
		return route("auth_route")
				.route(RequestPredicates.path("/auth/api/**"), http())
				.before(request -> logProxyRequest(request, "auth", upstream.getAuth()))
				.before(uri(upstream.getAuth()))
				.after(stripUpstreamCorsHeaders())
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> customerRoute() {
		return route("customer_route")
				.route(RequestPredicates.path("/customer/api/**"), http())
				.before(request -> logProxyRequest(request, "customer", upstream.getCustomer()))
				.before(uri(upstream.getCustomer()))
				.after(stripUpstreamCorsHeaders())
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> coreDataRoute() {
		return route("core_data_route")
				.route(RequestPredicates.path("/core-data/api/**"), http())
				.before(request -> logProxyRequest(request, "core-data", upstream.getCoreData()))
				.before(uri(upstream.getCoreData()))
				.after(stripUpstreamCorsHeaders())
				.build();
	}
}
