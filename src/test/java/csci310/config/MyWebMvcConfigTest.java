package csci310.config;

import csci310.filter.LoginInterceptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MyWebMvcConfigTest {
    @Autowired
    private ApplicationContext appContext;
    @Test
    public void testAddInterceptors() {
        Object obj=appContext.getBean(LoginInterceptor.class);
        System.out.println(obj);
        Assert.assertTrue(obj!=null); // test that the spring Ioc successfully register the loginIntercepter
    }

    @Test
    public void testAddViewControllers() {
        Object obj= appContext.getBean("viewControllerHandlerMapping");


        Assert.assertTrue(obj!=null); // test that the spring Ioc successfully register the viewControllerHandlerMapping
    }
}
