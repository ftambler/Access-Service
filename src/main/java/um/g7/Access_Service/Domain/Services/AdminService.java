package um.g7.Access_Service.Domain.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import um.g7.Access_Service.Domain.Entities.Admin;
import um.g7.Access_Service.Domain.Exception.AdminAlreadyExists;
import um.g7.Access_Service.Domain.Exception.BadCredentialsException;
import um.g7.Access_Service.Infrastructure.Repositories.AdminRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        
        return jwtService.generateAdminToken(admin.getEmail());
    }
    
    public void register(String email, String password) throws AdminAlreadyExists {
        Optional<Admin> optAdmin = adminRepository.getByEmail(email);
        
        if(optAdmin.isPresent())
            throw new AdminAlreadyExists("Email taken");
        
        Admin admin = Admin.builder()
            .id(UUID.randomUUID())
            .email(email)
            .password(passwordEncoder.encode(password))
            .build();
        
        adminRepository.save(admin);
    }
    
    public void changePassword(String email, String password, String oldPassword) throws BadCredentialsException {
        Optional<Admin> optAdmin = adminRepository.getByEmail(email);

        if(optAdmin.isEmpty())
            throw new BadCredentialsException("Not found");

        Admin admin = optAdmin.get();

        if(!passwordEncoder.matches(oldPassword, admin.getPassword()))
            throw new BadCredentialsException("Wrong Old Password");
        
        admin.setPassword(passwordEncoder.encode(password));

        adminRepository.save(admin);
    }

    public void checkForAdmin() throws AdminAlreadyExists {
        if(adminRepository.count() == 0) register("admin@admin.com", "admin");
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Page<String> paginatedAdmins(int page, int pageSize, String nameLookUp) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("email"));

        return adminRepository.paginatedAdmins(nameLookUp, pageable);
    }
}
