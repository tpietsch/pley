package com.pley.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pley.user.User;

/**
 * Servlet implementation class Login
 * AUTHOR: Trever Pietsch
 * DATE: July 19 2014
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletInputStream stream = request.getInputStream();
		
    	String s = this.getBody(request);
    	String user = "";
	    String pwd = "";
    	
    	try {
			JSONObject json = new JSONObject(s);
			user = json.getString("email");
			pwd = json.getString("password");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	BCryptPasswordEncoder enc = new BCryptPasswordEncoder(10); 
    	
  	  try { 
  		  
  	        
        	Class.forName("com.mysql.jdbc.Driver"); 
        	System.out.println(request.getSession().getId());
        	java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pley", "root", ""); 
        	
        	String query = "SELECT * FROM user where email = '"+user+"'";
      	
        	System.out.println(query);
        	
        	java.sql.Statement st = con.createStatement();
            
           // execute the query, and get a java resultset
           ResultSet rs = st.executeQuery(query);
            
           // iterate through the java resultset
           
           while (rs.next())
           {
          	
          	 String pass = rs.getString("password");
          	 if(enc.matches(pwd,pass)){
          		 System.out.println("logging in");
          		 	HttpSession session = request.getSession();
                   session.setAttribute("user",rs.getString("firstname"));
                   //setting session to expiry in 30 mins
                   session.setMaxInactiveInterval(30*60);
                   Cookie userName = new Cookie("user", user);
                   userName.setMaxAge(30*60);
                   response.addCookie(userName);
                   rs.close();
                   request.setAttribute("name", session);
                   
                  User u = (User)User.findByEmail(user);
                   session.setAttribute("user", u);
                   response.setContentType("text/html");
           		
           		PrintWriter writer = response.getWriter();
           		writer.write("<p> Thanks for using pley "+u.getFirstname()+" "+u.getLastname());
           		writer.write("<br/>Check out our Welcome Login Success Page at");
           		writer.write("<br/><a href='/pley/filter/LoginSuccess'>Success</a>");
           		writer.write("<br/><a href='/pley/LogoutServlet'>Logout</a>");
                   return;
          	 }
          
               
           }
           
           response.setContentType("text/plain");
           response.setStatus(415);
           response.getWriter().write("Incorrect Login Info");
           rs.close();
      
  	  } 
    	catch (Exception ex) { 
    	ex.printStackTrace(); 
    	}
  }
	}


