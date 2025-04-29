package um.g7.Access_Service.Domain.Services;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import um.g7.Access_Service.Domain.Entities.Admin;
import um.g7.Access_Service.Domain.Exception.BadCredentialsException;
import um.g7.Access_Service.Infrastructure.Repositories.AdminRepository;

@Service
public class AdminService {
    
    private final JwtService jwtService;

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String login(String email, String password) throws BadCredentialsException {
        Optional<Admin> optAdmin = adminRepository.getByEmail(email);

        if(optAdmin.isEmpty())
            throw new BadCredentialsException("Bad Credentials");

        Admin admin = optAdmin.get();
        
        if (!passwordEncoder.matches(password, admin.getPassword()))
            throw new BadCredentialsException("Bad Credentials");

        return "Bearer " + jwtService.generateToken(admin.getEmail());        
    }
}
