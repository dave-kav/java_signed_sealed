package sealed;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class SealedServer {
	public static void main( String argv[]) {

	      System.out.println("Application Started");      
	      System.setSecurityManager(new RMISecurityManager());
	      
	      try {
	         SealedFactoryImpl obj = new SealedFactoryImpl("Sealed Object Server");
	         Naming.rebind("rmi://" +argv[0] + "/SealedServer",obj);
	         System.out.println("Server in Registry");
	         }catch (Exception e)  {
	             System.out.println ("Vehicle Server error: " +
	                    e.getMessage());
	         }
	   }
}
