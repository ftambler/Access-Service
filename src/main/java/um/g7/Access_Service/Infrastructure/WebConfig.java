package um.g7.Access_Service.Infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import um.g7.Access_Service.Domain.Exception.AdminAlreadyExists;
import um.g7.Access_Service.Domain.Services.AdminService;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final AdminService adminService;

    @Value("${security.cors.frontendOrigin}")
    private String frontendOrigin;

    public WebConfig(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(frontendOrigin) // Allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("Content-Type", "Authorization") // Allowed headers
                .maxAge(3600);
    }

    @Bean
    public int checkForAdmin() throws AdminAlreadyExists {
        adminService.checkForAdmin();
        return 1;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable()) // usa la config global de CorsConfigurer
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().permitAll() // provisorio
                );

        return http.build();
    }
}
