package csci310.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        Object obj=request.getSession().getAttribute("loginUser");

        if(obj==null){

            request.setAttribute("error_message","You need to log in first!");
            request.getRequestDispatcher("/").forward(request,response);

            return false;
        }
        else{

            return true;
        }



    }



}
