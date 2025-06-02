package um.g7.Access_Service.Application.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import um.g7.Access_Service.Application.DTOs.CredentialsDTO;
import um.g7.Access_Service.Application.DTOs.AdminTokenDTO;
import um.g7.Access_Service.Domain.Exception.AdminAlreadyExists;
import um.g7.Access_Service.Domain.Exception.BadCredentialsException;
import um.g7.Access_Service.Domain.Services.AdminService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/auth")
public class AdminController {
    
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<AdminTokenDTO> login(@RequestBody CredentialsDTO credentialsDTO) throws BadCredentialsException {
        String token = adminService.login(credentialsDTO.getEmail(), credentialsDTO.getPassword());

        return ResponseEntity.ok(new AdminTokenDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody CredentialsDTO credentialsDTO) throws AdminAlreadyExists {
        adminService.register(credentialsDTO.getEmail(), credentialsDTO.getPassword());       

        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody CredentialsDTO credentialsDTO) throws BadCredentialsException {
        adminService.changePassword(credentialsDTO.getEmail(), credentialsDTO.getPassword(), credentialsDTO.getOldPassword());        

        return ResponseEntity.ok().build();
    }

}
