package um.g7.Access_Service.Application.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import um.g7.Access_Service.Application.DTOs.CredentialsDTO;
import um.g7.Access_Service.Domain.Services.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AdminController {
    
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody CredentialsDTO credentialsDTO) {
        String token = adminService.login(credentialsDTO.getEmail(), credentialsDTO.getPassword());

        return ResponseEntity.ok(token);
    }
    

}
