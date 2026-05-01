package org.atlas.flight.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsWebMvcConfig implements WebMvcConfigurer {

	private final GatewayCorsProperties corsProperties;

	public CorsWebMvcConfig(GatewayCorsProperties corsProperties) {
		this.corsProperties = corsProperties;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		var patterns = corsProperties.getAllowedOriginPatterns();
		if (patterns == null || patterns.isEmpty()) {
			return;
		}
		registry.addMapping("/**")
				.allowedOriginPatterns(patterns.toArray(String[]::new))
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
	}
}
