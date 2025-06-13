package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import um.g7.Access_Service.Application.DTOs.AdminMailsDTO;
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
    public ResponseEntity<AdminMailsDTO> paginatedAdmins(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
                                                         @RequestParam(value = "nameLookUp", defaultValue = "") String nameLookUp) {
        AdminMailsDTO adminMailsDTO = new AdminMailsDTO(adminService.paginatedAdmins(page, pageSize, nameLookUp));
        return ResponseEntity.ok(adminMailsDTO);
    }
    
    @PutMapping("/auth/change-password")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody CredentialsDTO credentialsDTO) throws BadCredentialsException {
        adminService.changePassword(credentialsDTO.getEmail(), credentialsDTO.getPassword(), credentialsDTO.getOldPassword());        

        return ResponseEntity.ok().build();
    }

}
