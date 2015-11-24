package sealed;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

@SuppressWarnings("serial")
public class CheckDone extends UnicastRemoteObject implements Notify {

	private SealedFactory factory;
	private IVehicle vehicle;
	private Key key;
	
	public CheckDone(SealedFactory f, Key k) throws RemoteException {
		factory = f;
		key = k;
	}

	@Override
	public void doneBuild()  {
		try {
			vehicle = factory.ready();
			if (vehicle != null)
				System.out.println("\nProduction nearly completed...");
			else
				System.out.println("\nProdction error!");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Key getKey() {
		return key;
	}
}
