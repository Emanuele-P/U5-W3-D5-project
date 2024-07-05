package ep2024.u5w3d5.security;

import ep2024.u5w3d5.entities.User;
import ep2024.u5w3d5.exceptions.UnauthorizedException;
import ep2024.u5w3d5.repositories.UsersDAO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UsersDAO usersDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Please input correctly your token in header.");
        String accessToken = authHeader.substring(7);
        jwtTools.verifyToken(accessToken);
        String id = jwtTools.extractIdFromToken(accessToken);
        Optional<User> currentUser = usersDAO.findById(UUID.fromString(id));

        if (currentUser.isPresent()) {
            User currentAuthorized = currentUser.get();
            Authentication authentication = new UsernamePasswordAuthenticationToken(currentAuthorized, null, currentAuthorized.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authenticated user: " + currentAuthorized);
        } else {
            throw new UnauthorizedException("User not found.");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}