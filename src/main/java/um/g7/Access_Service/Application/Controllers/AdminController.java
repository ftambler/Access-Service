package um.g7.Access_Service.Application.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import um.g7.Access_Service.Application.DTOs.AdminTokenDTO;
import um.g7.Access_Service.Application.DTOs.CredentialsDTO;
import um.g7.Access_Service.Domain.Exception.AdminAlreadyExists;
import um.g7.Access_Service.Domain.Exception.BadCredentialsException;
import um.g7.Access_Service.Domain.Services.AdminService;

@RestController
@RequestMapping()
public class AdminController {
    
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AdminTokenDTO> login(@RequestBody CredentialsDTO credentialsDTO) throws BadCredentialsException {
        String token = adminService.login(credentialsDTO.getEmail(), credentialsDTO.getPassword());

        return ResponseEntity.ok(new AdminTokenDTO(token));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<HttpStatus> register(@RequestBody CredentialsDTO credentialsDTO) throws AdminAlreadyExists {
        adminService.register(credentialsDTO.getEmail(), credentialsDTO.getPassword());       

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admins")
    public ResponseEntity<Page<String>> paginatedAdmins(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
                                                        @RequestParam(value = "nameLookUp", defaultValue = "") String nameLookUp) {
        return ResponseEntity.ok(adminService.paginatedAdmins(page, pageSize, nameLookUp));
    }
    
    @PutMapping("/auth/change-password")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody CredentialsDTO credentialsDTO) throws BadCredentialsException {
        adminService.changePassword(credentialsDTO.getEmail(), credentialsDTO.getPassword(), credentialsDTO.getOldPassword());        

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admins/self")
    public ResponseEntity<?> deleteAdminSelfAccount(HttpServletRequest request) {
        String adminEmail = (String) request.getAttribute("email");

        adminService.deleteAdminSelfAccount(adminEmail);

        return ResponseEntity.ok().build();
    }
}
