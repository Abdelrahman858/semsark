package project.semsark.service.authService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.semsark.HelperMessage;
import project.semsark.jwt.JwtUtil;
import project.semsark.model.AuthenticationResponse;
import project.semsark.model.entity.OTP;
import project.semsark.model.entity.User;
import project.semsark.model.enums.Using;
import project.semsark.model.requestBody.UpdatePasswordRequest;
import project.semsark.repository.OTPRepository;
import project.semsark.repository.UserRepository;
import project.semsark.service.CustomUserDetailsService;
import project.semsark.service.emailSenderService.EmailService;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class VerifyEmailService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    private int otpExpirationInMs;
    @Autowired
    private OTPRepository otpRepository;

    @Value("${otp.expirationDateInMs}")
    public void setOtpExpirationInMs(int OtpExpirationInMs) {
        this.otpExpirationInMs = OtpExpirationInMs;
    }

    public void sendEmailVerification(String email) throws MessagingException {
        OTP otp = otpRepository.findByEmail(email);
        if (otp == null)
            otp = new OTP();
        String uuid = UUID.randomUUID().toString();
        otp.setOtp(uuid);
        otp.setEmail(email);
        otp.setCreatedAt(new Date(System.currentTimeMillis()));
        otp.setExpiredDate(new Date(System.currentTimeMillis() + otpExpirationInMs));
        otp.setUsed(Using.EMAIL.name());
        String body = "http://localhost:3000/verifyEmail/" + uuid + ",Confirm Account";
        ;
        String subject = "Verify email Link";
        emailService.sendEmail(email, body, subject);
        otpRepository.save(otp);
    }

    @Transactional
    public String checkOtpValid(String otp, String choice) {
        OTP otp1 = otpRepository.findByOtp(otp);
        if (otp1 != null && otp1.getUsed().equals(Using.EMAIL.name())) {
            if (choice.equals("delete")) {
                otpRepository.delete(otp1);
                return "Done";
            }
            User user = userDetailsService.findUserByEmail(otp1.getEmail());
            user.setActive(true);
            userRepository.save(user);
            return otp1.getEmail();
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.OTP_NOT_FOUND);

    }

    public AuthenticationResponse createPassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = userDetailsService.findUserByEmail(updatePasswordRequest.getEmail());
        if (user != null) {
            checkOtpValid(updatePasswordRequest.getOtp(), "delete");
            user.setPassword(bcryptEncoder.encode(updatePasswordRequest.getPassword()));
            userRepository.save(user);
            String token = jwtUtil.generateToken(user);
            return new AuthenticationResponse(token);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.USER_NOT_FOUND);
    }


}
