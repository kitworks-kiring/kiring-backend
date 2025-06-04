package io.dodn.springboot.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 이 어노테이션은 메서드의 파라미터에만 사용될 수 있음을 의미
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 어노테이션 정보 유지
public @interface LoginUser {
    // 필요하다면 어노테이션에 속성을 추가할 수 있지만, 단순히 마커로만 사용해도 충분합니다.
}
