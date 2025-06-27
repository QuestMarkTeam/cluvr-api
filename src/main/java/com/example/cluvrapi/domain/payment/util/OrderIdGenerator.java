package com.example.cluvrapi.domain.payment.util;

import java.security.SecureRandom;

public class OrderIdGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
	private static final int LENGTH = 20;
	private static final SecureRandom random = new SecureRandom();

	public static String generate() {
		StringBuilder sb = new StringBuilder("ORD-"); // 접두사
		for (int i = 0; i < LENGTH; i++) {
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		return sb.toString();
	}
}
