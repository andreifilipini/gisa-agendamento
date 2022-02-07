package com.gisa.gisaagendamento.filter;

import com.gisa.gisacore.util.JwtTokenUtil;
import com.gisa.gisacore.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class SecurityFilter implements Filter {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiresIn}")
    private Integer expiresIn;

    private JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    private void init() {
        this.jwtTokenUtil = new JwtTokenUtil(secret, expiresIn);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = getJwtToken((HttpServletRequest) request);
        if (!this.jwtTokenUtil.isExpired(jwtToken) && this.jwtTokenUtil.verifyContainRole(jwtToken, "ASSOCIADO")) {
            chain.doFilter(request, response);
            response.getOutputStream().println();
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private String getJwtToken(HttpServletRequest request) {
        HttpServletRequest req = request;
        String token = req.getHeader("Authorization");

        if(StringUtil.isNotBlank(token)) {
            return token.replace("Bearer ",  "");
        }
        return null;
    }

}