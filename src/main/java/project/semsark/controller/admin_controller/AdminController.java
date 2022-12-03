package project.semsark.controller.admin_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.semsark.model.UserDetailsDto;
import project.semsark.service.admin_service.AdminService;

import java.util.List;

@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/getAllUsers")
    List<UserDetailsDto>getAllUsers(){
        return adminService.getAllUsers();
    }

}
