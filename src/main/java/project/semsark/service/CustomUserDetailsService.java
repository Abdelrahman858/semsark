package project.semsark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.semsark.HelperMessage;
import project.semsark.mapper.UserDetailsMapper;
import project.semsark.mapper.UserUpdateMapper;
import project.semsark.model.AuthenticationRequest;
import project.semsark.model.UserDetailsDto;
import project.semsark.model.UserSearchParameters;
import project.semsark.model.UserUpdate;
import project.semsark.model.entity.Role;
import project.semsark.model.entity.User;
import project.semsark.repository.UserRepository;
import project.semsark.repository.specification.UserSpecifications;
import project.semsark.service.auth_service.VerifyEmailService;
import project.semsark.validation.UserDetailsValidator;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    VerifyEmailService verifyEmailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsMapper userDetailsMapper;
    @Autowired
    private UserUpdateMapper userUpdateMapper;
    @Autowired
    private UserDetailsValidator userDetailsValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the name " + username));

        for (Role role : user.getProfile().getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }

    public User createUser(UserDetailsDto userDetailsDTO) throws MessagingException {

        User user = new User();
        userDetailsMapper.mapTo(userDetailsDTO, user);

        if (!userDetailsDTO.isSocial()) {
            userDetailsValidator.validate(userDetailsDTO.getEmail());
            verifyEmailService.sendEmailVerification(userDetailsDTO.getEmail());
            userRepository.save(user);
            throw new ResponseStatusException(HttpStatus.CREATED, HelperMessage.EMAIL_CREATED);
        } else {
            Optional<User> user1 = userRepository.findByEmail(userDetailsDTO.getEmail());
            if (user1.isPresent()) {
                user.setId(user1.get().getId());
                return user;
            } else
                return userRepository.save(user);
        }
    }

    public UserDetailsDto updateUserByEmail(UserUpdate userUpdate) {

        User existUser = findUserByEmail(userUpdate.getOldEmail());
        userUpdateMapper.mapTo(userUpdate, existUser);
        User updatedUser = userRepository.save(existUser);

        return userDetailsMapper.mapTo(updatedUser);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, HelperMessage.USER_NOT_FOUND));
    }

    public User findUser(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getSocial())
            return userRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, HelperMessage.USER_NOT_FOUND));


        UserSearchParameters userSearchParameters = userDetailsMapper.mapTo(authenticationRequest);

        UserSpecifications userSpecifications = UserSpecifications.builder()
                .userSearchParameters(userSearchParameters)
                .build();

        return userRepository.findAll(userSpecifications).stream()
                .filter(filterdUser -> passwordEncoder.matches(userSearchParameters.getPassword(), filterdUser.getPassword()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, HelperMessage.USER_NOT_FOUND));
    }
}
