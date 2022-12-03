package project.semsark.service.admin_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.semsark.mapper.UserDetailsMapper;
import project.semsark.model.UserDetailsDto;
import project.semsark.model.entity.Profile;
import project.semsark.model.entity.User;
import project.semsark.model.enums.ProfileName;
import project.semsark.repository.MainProfileRepository;
import project.semsark.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsMapper mapper;
    @Autowired
    MainProfileRepository profileRepository;

   public List<UserDetailsDto>getAllUsers(){
        List<UserDetailsDto>list=new ArrayList<>();

       Profile profile=profileRepository.findProfileByName(ProfileName.User.name());

        for(User user:userRepository.findByProfile(profile))
            list.add(mapper.mapTo(user));

        return list;
    }

}
