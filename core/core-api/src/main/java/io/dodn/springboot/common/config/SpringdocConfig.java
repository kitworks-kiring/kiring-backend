package io.dodn.springboot.common.config;

import io.dodn.springboot.common.annotation.LoginUser;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;

@Configuration
public class SpringdocConfig implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        // 핸들러 메서드의 모든 파라미터를 순회
        for (java.lang.reflect.Parameter methodParameter : handlerMethod.getMethod().getParameters()) {
            // 해당 파라미터에 @LoginUser 어노테이션이 있는지 확인
            if (methodParameter.isAnnotationPresent(LoginUser.class)) {
                // @LoginUser 어노테이션이 있다면, 해당 파라미터 이름과 일치하는 OpenAPI Parameter 객체를 찾음
                if (operation.getParameters() == null) {
                    continue;
                }
                for (Parameter parameter : operation.getParameters()) {
                    // 파라미터 이름이 일치하면 해당 파라미터를 숨김 처리
                    if (Objects.equals(parameter.getName(), methodParameter.getName())) {
                        parameter.addExtension("x-hidden", true); // Swagger UI에서 파라미터 입력 필드를 숨김
                        // parameter.setRequired(false); // 필수는 아니라고 표시 (숨김 처리 시 보통 필요 없음)
                        break; // 해당 파라미터를 찾았으므로 내부 루프 종료
                    }
                }
            }
        }
        return operation;
    }
}
