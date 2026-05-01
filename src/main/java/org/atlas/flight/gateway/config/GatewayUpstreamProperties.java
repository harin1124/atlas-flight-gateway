package org.atlas.flight.gateway.config;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "gateway.upstream")
public class GatewayUpstreamProperties {
	@NotBlank
	private String auth;

	@NotBlank
	private String customer;

	@NotBlank
	private String coreData;
}
