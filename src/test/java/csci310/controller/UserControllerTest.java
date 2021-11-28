package csci310.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import csci310.entity.Event;
import csci310.entity.Invite;
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
import org.springframework.test.web.servlet.ResultActions;
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

    /*@Mock
    private UserRepository userRepositoryMock;

    @Mock
    private EventRepository eventRepositoryMock;

    @Mock
    InviteRepository inviteRepositoryMock;*/

    @Mock
    HttpSession session;


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EventRepository eventRepository;


    @Autowired
    InviteRepository inviteRepository;



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
    }


    @Test
    //@Transactional
    //generate data for testing
    public void generateUserTestingData() throws Exception {
        /*List<String> receivers = new ArrayList<>();

        receivers.add("minyiche2");
        receivers.add("minyiche3");

        List<Event> events = new ArrayList<>();
        java.sql.Date eventDate =  java.sql.Date.valueOf("2021-10-16");

        Event event1 = new Event();
        event1.setEventName("Justin Bieber");
        event1.setGenre("Music");
        event1.setEventDate(eventDate);
        event1.setLocation("Los Angeles");

        Event event2 = new Event();
        event2.setEventName("KaRol G");
        event2.setGenre("Music");
        event2.setEventDate(eventDate);
        event2.setLocation("Los Angeles");

        Event event3 = new Event();
        event3.setEventName("Bad Bunny");
        event3.setGenre("Music");
        event3.setEventDate(eventDate);
        event3.setLocation("Los Angeles");

        Event event4 = new Event();
        event4.setEventName("Knotfest: Los Angeles");
        event4.setGenre("Music");
        event4.setEventDate(eventDate);
        event4.setLocation("Los Angeles");

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        InviteModel inviteModel = new InviteModel();
        inviteModel.setSender("minyiche1");
        inviteModel.setInvite_name("Music Invite");
        inviteModel.setReceivers(receivers);
        inviteModel.setEvents(events);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(inviteModel);

        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche1")
        );

        //one more invite
        receivers = new ArrayList<>();
        receivers.add("minyiche3");
        receivers.add("minyiche4");

        events.remove(0);

        inviteModel = new InviteModel();
        inviteModel.setSender("minyiche1");
        inviteModel.setInvite_name("Friend Invite");
        inviteModel.setReceivers(receivers);
        inviteModel.setEvents(events);

        json = ow.writeValueAsString(inviteModel);

        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche1")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche1")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche1")
        );

        //add blocked user
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche3");
        params.add("block", "minyiche2");
        mockMvc.perform(MockMvcRequestBuilders.post("/add-blocked-user")
                .params(params)
                .sessionAttr("loginUser", "minyiche1")
        );

        params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche4");
        params.add("block", "minyiche2");
        mockMvc.perform(MockMvcRequestBuilders.post("/add-blocked-user")
                .params(params)
                .sessionAttr("loginUser", "minyiche1")
        );

        params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche2");
        params.add("block", "minyiche1");
        mockMvc.perform(MockMvcRequestBuilders.post("/add-blocked-user")
                .params(params)
                .sessionAttr("loginUser", "minyiche1")
        );
        params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche4");
        params.add("block", "minyiche3");
        mockMvc.perform(MockMvcRequestBuilders.post("/add-blocked-user")
                .params(params)
                .sessionAttr("loginUser", "minyiche1")
        );

        events = new ArrayList<>();
        event1 = new Event();
        event1.setId(8L);
        event1.setAvailability("yes");
        event1.setPreference(5);

        event2 = new Event();
        event2.setId(9L);
        event2.setAvailability("maybe");
        event2.setPreference(2);

        event3 = new Event();
        event3.setId(10L);
        event3.setAvailability("maybe");
        event3.setPreference(4);

        event4 = new Event();
        event4.setId(11L);
        event4.setAvailability("no");
        event4.setPreference(1);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);


        Invite invite = new Invite();
        invite.setId(7L);
        invite.setInvite_events_list(new ArrayList<>(Arrays.asList(event1, event2, event3, event4)));

        json = ow.writeValueAsString(invite);

        mockMvc.perform(MockMvcRequestBuilders.post("/reply-invite")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche2")
        ).andExpect(status().isOk());

        events.get(0).setId(12L);
        events.get(1).setId(13L);
        events.get(2).setId(14L);
        events.get(3).setId(15L);

        invite.setInvite_events_list(new ArrayList<>(Arrays.asList(event1, event2, event3, event4)));

        json = ow.writeValueAsString(invite);

        mockMvc.perform(MockMvcRequestBuilders.post("/reply-invite")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche3")
        ).andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/confirm_receive_invite")
                .param("inviteId", "30")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche3")
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/reject_receive_invite")
                .param("inviteId", "23")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche3")
        );*/

        /*mockMvc.perform(MockMvcRequestBuilders.get("/confirm_receive_invite")
                .param("inviteId", "7")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche3")
        );*/

        /*MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startDate", "2020-10-16");
        params.add("endDate", "2024-10-18");
        mockMvc.perform(MockMvcRequestBuilders.post("/update-unavailable-date")
                        .params(params)
                        .sessionAttr("loginUser", "minyiche4")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is( "minyiche4 unavailable date range is set")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("200")));*/

        /*MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startDate", "2000-10-16");
        params.add("endDate", "2001-10-18");
        mockMvc.perform(MockMvcRequestBuilders.post("/update-unavailable-date")
                        .params(params)
                        .sessionAttr("loginUser", "minyiche3")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is( "minyiche3 unavailable date range is set")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("200")));*/
        /*MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startDate", "2030-10-16");
        params.add("endDate", "2031-10-18");
        mockMvc.perform(MockMvcRequestBuilders.post("/update-unavailable-date")
                        .params(params)
                        .sessionAttr("loginUser", "root1")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is( "root1 unavailable date range is set")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("200")));*/
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
        params.add("username", "test");
        params.add("password", "123");
        params.add("re_password", "123");
//        params.add("fname","tommy");
//        params.add("lname","trojan");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signup").params(params)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        ModelMap map = mvcResult.getModelAndView().getModelMap();
        Assert.assertEquals("Sign Up Successfully!", map.get("message"));

        Assert.assertEquals(200, status);


        params.set("username", "root");
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signup").params(params)).andReturn();
        status = mvcResult.getResponse().getStatus();

        map = mvcResult.getModelAndView().getModelMap();
        Assert.assertEquals("Username is taken. Try another one.", map.get("error_message"));

        Assert.assertEquals(200, status);

    }

    @Test
    @Transactional
    public void testSignin() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "test");
        params.add("password", "123");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();

//        HttpServletRequest request= mvcResult.getRequest();
//        request.getSession().setAttribute("loginUser","ABC");
//        HttpServletResponse response= mvcResult.getResponse();
//        boolean output= loginInterceptor.preHandle(request,response,null);
//        System.out.println("loginInterceptor= "+output);

        int status = mvcResult.getResponse().getStatus();

        ModelMap map = mvcResult.getModelAndView().getModelMap();
        Assert.assertEquals("Username does not exist!", map.get("error_message"));
        Assert.assertEquals("123", map.get("password"));
        Assert.assertEquals("test", map.get("username"));
        Assert.assertEquals(200, status);


        params.set("username", "root");
        mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andExpect(redirectedUrl("/home")).andExpect(status().isFound());
//        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();
//        status = mvcResult.getResponse().getStatus();
//        map=mvcResult.getModelAndView().getModelMap();
//        Assert.assertEquals("Login successfully",map.get("message"));
//        Assert.assertEquals(200,status);


        params.set("password", "1111");
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();
        status = mvcResult.getResponse().getStatus();
        map = mvcResult.getModelAndView().getModelMap();
        Assert.assertEquals("Username and password do not match!", map.get("error_message"));
        Assert.assertEquals("1111", map.get("password"));
        Assert.assertEquals("root", map.get("username"));
        Assert.assertEquals(200, status);

    }

    @Test
    @Transactional
    public void testSendInvite() throws Exception {
        List<String> receivers = new ArrayList<>();

        receivers.add("minyiche2");

        List<Event> events = new ArrayList<>();
        java.sql.Date eventDate = java.sql.Date.valueOf("2021-10-16");

        Event event1 = new Event();
        event1.setEventName("event1");
        event1.setGenre("event");
        event1.setEventDate(eventDate);
        event1.setLocation("LA");

        Event event2 = new Event();
        event2.setEventName("event2");
        event2.setGenre("event");
        event2.setEventDate(eventDate);
        event2.setLocation("LA");

        Event event3 = new Event();
        event3.setEventName("event3");
        event3.setGenre("event");
        event3.setEventDate(eventDate);
        event3.setLocation("LA");

        events.add(event1);
        events.add(event2);
        events.add(event3);

        InviteModel inviteModel = new InviteModel();
        inviteModel.setSender("minyiche1");
        inviteModel.setInvite_name("invite");
        inviteModel.setReceivers(receivers);
        inviteModel.setEvents(events);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(inviteModel);
        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", "minyiche1")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("you are blocked by minyiche2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("400")));


        receivers = new ArrayList<>();
        receivers.add("minyiche3");
        receivers.add("root");
        receivers.add("root1");

        inviteModel = new InviteModel();
        inviteModel.setSender("minyiche1");
        inviteModel.setInvite_name("invite");
        inviteModel.setReceivers(receivers);
        inviteModel.setEvents(events);

        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        json = ow.writeValueAsString(inviteModel);
        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", "minyiche1")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Invite Sent")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("200")));

        receivers.add("minyiche4");
        inviteModel.setReceivers(receivers);
        System.out.println(receivers);
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        json = ow.writeValueAsString(inviteModel);
        mockMvc.perform(MockMvcRequestBuilders.post("/send-invite")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", "minyiche1")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("minyiche4 can not attend event1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("400")));
    }

    @Test
    @Transactional
    public void testFindUserInvite() throws Exception {

        //MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/find-received-invite")
                .sessionAttr("loginUser", "minyiche4")
        ).andReturn();
        ModelMap map = mvcResult.getModelAndView().getModelMap();
        List<Invite> invites = (List<Invite>) map.get("invites");
        Assert.assertEquals(3, invites.size());
        Assert.assertEquals("Friend Invite", invites.get(0).getInviteName());
        Assert.assertEquals(3, invites.get(0).getInvite_events_list().size());

    }

    @Test
    @Transactional
    public void testReceiveGroupDate() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/receive-groupDates")
                .sessionAttr("loginUser", "minyiche2")
        ).andReturn();
        System.out.println(mvcResult.getResponse().getStatus());
        ModelMap map = mvcResult.getModelAndView().getModelMap();
        System.out.println(map.getAttribute("invites"));
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @Transactional
    public void testReceiveGroupDateEvents() throws Exception {
        MvcResult mvcResult0 = mockMvc.perform(MockMvcRequestBuilders.get("/receive-groupDates")
                .sessionAttr("loginUser", "root")
        ).andReturn();

        ModelMap map0 = mvcResult0.getModelAndView().getModelMap();
        List<Map<String, String>> list = (List<Map<String, String>>) map0.getAttribute("invites");
        System.out.println(list);
        Map<String, String> first_invite = list.get(0);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("inviteId", first_invite.get("inviteId"));
        params.add("status", first_invite.get("status"));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/receive_invite_events").params(params)
                .sessionAttr("loginUser", "root")
        ).andReturn();

        //ModelMap map = mvcResult.getModelAndView().getModelMap();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @Transactional
    public void testUpdateReceiveGroupDateEvents() throws Exception {
        MvcResult mvcResult0 = mockMvc.perform(MockMvcRequestBuilders.get("/receive-groupDates")
                .sessionAttr("loginUser", "root")
        ).andReturn();

        ModelMap map0 = mvcResult0.getModelAndView().getModelMap();
        List<Map<String, String>> list = (List<Map<String, String>>) map0.getAttribute("invites");

        Map<String, String> first_invite = list.get(0);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("inviteId", first_invite.get("inviteId"));
        params.add("status", first_invite.get("status"));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/receive_invite_events").params(params)
                .sessionAttr("loginUser", "root")
        ).andReturn();

        ModelMap map = mvcResult.getModelAndView().getModelMap();
        List<Map<String, String>> events = (List<Map<String, String>>) map.getAttribute("events");


        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();


        params1.add("eventId", events.get(events.size() - 1).get("eventId"));
        params1.add("preference", "5");
        params1.add("availability", "yes");
        mockMvc.perform(MockMvcRequestBuilders.get("/update_receive_invite_events")
                        .params(params1)
                        .sessionAttr("loginUser", "root")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Updated")));

    }

    @Test
    @Transactional
    public void testConfirmReceiveGroupDate() throws Exception {

        MvcResult mvcResult0 = mockMvc.perform(MockMvcRequestBuilders.get("/receive-groupDates")
                .sessionAttr("loginUser", "root")
        ).andReturn();

        ModelMap map0 = mvcResult0.getModelAndView().getModelMap();
        List<Map<String, String>> list = (List<Map<String, String>>) map0.getAttribute("invites");

        String inviteId = "";
        for (Map<String, String> obj : list) {
            if (obj.get("status").equalsIgnoreCase("new")) {
                inviteId = obj.get("inviteId");
                break;
            }
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("inviteId", inviteId);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/confirm_receive_invite").params(params)
                .sessionAttr("loginUser", "root")
        ).andReturn();

        Assert.assertEquals("302", mvcResult.getResponse().getStatus() + "");

        inviteId = "";
        for (Map<String, String> obj : list) {
            if (obj.get("status").equalsIgnoreCase("Rejected")) {
                inviteId = obj.get("inviteId");
                break;
            }
        }
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("inviteId", inviteId);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/confirm_receive_invite").params(params1)
                .sessionAttr("loginUser", "root")
        ).andReturn();

        Assert.assertEquals("302", mvcResult.getResponse().getStatus() + "");

        params1.add("inviteId", "999999");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/confirm_receive_invite").params(params1)
                .sessionAttr("loginUser", "root")
        ).andReturn();
    }


    @Test
    @Transactional
    public void testRejectReceiveGroupDate() throws Exception {
        MvcResult mvcResult0 = mockMvc.perform(MockMvcRequestBuilders.get("/receive-groupDates")
                .sessionAttr("loginUser", "root")
        ).andReturn();

        ModelMap map0 = mvcResult0.getModelAndView().getModelMap();
        List<Map<String, String>> list = (List<Map<String, String>>) map0.getAttribute("invites");

        String inviteId = "";
        for (Map<String, String> obj : list) {
            if (obj.get("status").equalsIgnoreCase("new")) {
                inviteId = obj.get("inviteId");
                break;
            }
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("inviteId", inviteId);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/reject_receive_invite").params(params)
                .sessionAttr("loginUser", "root")
        ).andReturn();

        Assert.assertEquals("302", mvcResult.getResponse().getStatus() + "");

        inviteId = "";
        for (Map<String, String> obj : list) {
            if (obj.get("status").equalsIgnoreCase("Confirmed")) {
                inviteId = obj.get("inviteId");
                break;
            }
        }
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("inviteId", inviteId);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/reject_receive_invite").params(params1)
                .sessionAttr("loginUser", "root")
        ).andReturn();

        Assert.assertEquals("302", mvcResult.getResponse().getStatus() + "");

        params1.add("inviteId", "999999");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/reject_receive_invite").params(params1)
                .sessionAttr("loginUser", "root")
        ).andReturn();

    }

    @Test
    public void testEventSearch() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche3");
        params.add("invite_id", "7");

        mockMvc.perform(MockMvcRequestBuilders.get("/search-event-by-invite-and-username")
                        .params(params)
                        .sessionAttr("loginUser", "minyiche1")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventName", is("Justin Bieber")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventName", is("KaRol G")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].eventName", is("Bad Bunny")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].eventName", is("Knotfest: Los Angeles"))); //this can be changed with regard to data in database
    }

    @Test
    @Transactional
    public void testFinalizeInvite() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/propose-finalize-invite")
                        .param("invite_id", "7")
                        .sessionAttr("loginUser", "minyiche1")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.proposedEvent.eventName", is("Justin Bieber")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.average", is(4.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.median", is(3.0)));

        mockMvc.perform(MockMvcRequestBuilders.post("/propose-finalize-invite")
                        .param("invite_id", "31")
                        .sessionAttr("loginUser", "minyiche1")
                )
                .andExpect(status().isOk());

        //resultActions.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testLogout() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "root");
        params.add("password", "123");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signin").params(params)).andReturn();
        MockHttpSession session = (MockHttpSession) mvcResult.getRequest().getSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/logout").session(session)).andExpect(redirectedUrl("/signin")).andExpect(status().isFound());
    }

    @Test
    public void access_home_without_login() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/home")).andReturn();
        String value = (String) mvcResult.getRequest().getAttribute("error_message");

        Assert.assertEquals("You need to log in first!", value);
    }

    @Test
    @Transactional
    void testAddBlockedUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche2");
        params.add("block", "minyiche1");
        mockMvc.perform(MockMvcRequestBuilders.post("/add-blocked-user")
                        .params(params)
                        .sessionAttr("loginUser", "minyiche1")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("minyiche1 is already on your blocked list")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("400")));

        params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche3");
        params.add("block", "minyiche1");
        mockMvc.perform(MockMvcRequestBuilders.post("/add-blocked-user")
                        .params(params)
                        .sessionAttr("loginUser", "minyiche1")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("added to blocklist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("200")));

    }

    @Test
    public void testUsernameStartingWith() throws Exception {
        String json = "{\"name\":\"r\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/usernameStartingWith")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();
        String response_json = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> map = mapper.readValue(response_json, Map.class);
        List<String> names = map.get("names");
        Boolean found = true;
        for (String name : names) {
            if (name.substring(0).equalsIgnoreCase("r")) {
                found = false;
                break;
            }
        }

        Assert.assertTrue(found);

    }


    @Test
    @Transactional
    public void testReplyInvite() throws Exception {
        //prepare reply event
        List<Event> events = new ArrayList<>();

        Invite invite = new Invite();
        invite.setId(7L);

        Event event1 = new Event();
        event1.setId(9L);
        event1.setAvailability("yes");
        event1.setPreference(2);

        Event event2 = new Event();
        event2.setId(10L);
        event2.setAvailability("maybe");
        event2.setPreference(2);


        events.add(event1);
        events.add(event2);

        invite.setInvite_events_list(events);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(invite);

        mockMvc.perform(MockMvcRequestBuilders.post("/reply-invite")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", "minyiche1")
                ).
                andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Reply Sent")));

    }

    @Test
    @Transactional
    public void testUpdateUserDateRange() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startDate", "2021-10-16");

        params.add("endDate", "2021-10-18");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/update-unavailable-date")
                .params(params)
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();

        Assert.assertEquals(302, mvcResult.getResponse().getStatus()); // 302 redirection http code
    }

    @Test
    @Transactional
    public void testRemoveUserDateRange() throws Exception {


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/remove-unavailable-date")

                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();

        Assert.assertEquals(302, mvcResult.getResponse().getStatus()); // 302 redirection http code

    }

    @Test
    @Transactional
    public void testGetBlockedUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "minyiche2");
//        mockMvc.perform(MockMvcRequestBuilders.get("/get-blocked-user")
//                .params(params)
//                .sessionAttr("loginUser", "minyiche1")
//        ).andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", is("minyiche1")));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/get-blocked-user")
                .params(params)
                .sessionAttr("loginUser", "minyiche1")).andReturn();

        String response_json = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> map = mapper.readValue(response_json, Map.class);
        List<String> names = map.get("blocked_usernames");

        String target = "minyiche1";
        Boolean found = false;
        for (String name : names) {
            if (name.equals(target)) {
                found = true;
            }

        }
        Assert.assertTrue(found);
    }

    @Test
    @Transactional
    public void testDeleteBlockedUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("blocked", "minyiche3");
        mockMvc.perform(MockMvcRequestBuilders.post("/delete-blocked-user")
                        .params(params)
                        .sessionAttr("loginUser", "minyiche4")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("User unblocked")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnCode", is("200")));
    }

    @Test
    @Transactional
    public void testFindSentInvite() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/list-sent-invite")
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();
        ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
        List<Invite> invites = (List<Invite>) modelMap.getAttribute("invites");
        Assert.assertEquals(4, invites.size());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.invites[0].inviteName", is("Music Invite")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.invites[1].inviteName", is("Friend Invite")));
    }

    @Test
    @Transactional
    public void testSearchEvent() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("city", "Los Angeles");
        params.add("genre", "Music");
        params.add("keyword", "Justin");
        params.add("startDate", "2021-11-17");
        params.add("endDate", "2022-03-08");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/search-ticketmaster-event")
                .params(params)
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();
        ModelMap modelMap = mvcResult.getModelAndView().getModelMap();

        JsonNode eventNode = new ObjectMapper().readTree((String) modelMap.getAttribute("events"));
        //System.out.println((String)modelMap.getAttribute("events"));
        Assert.assertEquals(1, eventNode.get("_embedded").get("events").size());
        Assert.assertEquals("Justin Bieber", eventNode.get("_embedded").get("events").get(0).get("name").asText());
    }

    @Test
    @Transactional

    public void testSetting() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/setting")

                .sessionAttr("loginUser", "root")
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/setting")

                .sessionAttr("loginUser", "root1")
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @Transactional
    public void testFindSentInviteEvent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/list-sent-invite-event")
                .param("inviteId", "7")
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();
        ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        Invite invite = (Invite) modelMap.getAttribute("invite");
        Map<Event, String> eventsMap = (Map<Event, String>) modelMap.getAttribute("eventsReceivers");
        List<User> receivers = (List<User>) modelMap.getAttribute("receivers");
        List<Event> events = (List<Event>) modelMap.getAttribute("events");
        Assert.assertEquals(8, eventsMap.size());
        Assert.assertEquals(2, receivers.size());
        Assert.assertEquals(4, events.size());
        Assert.assertEquals("Music Invite", invite.getInviteName());

    }

    @Test
    @Transactional
    public void testDeleteSentInviteEvent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete-sent-invite-event")
                .param("inviteId", "7")
                .param("eventId", "8")
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();
        ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(302, status);
        List<Event> events = inviteRepository.findById(7L).get().getInvite_events_list();
        /*for(Event event : events){
            Assert.assertNotEquals(new Long(8L),event.getId());
        }*/
    }

    @Test
    @Transactional
    public void testDeleteSentInviteUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete-sent-invite-user")
                .param("inviteId", "7")
                .param("username", "minyiche2")
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();
        ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(302, status);
        List<User> receivers = inviteRepository.getById(7L).getReceivers();
        /*for(User receiver : receivers){
            Assert.assertNotEquals("minyiche2", receiver.getUsername());
        }*/
    }

    @Test
    @Transactional

    public void testRemove_receive_invites_lists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/remove_receive_invites_lists")

                .sessionAttr("loginUser", "root")
        ).andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testDeleteSentInvite() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete-sent-invite")
                .param("inviteId", "7")
                .sessionAttr("loginUser", "minyiche1")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(302, status);
    }

}