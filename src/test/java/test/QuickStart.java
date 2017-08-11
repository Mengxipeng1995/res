package test;
import org.eclipse.jetty.server.Server;
import org.springside.modules.test.jetty.JettyFactory;
import org.springside.modules.test.spring.Profiles;
public class QuickStart {
	public static final int PORT = 8888;
    public static final String CONTEXT = "/res";
    public static final String BASE_URL = "http://localhost:"+PORT+CONTEXT;
    public static final String[] TLD_JAR_NAMES = new String[] { "spring-webmvc"};
	public static void main(String[] args) {
		
		
		Profiles.setProfileAsSystemProperty(Profiles.DEVELOPMENT);
		
		Server server = JettyFactory.createServerInSource(PORT, CONTEXT);
		
		JettyFactory.setTldJarNames(server, TLD_JAR_NAMES);
		try{
			 server.start();
			 System.out.println("Server running at " + BASE_URL);
				System.out.println("Hit Enter to reload the application");
			 
			 while (true) {
                 char c = (char) System.in.read();
                 if (c == '\n') {
                     JettyFactory.reloadContext(server);
                 }
             }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
