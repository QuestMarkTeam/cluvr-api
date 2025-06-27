package com.example.cluvrapi.domain.payment.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import lombok.RequiredArgsConstructor;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.payment.dto.request.PaymentPrepareRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.PaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.service.PaymentService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@Controller
@RequiredArgsConstructor
public class PaymentViewController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PaymentService paymentService;

	@RequestMapping(value = "/api/confirm")
	public ResponseEntity<JSONObject> confirmPayment1(@RequestBody String jsonBody) throws Exception {

		JSONParser parser = new JSONParser();
		String orderId;
		String amount;
		String paymentKey;
		try {
			// 클라이언트에서 받은 JSON 요청 바디입니다.
			JSONObject requestData = (JSONObject)parser.parse(jsonBody);
			paymentKey = (String)requestData.get("paymentKey");
			orderId = (String)requestData.get("orderId");
			amount = (String)requestData.get("amount");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		;
		JSONObject obj = new JSONObject();
		obj.put("orderId", orderId);
		obj.put("amount", amount);
		obj.put("paymentKey", paymentKey);

		// 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
		// 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
		String widgetSecretKey = "test_sk_Gv6LjeKD8ajEAPQ2yxgw3wYxAdXy";
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		// 결제를 승인하면 결제수단에서 금액이 차감돼요.
		URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Authorization", authorizations);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(obj.toString().getBytes("UTF-8"));

		int code = connection.getResponseCode();
		boolean isSuccess = code == 200;

		InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

		// 결제 성공 및 실패 비즈니스 로직을 구현하세요.
		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
		JSONObject jsonObject = (JSONObject)parser.parse(reader);
		responseStream.close();

		return ResponseEntity.status(code).body(jsonObject);
	}

	/**
	 * 설명: 결제 UI 호출
	 *
	 * @return {반환값에 대한 설명}
	 *
	 * @author {작성자 이름}
	 */
	@PostMapping("/api/checkout")
	public ResponseEntity<BaseResponse<PaymentPrepareResponseDto>> redirectToCheck(
		@RequestBody PaymentPrepareRequestDto requestDto,
		@Auth AuthUser authUser
	) {
		PaymentPrepareResponseDto responseDto = paymentService.savePaymentInfo(authUser.id(), requestDto);
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.OK));
	}
	@GetMapping("/api/fail")
	public String redirectToFail() {
		return "redirect:/payment/fail.html";
	}
	@GetMapping("/api/success")
	public String redirectToSuccess() {
		return "redirect:/payment/success.html";
	}
}
