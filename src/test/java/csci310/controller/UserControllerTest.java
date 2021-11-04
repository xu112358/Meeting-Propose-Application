package csci310.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import csci310.entity.Event;
import csci310.entity.User;
import csci310.filter.LoginInterceptor;
import csci310.model.InviteModel;
import csci310.repository.EventRepository;
import csci310.repository.InviteRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

    @Mock
    private EventRepository eventRepository;

    @Mock
    InviteRepository inviteRepository;

    @Mock
    HttpSession session;

    private MockMvc mockMvc;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //when(loginInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        User sender = new User();
        sender.setUsername("minyiche1");
        sender.setHashPassword("password");
        userRepository.save(sender);

        User receiver = new User();
        receiver.setUsername("minyiche2");
        receiver.setHashPassword("password");
        userRepository.save(receiver);
        receiver.setUsername("minyiche3");
        userRepository.save(receiver);
    }



    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/")
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/signin")
        ).andExpect(status().isOk());
    }

    @Test
    public void testCreateSignupFormTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/signup")
        ).andExpect(status().isOk());
                //.andExpect(MockMvcResultMatchers.model().attribute("welcome", "welcome"));
    }

    @Test
    @Transactional
    public void testCreateUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username","test");
        params.add("password","123");
        params.add("re_password","123");
//        params.add("fname","tommy");
//        params.add("lname","trojan");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signup").params(params)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        ModelMap map=mvcResult.getModelAndView().getModelMap();
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
    public void testSignin() throws Exception {
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
    @Transactional
    public void testSendInvite() throws Exception{
        List<String> receivers = new ArrayList<>();

        receivers.add("minyiche2");

        List<Event> events = new ArrayList<>();
        java.sql.Date eventDate =  java.sql.Date.valueOf("2021-10-16");

        Event event1 = new Event();
        event1.setEventName("event1");
        event1.setGenre("event");
        event1.setEventDate(eventDate);
        event1.setLocation("LA");
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setEventName("event2");
        event2.setGenre("event");
        event2.setEventDate(eventDate);
        event2.setLocation("LA");
        eventRepository.save(event2);

        events.add(event1);
        events.add(event2);

        InviteModel inviteModel = new InviteModel();
        inviteModel.setSender("minyiche1");
        inviteModel.setInvite_name("invite");
        inviteModel.setReceivers(receivers);
        inviteModel.setEvents(events);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(inviteModel);
        System.out.println(json);
        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", "minyiche1")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("you are blocked by minyiche2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("400")));

        receivers = new ArrayList<>();
        receivers.add("minyiche3");

        inviteModel = new InviteModel();
        inviteModel.setSender("minyiche1");
        inviteModel.setInvite_name("invite");
        inviteModel.setReceivers(receivers);
        inviteModel.setEvents(events);

        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        json = ow.writeValueAsString(inviteModel);
        System.out.println(json);
        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", "minyiche1")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Invite Sent")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("200")));
    }

    @Test
    @Transactional
    public void testFindUserInvite() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/find-user-invite")
                        .param("username", "minyiche2")
                        .sessionAttr("loginUser", "minyiche1")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].inviteName", is("invite")));

    }

    @Test
    public void testEventSearch() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username","minyiche3");
        params.add("invite_id","6");

        mockMvc.perform(MockMvcRequestBuilders.get("/search-event-by-invite-and-username")
                        .params(params)
                        .sessionAttr("loginUser", "minyiche1")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventName", is("event1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventName", is("event2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(10))); //this can be changed with regard to data in database
    }

    @Test
    @Transactional
    public void testFinalizeInvite() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/finalize-invite")
                        .param("invite_id","6")
                        .sessionAttr("loginUser", "minyiche1")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testLogout() throws Exception {
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


    @Test
    public void testUsernameStartingWith() throws Exception {
        String json="{\"name\":\"r\"}";
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("/usernameStartingWith")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", "minyiche1")
                ).andReturn();
        String response_json=mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> map = mapper.readValue(response_json, Map.class);
        List<String> names=map.get("names");
        Boolean found=true;
        for(String name:names){
            if(name.substring(0).equalsIgnoreCase("r")){
                found=false;
                break;
            }
        }

        Assert.assertTrue(found);
    }
}