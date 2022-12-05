package project.semsark.mapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.semsark.model.request_body.AuthenticationRequest;
import project.semsark.model.request_body.UserDetailsDto;
import project.semsark.model.request_body.UserSearchParameters;
import project.semsark.model.entity.Profile;
import project.semsark.model.entity.User;
import project.semsark.repository.MainProfileRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsMapper {

    @Autowired
    MainProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    public void mapTo(UserDetailsDto userDetailsDTO, User user) {

        if (userDetailsDTO.getUsername() != null) {
            user.setUsername(userDetailsDTO.getUsername());
        }
        if (userDetailsDTO.getImg() != null) {
            user.setImg(userDetailsDTO.getImg());
        }

        if (userDetailsDTO.getEmail() != null) {
            user.setEmail(userDetailsDTO.getEmail());
        }
        if (userDetailsDTO.getProfileId() != null) {
            user.setProfile(profileRepository.findById(userDetailsDTO.getProfileId()).get());
        }
        if (userDetailsDTO.isSocial()) {
            user.setActive(true);
        }
        if (userDetailsDTO.getPhone() != null) {
            user.setPhone(userDetailsDTO.getPhone());
        }
        user.setPassword(bcryptEncoder.encode(generateCommonLangPassword()));
    }

    public String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

    public UserDetailsDto mapTo(User user) {
        Profile profile = user.getProfile();
        Long profileId = profile != null ? profile.getId() : null;

        return UserDetailsDto.builder().username(user.getUsername()).profileId(profileId).build();
    }

    public UserSearchParameters mapTo(AuthenticationRequest authenticationRequest) {

        return UserSearchParameters.builder()
                .email(authenticationRequest.getEmail()).password(authenticationRequest.getPassword()).build();
    }

}
