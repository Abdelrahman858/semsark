package project.semsark.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.semsark.utiles.WebTokenDetails;

import java.security.Principal;

@RestController
public class ResourceController {

    @Autowired
    private WebTokenDetails webTokenDetails;

    @GetMapping(value = "/hellouser")
    @PreAuthorize("hasRole('ALL') or hasRole('ROLE_ADMIN')")
    public void getUser() {
        System.out.println(webTokenDetails.getUsername());
    }

    @GetMapping(value = "/helloadmin")
    @PreAuthorize(value = "hasRole('ALL')")
    public String getAdmin(Principal principal) {
        return "Hello Admin: " + principal.getName();
    }


}
