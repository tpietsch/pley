package com.pley.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class CreateUserAccountServlet
 * AUTHOR: Trever Pietsch
 * DATE: July 19 2014
 */
@WebServlet("/Create")
public class CreateUserAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUserAccountServlet() {
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
		
		try {
			
			User newUser = new User(new JSONObject(s));
			
			if(newUser.save()){
				
				return;
			}else{
				
				//rollback user
				//response.setContentType("application/json");
				System.out.println("user save error");
				response.sendError(415);
				response.getWriter().write(newUser.getMessage());
				
				return;
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("jsaon parse aerror");
			response.sendError(415);
			e.printStackTrace();
			return;
		}
		
	
	
		
		
		
	}

}
