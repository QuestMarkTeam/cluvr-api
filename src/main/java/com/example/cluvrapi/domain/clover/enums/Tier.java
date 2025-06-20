package com.example.cluvrapi.domain.clover.enums;

import java.util.Arrays;
import java.util.Comparator;

import lombok.Getter;

@Getter
public enum Tier {
	SPROUT(0, 0, 0, 0), // 뉴비, 새싹 등급
	CLOVER1(1, 1000, 10, 10),
	CLOVER2(2, 2000, 20, 20),
	CLOVER3(3, 3000, 30, 30),
	RAINBOW_CLOVER1(4, 4000, 40, 40),
	RAINBOW_CLOVER2(5, 5000, 50, 50),
	RAINBOW_CLOVER3(6, 6000, 60, 60);

	private final Integer level;
	private final Integer requiredClover;
	private final Integer requiredWriteBoard;
	private final Integer requiredWriteReply;

	Tier(Integer level, Integer requiredClover, Integer requiredWriteBoard, Integer requiredWriteReply) {
		this.level = level;
		this.requiredClover = requiredClover;
		this.requiredWriteBoard = requiredWriteBoard;
		this.requiredWriteReply = requiredWriteReply;
	}

	public Integer checkAndUpgrade(Integer level, Integer clover, Integer boardCnt, Integer replyCnt) {
		return Arrays.stream(Tier.values())
			.sorted(Comparator.comparingInt(Tier::getLevel).reversed()) // 높은 등급부터
			.filter(tier -> tier.getLevel() >= this.level)
			.filter(tier -> tier.getRequiredClover() <= clover)
			.filter(tier -> tier.getRequiredWriteBoard() <= boardCnt)
			.filter(tier -> tier.getRequiredWriteReply() <= replyCnt)
			.findFirst()
			.orElse(SPROUT)
			.getLevel();
	}
}
