package project.semsark.controller.verify_email_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.semsark.model.response_body.AuthenticationResponse;
import project.semsark.model.request_body.UpdatePasswordRequest;
import project.semsark.service.auth_service.VerifyEmailService;

@RestController


public class VerifyEmailController {
    @Autowired
    VerifyEmailService verifyEmailService;

    @PostMapping(value = "/verifyEmail/{otp}")
    public String verifyEmail(@PathVariable String otp) {
        return verifyEmailService.checkOtpValid(otp, "wait");
    }

    @PostMapping(value = "/createPassword")
    public AuthenticationResponse createPassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return verifyEmailService.createPassword(updatePasswordRequest);
    }

}
