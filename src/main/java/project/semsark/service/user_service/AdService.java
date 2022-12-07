package project.semsark.service.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.semsark.jwt.JwtUtil;
import project.semsark.mapper.AdMapper;
import project.semsark.model.entity.Building;
import project.semsark.model.entity.User;
import project.semsark.model.request_body.AdRequest;
import project.semsark.repository.BuildingRepository;
import project.semsark.repository.UserRepository;

@Service
public class AdService {
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdMapper adMapper;
    @Autowired
    JwtUtil jwtUtil;

    public void createAd(AdRequest adRequest){
        Building building = buildingRepository.getBuildingByLangAndLat(adRequest.getLang(),adRequest.getLat());
        while (building !=null){
            adRequest.setLang(adRequest.getLang()+1);
            adRequest.setLat(adRequest.getLat()+1);
            building = buildingRepository.getBuildingByLangAndLat(adRequest.getLang(),adRequest.getLat());
        }
        Building ad = new Building();
        adMapper.mapTo(adRequest,ad);
        User user = jwtUtil.getUserDataFromToken();

        ad.setUserId(user.getId());
        Building myAd = buildingRepository.save(ad);
        user.getMyAds().add(myAd);
        userRepository.save(user);
    }

}
