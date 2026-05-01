package org.atlas.flight.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.upstream")
public class GatewayUpstreamProperties {

	/**
	 * Auth service base URL (scheme, host, port only; request path is preserved).
	 */
	private String auth = "http://localhost:8082";

	/**
	 * Customer service base URL.
	 */
	private String customer = "http://localhost:8086";

	/**
	 * Core-data service base URL.
	 */
	private String coreData = "http://localhost:8081";

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCoreData() {
		return coreData;
	}

	public void setCoreData(String coreData) {
		this.coreData = coreData;
	}
}
