package project.semsark.service.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.semsark.HelperMessage;
import project.semsark.jwt.JwtUtil;
import project.semsark.model.response_body.AuthenticationResponse;
import project.semsark.model.entity.OTP;
import project.semsark.model.entity.User;
import project.semsark.model.enums.Using;
import project.semsark.model.request_body.UpdatePasswordRequest;
import project.semsark.repository.OTPRepository;
import project.semsark.repository.UserRepository;
import project.semsark.service.CustomUserDetailsService;
import project.semsark.service.emailSenderService.EmailService;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.UUID;

@Service
public class ForgetPasswordService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    private int otpExpirationInMs;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private JwtUtil jwtUtil;


    @Value("${otp.expirationDateInMs}")
    public void setOtpExpirationInMs(int OtpExpirationInMs) {
        this.otpExpirationInMs = OtpExpirationInMs;
    }

    public void forgetPassword(String email) throws MessagingException {
        userDetailsService.findUserByEmail(email);
        OTP otp = otpRepository.findByEmail(email);
        if (otp == null)
            otp = new OTP();
        String uuid = UUID.randomUUID().toString();
        otp.setOtp(uuid);
        otp.setEmail(email);
        otp.setUsed(Using.PASSWORD.name());
        otp.setCreatedAt(new Date(System.currentTimeMillis()));
        otp.setExpiredDate(new Date(System.currentTimeMillis() + otpExpirationInMs));
        String body = "http://localhost:3000/forgetPassword/" + uuid + ",Change password";
        String subject = "Forget password Link";
        emailService.sendEmail(email, body, subject);
        otpRepository.save(otp);
        throw new ResponseStatusException(HttpStatus.CREATED, HelperMessage.FORGET_PASSWORD_REQUEST);
    }

    public void checkOtp(String otp, String choice) {
        OTP otp1 = otpRepository.findByOtp(otp);
        if (otp1 != null && otp1.getUsed().equals(Using.PASSWORD.name())) {
            if (otp1.getExpiredDate().getTime() - System.currentTimeMillis() > 0) {
                if (choice.equals("delete"))
                    otpRepository.delete(otp1);
            } else {
                otpRepository.delete(otp1);
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, HelperMessage.OTP_EXPIRE);
            }
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.OTP_NOT_FOUND);

    }

    public AuthenticationResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = userDetailsService.findUserByEmail(updatePasswordRequest.getEmail());
        if (user != null) {
            checkOtp(updatePasswordRequest.getOtp(), "delete");
            user.setPassword(bcryptEncoder.encode(updatePasswordRequest.getPassword()));
            userRepository.save(user);
            String token = jwtUtil.generateToken(user);
            return new AuthenticationResponse(token);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.USER_NOT_FOUND);
    }

}






