package com.example.LibraryManagement.jwtauthentication;

import com.example.LibraryManagement.entity.User;
import com.example.LibraryManagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        final String jwtToken ;
        final String username;

        // check if the authheader is present and strts with the bearer

        if(authorizationHeader == null && authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request,response);
            return;

        }

        //ectract the jwt token

        jwtToken = authorizationHeader.substring(7);

        username = jwtService.extractUsername(jwtToken);

        //check if we have the username and no authentication exisits
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //get the userdetails from the database
            User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found"));

            //validate the token
            if(jwtService.isTokenValid(jwtToken, user)){

                //create the authentication using the user roles
                List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,null,authorities
                );

                //set authentication
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //update the security context with authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }
        filterChain.doFilter(request,response);

    }

}
