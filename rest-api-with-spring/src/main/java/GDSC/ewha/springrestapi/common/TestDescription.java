package GDSC.ewha.springrestapi.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE) // 어노테이션을 붙인 코드를 얼마나 오래 가져갈지
public @interface TestDescription {
    String value();
}
