package project.semsark.service.admin_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.semsark.exception.HelperMessage;
import project.semsark.mapper.UserDetailsMapper;
import project.semsark.model.request_body.UserDetailsDto;
import project.semsark.model.entity.Profile;
import project.semsark.model.entity.User;
import project.semsark.model.enums.ProfileName;
import project.semsark.repository.MainProfileRepository;
import project.semsark.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsMapper mapper;
    @Autowired
    MainProfileRepository profileRepository;

    public void deleteUser(String email){
        Optional<User>user=userRepository.findByEmail(email);

        if(user.isPresent()){
            userRepository.deleteById(user.get().getId());
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,HelperMessage.USER_NOT_FOUND);
    }

    public void suspendUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            user.get().setSuspended(true);
            userRepository.save(user.get());
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.USER_NOT_FOUND);
    }

    public void unSuspendUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            user.get().setSuspended(false);
            userRepository.save(user.get());
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.USER_NOT_FOUND);

    }

    public void verifyUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            user.get().setVerifyID(true);
            userRepository.save(user.get());
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.USER_NOT_FOUND);
    }

    public void unVerifyUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            user.get().setVerifyID(false);
            userRepository.save(user.get());
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.USER_NOT_FOUND);

    }
    ////////////////////////////////////////////////////////////////////
   public List<UserDetailsDto>getAllUsers(){
        List<UserDetailsDto>list=new ArrayList<>();

       Profile profile=profileRepository.findProfileByName(ProfileName.User.name());

        for(User user:userRepository.findByProfile(profile))
            list.add(mapper.mapTo(user));

        return list;
    }

}
