package com.example.cluvrapi.domain.auth.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

	private List<String> adminDomains = new ArrayList<>();
	private String cognitoUserPoolId;

	public List<String> getAdminDomains() { return adminDomains; }
	public void setAdminDomains(List<String> domains) { this.adminDomains = domains; }
	public String getCognitoUserPoolId() {
		return cognitoUserPoolId;
	}
	public void setCognitoUserPoolId(String cognitoUserPoolId) {
		this.cognitoUserPoolId = cognitoUserPoolId;
	}
}
