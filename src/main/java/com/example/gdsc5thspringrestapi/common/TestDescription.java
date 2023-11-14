package com.example.gdsc5thspringrestapi.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE) //애노테이션 붙인 코드를 얼마나 길게 가져갈 것이냐
public @interface TestDescription {
    String value();

}
