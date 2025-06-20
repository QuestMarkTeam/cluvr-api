package com.example.cluvrapi.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EarnGem {
	GemUserActivityType value();
}
