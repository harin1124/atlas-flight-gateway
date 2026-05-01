package org.atlas.flight.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.cors")
public class GatewayCorsProperties {

	/**
	 * Browser origins allowed to call this gateway (patterns supported, e.g. {@code http://localhost:*}).
	 */
	private List<String> allowedOriginPatterns = new ArrayList<>(List.of(
			"http://localhost:*",
			"http://127.0.0.1:*"));

	public List<String> getAllowedOriginPatterns() {
		return allowedOriginPatterns;
	}

	public void setAllowedOriginPatterns(List<String> allowedOriginPatterns) {
		this.allowedOriginPatterns = allowedOriginPatterns;
	}
}
