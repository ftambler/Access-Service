package um.g7.Access_Service.Infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import um.g7.Access_Service.Domain.Exception.AdminAlreadyExists;
import um.g7.Access_Service.Domain.Services.AdminService;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final AdminService adminService;

    public WebConfig(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public int checkForAdmin() throws AdminAlreadyExists {
        adminService.checkForAdmin();
        return 1;
    }
}
