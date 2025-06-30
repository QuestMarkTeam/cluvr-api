package com.example.cluvrapi.global.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CognitoClientConfig {

	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Bean(name = "cognitoUserClient")
	public CognitoIdentityProviderClient cognitoUserClient() {
		return CognitoIdentityProviderClient.builder()
			.region(Region.of(region))
			.build(); // 기본 Profile, 또는 secretHash 기반 요청에 맞게 구성
	}

	// 관리자 요청용 (AdminConfirmSignUp 등: IAM 전용)
	@Bean(name = "cognitoAdminClient")
	public CognitoIdentityProviderClient cognitoAdminClient() {
		return CognitoIdentityProviderClient.builder()
			.region(Region.of(region))
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(accessKey, secretKey)
				)
			)
			.build();
	}
}
