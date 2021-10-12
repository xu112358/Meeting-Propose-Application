package csci310.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import csci310.entity.User;
import csci310.filter.LoginInterceptor;
import csci310.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }



    @Test
    public void index() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/")
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/signin")
        ).andExpect(status().isOk());
    }

    @Test
    public void createSignupFormTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/signup")
        ).andExpect(status().isOk());
                //.andExpect(MockMvcResultMatchers.model().attribute("welcome", "welcome"));
    }

    @Test
    @Transactional
    public void createUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username","test");
        params.add("password","123");
        params.add("re_password","123");
        params.add("fname","tommy");
        params.add("lname","trojan");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signup").params(params)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        ModelMap map=mvcResult.getModelAndView().getModelMap();
        System.out.println(map);
        Assert.assertEquals("Sign Up Successfully!",map.get("message"));

        Assert.assertEquals(200,status);


        params.set("username","root");
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signup").params(params)).andReturn();
        status = mvcResult.getResponse().getStatus();

        map=mvcResult.getModelAndView().getModelMap();
        Assert.assertEquals("Username is taken. Try another one.",map.get("error_message"));

        Assert.assertEquals(200,status);

    }

    @Test
    @Transactional
    public void signinTest() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username","test");
        params.add("password","123");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();

//        HttpServletRequest request= mvcResult.getRequest();
//        request.getSession().setAttribute("loginUser","ABC");
//        HttpServletResponse response= mvcResult.getResponse();
//        boolean output= loginInterceptor.preHandle(request,response,null);
//        System.out.println("loginInterceptor= "+output);

        int status = mvcResult.getResponse().getStatus();

        ModelMap map=mvcResult.getModelAndView().getModelMap();
        System.out.println(map);
        Assert.assertEquals("Username does not exist!",map.get("error_message"));
        Assert.assertEquals("123",map.get("password"));
        Assert.assertEquals("test",map.get("username"));
        Assert.assertEquals(200,status);


        params.set("username","root");
        mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andExpect(redirectedUrl("/home")).andExpect(status().isFound());
//        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();
//        status = mvcResult.getResponse().getStatus();
//        map=mvcResult.getModelAndView().getModelMap();
//        Assert.assertEquals("Login successfully",map.get("message"));
//        Assert.assertEquals(200,status);


        params.set("password","1111");
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();
        status = mvcResult.getResponse().getStatus();
        map=mvcResult.getModelAndView().getModelMap();
        Assert.assertEquals("Username and password do not match!",map.get("error_message"));
        Assert.assertEquals("1111",map.get("password"));
        Assert.assertEquals("root",map.get("username"));
        Assert.assertEquals(200,status);

    }
    @Test
    public void logout() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username","root");
        params.add("password","123");


        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();
        MockHttpSession session = (MockHttpSession) mvcResult.getRequest().getSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/logout").session(session)).andExpect(redirectedUrl("/signin")).andExpect(status().isFound());
    }
    @Test
    public void access_home_without_login() throws Exception{
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/home")).andReturn();
        String value= (String) mvcResult.getRequest().getAttribute("error_message");

        Assert.assertEquals("You need to log in first!",value);
    }

}