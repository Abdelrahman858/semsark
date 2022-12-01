package project.semsark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.semsark.HelperMessage;
import project.semsark.jwt.JwtUtil;
import project.semsark.model.AuthenticationRequest;
import project.semsark.model.AuthenticationResponse;
import project.semsark.model.RefreshTokenResponse;
import project.semsark.model.entity.User;
import project.semsark.service.CustomUserDetailsService;

@RestController

public class AuthenticationController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/insecure/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        User user = customUserDetailsService.findUser(authenticationRequest);
        if (user.isSuspended()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, HelperMessage.USER_IS_SUSPEND);
        }
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, HelperMessage.USER_NOT_VERFIED);
        }
        String token = jwtUtil.generateToken(user);
        return new AuthenticationResponse(token);
    }

    @GetMapping(value = "/token/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestHeader(value = "Authorization") String willExpireToken) {
        return ResponseEntity.ok(jwtUtil.generateRefreshToken(willExpireToken));
    }
}
