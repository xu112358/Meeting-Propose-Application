package csci310.config;

import csci310.filter.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        registry.addInterceptor(new LoginInterceptor())
                // 拦截路劲
                .addPathPatterns("/**")
                // 排除路径
                .excludePathPatterns("/signin","/","/signup","/**/*.css",
                        "/**/*.js","/usernameStartingWith");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/proposeEvent").setViewName("proposeEvent");
//        registry.addViewController("/setting").setViewName("setting");
        registry.addViewController("/messages").setViewName("messages");
        registry.addViewController("/listProposals").setViewName("listProposals");
        registry.addViewController("/sent_groupDate").setViewName("sent_groupDate");

    }

}
