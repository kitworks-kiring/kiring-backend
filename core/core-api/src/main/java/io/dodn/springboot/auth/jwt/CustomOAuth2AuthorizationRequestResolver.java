package io.dodn.springboot.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2AuthorizationRequestResolver.class);
    public static final String CLIENT_REGISTRATION_ID_ATTRIBUTE_NAME = "client_registration_id";

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        log.info(">>> Custom resolver (1) is called!");

        OAuth2AuthorizationRequest authorizationRequest = this.defaultResolver.resolve(request);
        String registrationId = extractRegistrationIdFromRequest(request);
        return addKakaoPrompt(authorizationRequest, registrationId);
    }

    private String extractRegistrationIdFromRequest(final HttpServletRequest request) {
        String uri = request.getRequestURI(); // 예: /oauth2/authorization/kakao
        String prefix = "/oauth2/authorization/";
        if (uri.startsWith(prefix)) {
            return uri.substring(prefix.length());
        }
        return null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        log.info(">>> Custom resolver (2) for client '{}' is called!", clientRegistrationId);

        OAuth2AuthorizationRequest authorizationRequest = this.defaultResolver.resolve(request, clientRegistrationId);
        return addKakaoPrompt(authorizationRequest, clientRegistrationId);
    }

    private OAuth2AuthorizationRequest addKakaoPrompt(OAuth2AuthorizationRequest authorizationRequest, String clientRegistrationId) {
        if (authorizationRequest == null) {
            return null;
        }

        log.info(">>> Checking registrationId (from method param): [{}]", clientRegistrationId);

        if ("kakao".equalsIgnoreCase(clientRegistrationId)) {
            log.info(">>> Matched 'kakao'! Adding 'prompt=login' parameter.");

            Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());
            additionalParameters.put("prompt", "login");

            // 기존 attributes 복사 + registrationId 강제 세팅
            Map<String, Object> attributes = new HashMap<>(authorizationRequest.getAttributes());
            attributes.put("client_registration_id", clientRegistrationId);

            return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .additionalParameters(additionalParameters)
                    .attributes(attributes)
                    .build();
        }

        return authorizationRequest;
    }

}
