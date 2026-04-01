package az.developia.demo.Entity;


import az.developia.demo.Service.CustomUserDetailsService;
import az.developia.demo.Utility.JwtFilter;
import az.developia.demo.Utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users/verify").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/students/verify").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students/playlist/**").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/users/playlists").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/playlists/playlists").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/playlists/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/teachers/delete/**").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/playlists/update/**").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/playlists/*/delete-picture").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/playlists/*/upload-from-url ").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/playlists/**").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/playlists/**").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/users/playlists/mine").hasAnyAuthority("ROLE_STUDENT","ROLE_TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/students/register").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/students/profile").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/delete").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students/profile").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/teachers/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/teachers/profile").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/teachers/delete").hasAuthority("ROLE_TEACHER").requestMatchers(HttpMethod.DELETE, "/api/teachers/delete").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/teachers/delete").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/teachers/my-students").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/logs").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/reviews").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/categories").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/playlists/create").hasAuthority("ROLE_TEACHER")
                        .requestMatchers(permitAllUrls).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static String[] permitAllUrls = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}