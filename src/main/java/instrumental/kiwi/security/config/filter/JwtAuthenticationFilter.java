package instrumental.kiwi.security.config.filter;

import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.token.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static instrumental.kiwi.exception.MsgCode.OOPS_ERROR;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(

            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain

    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        final String username = jwtService.extractUsername(jwt);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            boolean isTokenValid = tokenRepository.findByToken(jwt)
                    .map(token -> !token.isRevoked() && !token.isExpired())
                    .orElse(false);

            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(

                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else {
                throw new BackendException(OOPS_ERROR);
            }
        }

        filterChain.doFilter(request, response);
    }
}