// Import required java libraries
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
@WebServlet("/Login")
public class Login extends HttpServlet {

   public void init() throws ServletException {
       
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
       String dbURL1 = "jdbc:derby://localhost/CodeFire/MyDb;create=true";
       try{
           DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
           Connection conn1 = DriverManager.getConnection(dbURL1);
       if (conn1 != null) {
           System.out.println("Connected to database #1");
       }
       }
       catch(SQLException e){
           System.out.println(e);
       }
      // Set response content type
      response.setContentType("text/html");
     String uname=request.getParameter("username");
      // Actual logic goes here.
      PrintWriter out = response.getWriter();
      out.println("<h1> Hello  " + uname + " . . .</h1>");
   }

   public void destroy() {
      // do nothing.
   }
}
