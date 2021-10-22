package csci310.filter;

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
public class LoginInterceptorTest {
    @Autowired
    private ApplicationContext appContext;
    @Test
    public void testPreHandle() {
        Object obj=appContext.getBean(LoginInterceptor.class);

        Assert.assertTrue(obj!=null); // test that the spring Ioc successfully register the loginIntercepter
    }
}