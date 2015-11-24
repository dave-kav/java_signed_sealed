package sealed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Key;

public interface Notify extends Remote {
	public void doneBuild() throws RemoteException;
	
	public Key getKey() throws RemoteException;
}
