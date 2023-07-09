package com.rest.expensetracker.filters;

import com.rest.expensetracker.Constraints;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader=httpRequest.getHeader("Authorization");

        if(authHeader != null){
            String[] authHeaderArr=authHeader.split("Bearer");
            if(authHeaderArr.length>1 && authHeaderArr[1] !=null){
                String token=authHeaderArr[1];

                try{
                    Claims claims= Jwts.parser().setSigningKey(Constraints.API_SERECT_KEY)
                            .parseClaimsJws(token).getBody();
                    httpRequest.setAttribute("userId",Integer.parseInt(claims.get("userId").toString()));

                }catch(Exception e){
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Invalid/Expired token");
                    return;
                }
            }
            else{
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Authorization token must be Bearer {token}");
                return;
            }
        }
        else{
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Authorization token must be provided");
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
