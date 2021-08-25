package test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
@WebServlet("/do")
public class LdapsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LdapsClient client = new LdapsClient();
            client.doLdaps();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        response.setContentType("text/plain");
        response.getWriter().println(this.getClass().getName() + " done~");
        response.getWriter().close();
    }
}
