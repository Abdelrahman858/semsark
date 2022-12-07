package project.semsark.controller.user_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.semsark.model.entity.Building;
import project.semsark.model.request_body.AdRequest;
import project.semsark.service.user_service.AdService;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {
    @Autowired
    private AdService adService;
    @PostMapping("/createAd")
    void createAd(@RequestBody AdRequest adRequest){
        adService.createAd(adRequest);
    }

    @PatchMapping("/updateAd/{id}")
    void updateAd(@RequestBody AdRequest adRequest,@PathVariable long id){
        adService.updateAd(adRequest,id);
    }

    @DeleteMapping("/deleteAd/{id}")
    void deleteAd(@PathVariable long id){
        adService.deleteAd(id);
    }
    ///////////////////////////////////////////////////////////////////////
    @GetMapping("/getAllAds")
    List<Building> getAllAds(){
        return adService.getAllAds();
    }

    @GetMapping("/getMyAds")
    List<Building> getMyAds(){
        return adService.getMyAds();
    }
}
