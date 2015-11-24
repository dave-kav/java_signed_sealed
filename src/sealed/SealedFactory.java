package sealed;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Key;

import javax.crypto.SealedObject;

public interface SealedFactory extends Remote {
	public void build(String model, Notify n) throws RemoteException;
	
	public void notifyDone() throws RemoteException;

	public SealedObject getVehicle() throws RemoteException;
	
	public IVehicle ready()  throws RemoteException;

}
