package com.renegz.pnccontroller.security;

import com.renegz.pnccontroller.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTTokenFilter extends OncePerRequestFilter {

    final
    JWTTools jwtTools;

    final
    UserService userService;

    private final CustomUserDetailsService customUserDetailsService;

    public JWTTokenFilter(JWTTools jwtTools, UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.jwtTools = jwtTools;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJWTFromRequest(request);
        if (StringUtils.hasText(token) && jwtTools.verifyToken(token)) {
            String username = jwtTools.getUsernameFromToken(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String tokenHeader = request.getHeader("Authorization");
//        String username = null;
//        String token = null;
//
//        if (tokenHeader != null && tokenHeader.startsWith("Bearer ") && tokenHeader.length() > 7) {
//            token = tokenHeader.substring(7);
//
//            try {
//                username = jwtTools.getUsernameFromToken(token);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Unable to get JWT Token");
//            } catch (ExpiredJwtException e) {
//                System.out.println("JWT TOKEN has expired");
//            } catch (MalformedJwtException e) {
//                System.out.println("JWT Malformed");
//            }
//        } else {
//            System.out.println("Bearer string not included in the header or token is empty");
//        }
//
//        if (username != null && token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            User user = userService.findUserByIdentifier(username);
//
//            if (user != null) {
//                Boolean tokenValidity = userService.isTokenValid(user, token);
//
//                if (tokenValidity) {
//                    //Preparing the authentication token.
//                    UsernamePasswordAuthenticationToken authToken
//                            = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//
//                    authToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//
//                    //This line, sets the user to security context to be handled by the filter chain
//                    SecurityContextHolder
//                            .getContext()
//                            .setAuthentication(authToken);
//                }
//                else {
//                    throw new RuntimeException("Token is not valid");
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }

}
