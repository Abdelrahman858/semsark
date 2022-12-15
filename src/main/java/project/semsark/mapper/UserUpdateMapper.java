package project.semsark.mapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.semsark.model.request_body.UserUpdate;
import project.semsark.model.entity.User;
import project.semsark.repository.MainProfileRepository;

import java.util.Objects;

@Component
@Setter
@Getter
public class UserUpdateMapper {
    @Autowired
    MainProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    public void mapTo(UserUpdate userDetailsDTO, User user) {

        if (userDetailsDTO.getUsername() != null) {
            user.setUsername(userDetailsDTO.getUsername());
        }
        if (userDetailsDTO.getPassword() != null) {
            user.setPassword(bcryptEncoder.encode(userDetailsDTO.getPassword()));
        }
        if (userDetailsDTO.getImg() != null) {
            user.setImg(userDetailsDTO.getImg());
        }
        if (userDetailsDTO.getEmail() != null) {
            user.setEmail(userDetailsDTO.getEmail());
        }
        if (userDetailsDTO.getPhone() != null) {
            if (!Objects.equals(userDetailsDTO.getPhone(), "0"))
                user.setPhone(userDetailsDTO.getPhone());
        }
    }
}
