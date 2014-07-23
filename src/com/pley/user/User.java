package com.pley.user;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class User {
	
	private String firstname;
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String lastname;
	private String email;
	private String password;
	private long id;
	private String message = "";
	
	public User(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		try {
			this.firstname = jsonObject.getString("firstname");
			this.lastname = jsonObject.getString("lastname");
			this.email = jsonObject.getString("email");
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);		
			this.password = encoder.encode(jsonObject.getString("password"));	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public long getId(){
		return id;
	}

	public boolean save() {
		// TODO Auto-generated method stub
		try {
		java.sql.Connection conn = this.getCon();
		java.sql.Statement statement = null;

			statement = conn.createStatement();

			String sql = "SELECT COUNT(1) FROM user where email='"+this.email+"'";
			ResultSet rss = statement.executeQuery(sql);
			rss.next();
			int rowCount = rss.getInt(1);
			System.out.println(rowCount);
			System.out.println(sql);
			
			if(rowCount > 0){
				System.out.println("row count is too big");
				this.message = "{ message : This User Already Exists! }";
				return false;
			}
			statement.executeUpdate("INSERT INTO User (firstname,lastname,email,password)" + "VALUES ('"+this.firstname+"', '"+this.lastname+"', '"+this.email+"', '"+this.password+"')", Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = statement.getGeneratedKeys();
			while(rs.next()){
				this.id = rs.getInt(1);
			}
			
		
			System.out.println(this.id);
			
			conn.close();
			return true;
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}
		System.out.println("in this loc");
		return false;
	}
	
	private java.sql.Connection getCon(){
		
		String host = "jdbc:mysql://localhost:3306/pley";
		String username = "root";
		String password = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			java.sql.Connection con = DriverManager.getConnection(host, username, password);
			return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
		
	}
	
private static java.sql.Connection getConn(){
		
		String host = "jdbc:mysql://localhost:3306/pley";
		String username = "root";
		String password = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			java.sql.Connection con = DriverManager.getConnection(host, username, password);
			return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
		
	}
	
	
	public String getMessage() {
		// TODO Auto-generated method stub
		
		return message;
	}

	public static Object findByEmail(String user) {
		// TODO Auto-generated method stub
		java.sql.Connection conn = User.getConn();
		java.sql.Statement statement = null;

			try {
				statement = conn.createStatement();
			
			String sql = "SELECT * FROM user where email='"+user+"'";
			ResultSet rss = statement.executeQuery(sql);
			rss.next();
			
			JSONObject json = new JSONObject();
			
			json.put("id",rss.getInt("id"));
			json.put("lastname", rss.getString("lastname"));
			json.put("firstname",  rss.getString("firstname"));
			json.put("email", rss.getString("email"));
			json.put("password",  rss.getString("password"));
			
			User loadedUser = new User(json);
			return loadedUser;
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return null;
	}

}
