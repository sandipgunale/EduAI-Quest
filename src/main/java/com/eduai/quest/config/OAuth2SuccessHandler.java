package com.eduai.quest.config;

import com.eduai.quest.model.dto.auth.JwtResponse;
import com.eduai.quest.service.AuthService;
import com.eduai.quest.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            log.info("OAuth2 login successful for provider: {}", oauthToken.getAuthorizedClientRegistrationId());
            log.info("OAuth2 user attributes: {}", oAuth2User.getAttributes());

            // Extract user information safely
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = getAttribute(attributes, "email");
            String name = getAttribute(attributes, "name");
            String googleId = getAttribute(attributes, "sub");

            if (email == null) {
                throw new RuntimeException("Email not found in OAuth2 response");
            }

            // Use the proper method that returns JwtResponse
            JwtResponse jwtResponse = authService.loginOrRegisterOAuthUser(email, name, googleId);

            // Build redirect URL with all necessary information
            String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/success")
                    .queryParam("token", jwtResponse.getAccessToken())
                    .queryParam("email", jwtResponse.getEmail())
                    .queryParam("name", jwtResponse.getName() != null ? jwtResponse.getName() : "")
                    .queryParam("tokenType", jwtResponse.getTokenType())
                    .build().toUriString();

            log.info("Redirecting to frontend: {}", redirectUrl.replace(jwtResponse.getAccessToken(), "***"));
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 authentication success handling failed", e);

            // Redirect to error page
            String errorUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/error")
                    .queryParam("error", "authentication_failed")
                    .queryParam("message", e.getMessage())
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }

    private String getAttribute(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        return value != null ? value.toString() : null;
    }
}