package com.missionchecker.support;

import com.missionchecker.annotation.Role;
import com.missionchecker.domain.MemberRole;
import com.missionchecker.service.RoleService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

/**
 * 로그인한 사용자의 경우 세션에 있는 사용자 정보를 꺼내어 핸들러 매개변수에 넣어준다.
 */
@Component
@RequiredArgsConstructor
public class LoginMemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String NOT_EXISTED_LOGIN_MEMBER_MESSAGE = "세션 내 로그인 사용자 정보가 존재하지 않습니다.";
    public static final String HAS_INVALID_ROLE_MESSAGE = "접근 가능한 역할이 아닙니다.";
    private static final String MISSION_ID_KEY = "missionId";
    private final RoleService roleService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterAnnotation(Role.class) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        SessionMember sessionMember = (SessionMember) webRequest.getAttribute(SessionMember.LOGIN_MEMBER_SESSION_KEY,
                RequestAttributes.SCOPE_SESSION);
        if (sessionMember == null) {
            throw new IllegalStateException(NOT_EXISTED_LOGIN_MEMBER_MESSAGE);
        }

        Role role = parameter.getParameterAnnotation(Role.class);
        // 요청 처리를 위해 요구되는 역할
        MemberRole requestedRole = role.value();
        // 요청 처리를 위해 Administrator 또는 Owner 역할을 필요로 하면 검증 후 진행
        if (requestedRole == MemberRole.ADMINISTRATOR || requestedRole == MemberRole.OWNER) {
            Long missionId = extractMissionId(webRequest);
            boolean hasValidRoleForAccess = roleService.hasValidRoleForAccess(requestedRole, missionId,
                    sessionMember.getId());
            if (!hasValidRoleForAccess) {
                throw new IllegalStateException(HAS_INVALID_ROLE_MESSAGE);
            }
        }
        return sessionMember;
    }

    /**
     * PathVariable 중에서 mission id 를 추출한다
     *
     * @param webRequest
     * @return
     */
    private Long extractMissionId(NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String> pathVariables = (Map<String, String>) httpServletRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.parseLong(pathVariables.get(MISSION_ID_KEY));
    }
}
