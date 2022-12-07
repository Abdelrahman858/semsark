package project.semsark.service.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.semsark.exception.HelperMessage;
import project.semsark.jwt.JwtUtil;
import project.semsark.model.entity.Emails;
import project.semsark.model.entity.OTP;
import project.semsark.model.enums.Using;
import project.semsark.repository.EmailsRepository;
import project.semsark.repository.OTPRepository;
import project.semsark.repository.UserRepository;
import project.semsark.service.CustomUserDetailsService;
import project.semsark.service.email_sender_service.EmailSenderService;
import project.semsark.service.email_sender_service.EmailService;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;

import static project.semsark.global_methods.GlobalMethods.generateOtp;

@Service
public class VerifyEmailService {
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    EmailsRepository emailsRepository;
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
        if (userRepository.findByEmail(email).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HelperMessage.EMAIL_ALREADY_VERIFIED);
        else if (emailsRepository.findByEmail(email).isPresent()) {
            OTP otp = otpRepository.findByEmail(email);
            if (otp == null)
                otp = new OTP();
            otp.setOtp(generateOtp());
            otp.setEmail(email);
            otp.setCreatedAt(new Date(System.currentTimeMillis()));
            otp.setExpiredDate(new Date(System.currentTimeMillis() + otpExpirationInMs));
            otp.setUsed(Using.EMAIL.name());
            Emails e = emailsRepository.findByEmail(email).get();
            e.setVerified(false);
            emailsRepository.save(e);
            String body = otp.getOtp();
            String subject = "Verify email OTP";
            emailSenderService.sendSimpleEmail(email, body, subject);
            otpRepository.save(otp);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HelperMessage.EMAIL_WRONG);
    }


    @Transactional
    public String checkOtpValid(String otp, String choice) {
        OTP otp1 = otpRepository.findByOtp(otp);
        if (otp1 != null && otp1.getUsed().equals(Using.EMAIL.name())) {
            if (choice.equals("delete")) {
                otpRepository.delete(otp1);
                return "Done";
            }
            Emails emails = emailsRepository.findByEmail(otp1.getEmail()).get();
            emails.setVerified(true);
            emailsRepository.save(emails);
            otpRepository.delete(otp1);
            return otp1.getEmail();
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.OTP_NOT_FOUND);

    }

}
