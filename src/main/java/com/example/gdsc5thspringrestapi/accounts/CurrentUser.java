package com.example.gdsc5thspringrestapi.accounts;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //파라미터에 붙일 수 있음
@Retention(RetentionPolicy.RUNTIME) //언제까지 어노테이션 정보 유지할건지
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUser {
}
