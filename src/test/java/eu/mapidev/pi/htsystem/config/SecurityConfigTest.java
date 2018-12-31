package eu.mapidev.pi.htsystem.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void anonymousShouldHaveAccessToRootPath() throws Exception {
	mockMvc.perform(get("/"))
		.andExpect(unauthenticated())
		.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void anonymousShouldNotBeAbleToGetToTheAdminResources() throws Exception {
	mockMvc.perform(get("/console"))
		.andExpect(unauthenticated())
		.andExpect(status().is4xxClientError());
    }

    @Test
    public void adminShouldBeAbleToLogIn() throws Exception {
	mockMvc.perform(get("/user").with(httpBasic("admin", "admin")))
		.andExpect(authenticated());
    }

    @Test
    public void otherUserShouldNotBeAbleToLogIn() throws Exception {
	mockMvc.perform(get("/user").with(httpBasic("admin", "admin")))
		.andExpect(authenticated());
    }
}
