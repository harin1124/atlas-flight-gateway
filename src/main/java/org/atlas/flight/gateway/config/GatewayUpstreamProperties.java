package org.atlas.flight.gateway.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "gateway.upstream")
public class GatewayUpstreamProperties {

	/**
	 * Auth service base URL (scheme, host, port only; request path is preserved).
	 */
	@NotBlank
	private String auth;

	/**
	 * Customer service base URL.
	 */
	@NotBlank
	private String customer;

	/**
	 * Core-data service base URL.
	 */
	@NotBlank
	private String coreData;

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
