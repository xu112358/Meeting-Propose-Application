package csci310.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.dockerjava.core.MediaType;
import csci310.entity.User;
import csci310.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import static com.google.common.base.Predicates.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController = new UserController();

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void createSignupFormTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/signup")
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("welcome", "welcome"));
    }

    @Test
    public void createUser() throws Exception {
        User user = new User();
        user.setUsername("minyiche");
        user.setFirstName("Minyi");
        user.setLastName("Chen");
        user.setHashPassword("minyi276");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(json)
                ).andExpect(status().isOk());
    }
}