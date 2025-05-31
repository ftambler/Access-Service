package um.g7.Access_Service.Application;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import um.g7.Access_Service.Domain.Exception.InvalidTokenException;
import um.g7.Access_Service.Domain.Services.JwtService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwt = parseJwt(request);
        if (jwt != null && jwtService.isValidAdminToken(jwt)) {
            String username = jwtService.getEmailFromToken(jwt);    
            request.setAttribute("email", username);
        }
        else throw new InvalidTokenException("Invalid Token");
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return this.pathMatcher.match("/auth/login", request.getServletPath());
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
    
}
