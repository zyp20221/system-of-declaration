package com.zyp.demo.interceptor;

import com.zyp.demo.exception.UnauthorizedException;
import com.zyp.demo.utils.JwtTool;
import com.zyp.demo.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTool jwtTool;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的 token
        String token = request.getHeader("Authorization");

        // 2.校验token是否为空
        if (token == null || !token.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未提供token");
            return false;
        }

        // 3. 解析 token，获取用户 ID
        Long userId;
        try {
            userId = jwtTool.parseToken(token.substring(7)); // 去掉 "Bearer " 前缀
        } catch (UnauthorizedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }

        // 4.存入上下文
        UserContext.setUser(userId);

        // 5.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理用户
        UserContext.removeUser();
    }
}