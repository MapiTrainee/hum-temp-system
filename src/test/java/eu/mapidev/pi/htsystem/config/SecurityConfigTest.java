package eu.mapidev.pi.htsystem.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void anonymousShouldHaveAccessToRootPath() throws Exception {
	this.mockMvc.perform(get("/"))
		.andExpect(unauthenticated())
		.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void anonymousShouldNotBeAbleToGetToTheAdminResources() throws Exception {
	this.mockMvc.perform(get("/console"))
		.andExpect(unauthenticated())
		.andExpect(status().is3xxRedirection());
    }

    @Test
    public void adminShouldBeAbleToLogIn() throws Exception {
	this.mockMvc.perform(formLogin().user("admin").password("admin"))
		.andExpect(authenticated());
    }

    @Test
    public void otherUserShouldNotBeAbleToLogIn() throws Exception {
	this.mockMvc.perform(formLogin().user("user").password("user"))
		.andExpect(unauthenticated());
    }
}
