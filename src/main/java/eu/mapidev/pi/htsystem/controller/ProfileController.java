package eu.mapidev.pi.htsystem.controller;

import eu.mapidev.pi.htsystem.config.Profiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Profile(Profiles.DEVELOPMENT)
@Controller
public class ProfileController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @RequestMapping("/profile")
    @ResponseBody
    public String profile() {
	return activeProfile;
    }

}
