package com.pley.login;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.Cookies;

import com.pley.user.User;

/**
 * Servlet Filter implementation class IsAuthenticatedFilter
 * AUTHOR: Trever Pietsch
 * DATE: July 19 2014
 */
@WebFilter("/IsAuthenticatedFilter")
public class IsAuthenticatedFilter implements Filter {
	
	 private ServletContext context;
     
	    public void init(FilterConfig fConfig) throws ServletException {
	        this.context = fConfig.getServletContext();
	        this.context.log("AuthenticationFilter initialized");
	    }
	     
    /**
     * Default constructor. 
     */
    public IsAuthenticatedFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
         
        String uri = req.getRequestURI();
        //this.context.log("Requested Resource::"+uri);
         
        HttpSession session = req.getSession(false);
        Object user = (session != null) ? session.getAttribute("user") : null;

        if (user != null) {
        	System.out.println("user been set");
            try {
            	System.out.println("trever");
				chain.doFilter(request, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
        	RequestDispatcher rd= request.getRequestDispatcher("/login.html");
            try {
            	System.out.println("forwarding");
				rd.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	

}
