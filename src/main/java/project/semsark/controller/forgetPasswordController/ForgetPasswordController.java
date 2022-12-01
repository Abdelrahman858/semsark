package project.semsark.controller.forgetPasswordController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.semsark.model.AuthenticationResponse;
import project.semsark.model.requestBody.EmailRequest;
import project.semsark.model.requestBody.OtpRequest;
import project.semsark.model.requestBody.UpdatePasswordRequest;
import project.semsark.service.authService.ForgetPasswordService;

import javax.mail.MessagingException;

@RestController


public class ForgetPasswordController {

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @PostMapping(value = "/forgetPassword")
    public void forgetPassword(@RequestBody EmailRequest email) throws MessagingException {
        forgetPasswordService.forgetPassword(email.getEmail());
    }

    @PostMapping(value = "/checkOtp")
    public void checkOtp(@RequestBody OtpRequest otp) {
        forgetPasswordService.checkOtp(otp.getOtp(), "check");
    }

    @PostMapping(value = "/updatePassword")
    public AuthenticationResponse updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return forgetPasswordService.updatePassword(updatePasswordRequest);
    }


}
