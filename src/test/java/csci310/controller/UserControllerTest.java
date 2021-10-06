package csci310.controller;

import csci310.entity.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static com.google.common.base.Predicates.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController = new UserController();

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
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("welcome", "welcome"));
    }
}