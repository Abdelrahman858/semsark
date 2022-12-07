package project.semsark.controller.user_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.semsark.model.request_body.AdRequest;
import project.semsark.service.user_service.AdService;

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

}
