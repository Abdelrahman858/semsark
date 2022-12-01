package project.semsark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.semsark.model.MainProfileRequest;
import project.semsark.model.ProfileResponse;
import project.semsark.service.MainProfileService;

import javax.validation.Valid;

@RestController
@RequestMapping("/insecure/mainprofile")
public class ProfileController {

    @Autowired
    private MainProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody MainProfileRequest profileRequest) {
        return ResponseEntity.ok(profileService.createProfile(profileRequest));
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody MainProfileRequest profileRequest,
                                                         @PathVariable Long profileId) {
        return ResponseEntity.ok(profileService.updateProfile(profileId, profileRequest));
    }
}
