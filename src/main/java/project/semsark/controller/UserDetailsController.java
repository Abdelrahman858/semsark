package project.semsark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.semsark.jwt.JwtUtil;
import project.semsark.model.response_body.AuthenticationResponse;
import project.semsark.model.request_body.UserDetailsDto;
import project.semsark.model.request_body.UserUpdate;
import project.semsark.model.entity.User;
import project.semsark.service.CustomUserDetailsService;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/insecure/userDetails")
public class UserDetailsController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping(name = "/createUser")
    public AuthenticationResponse saveUser(@Valid @RequestBody UserDetailsDto userDetailsDto) throws MessagingException {
        User user = customUserDetailsService.createUser(userDetailsDto);
        return new AuthenticationResponse(jwtUtil.generateToken(user));

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")

    @PatchMapping(value = "/updateUser")
    public ResponseEntity<UserDetailsDto> updateUser(@Valid @RequestBody UserUpdate userDetailsDto) {
        return ResponseEntity.ok(customUserDetailsService.updateUserByEmail(userDetailsDto));
    }
}
