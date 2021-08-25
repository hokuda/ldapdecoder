package test;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LdapsWebListener implements ServletContextListener {

  public void contextDestroyed(ServletContextEvent sce) {
  }

  public void contextInitialized(ServletContextEvent sce) {
      try {
          LdapsClient client = new LdapsClient();
          client.doLdaps();
      }
      catch(Exception e) {
          e.printStackTrace();
      }
      
      System.out.println(this.getClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName() + " done~");
  }
}
