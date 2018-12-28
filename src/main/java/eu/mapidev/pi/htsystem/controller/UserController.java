package eu.mapidev.pi.htsystem.controller;

import eu.mapidev.pi.htsystem.config.Profiles;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Profile(Profiles.DEVELOPMENT)
@Controller
public class UserController {

    @RequestMapping("/**")
    @ResponseBody
    public String handler(HttpServletRequest request) {
	Authentication auth = SecurityContextHolder.getContext()
		.getAuthentication();
	StringBuilder body = new StringBuilder();
	body.append("uri:").append(request.getRequestURI());
	body.append(", user:").append(auth.getName());
	body.append(", roles:").append(auth.getAuthorities());
	body.append(", is authenticated:").append(auth.isAuthenticated());
	return body.toString();
    }
}
