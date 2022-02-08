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

@Component
@Order(1)
public class SecurityFilter implements Filter {

    @Inject
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = this.jwtTokenUtil.getJwtToken((HttpServletRequest) request);
        if (!this.jwtTokenUtil.isExpired(jwtToken) && this.jwtTokenUtil.verifyContainRole(jwtToken, RoleEnum.ASSOCIADO)) {
            chain.doFilter(request, response);
            response.getOutputStream().println();
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

}