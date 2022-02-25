package com.gisa.gisaagendamento.filter;

import com.gisa.gisacore.model.RoleEnum;
import com.gisa.gisacore.util.JwtTokenUtil;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class SecurityFilter implements Filter {

    private static final List<String> SWAGGER_PATTERNS = List.of("swagger-ui", "v3/api-docs");

    @Inject
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = this.jwtTokenUtil.getJwtToken((HttpServletRequest) request);
        if (isSwagger(((HttpServletRequest) request).getRequestURI()) ||
                (!this.jwtTokenUtil.isExpired(jwtToken) && this.jwtTokenUtil.verifyContainRole(jwtToken, RoleEnum.ASSOCIADO))) {
            chain.doFilter(request, response);
            response.getOutputStream().println();
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private boolean isSwagger(String path) {
        return path != null && SWAGGER_PATTERNS.stream().anyMatch(pattern -> path.contains(pattern));
    }

}