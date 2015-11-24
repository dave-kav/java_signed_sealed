package sealed;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

@SuppressWarnings("serial")
public class SealedFactoryImpl extends UnicastRemoteObject implements SealedFactory {
	private String name;
	private int v = 0;
	private IVehicle myVehicle;
	private Notify n = null;
	private Random random = new Random();
	private PrivateKey privateKey = null;
	private PublicKey publicKey = null;
	private Signature signature = null;
	private SignedObject signedObject = null;
	private SealedObject sealedObject = null;
	private KeyPair keyPair = null;
	private Key key = null;
	private Cipher cipher;
	
	protected SealedFactoryImpl(String name) throws RemoteException {
		this.name = name;
		try {
			keyPair = generateKeyPair("DSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();
	}
	
	public void build(String model, Notify n) throws RemoteException {
		this.n = n;
		if (model.equalsIgnoreCase("car")) {
			System.out.printf("\n\nProduction under way, please stand by...\n\n");
			try {
				Thread.sleep(random.nextInt(1000) + 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myVehicle = new Car();
			if (myVehicle != null) {
				v++;	
				notifyDone();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.printf("Prodcution Completed!!\n----------------------------\nModel:\t\t" + myVehicle.getModel()  
				+ "\nRegistration:\t152 - C - %d\n----------------------------\n", v);
			}
		}
		else if (model.equalsIgnoreCase("van")) {
			System.out.printf("\n\nProduction under way, please stand by...\n\n");
			try {
				Thread.sleep(random.nextInt(10000) + 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myVehicle = new Van();
			if (myVehicle != null) {
				v++;
				notifyDone();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.printf("Production Completed!!\n----------------------------\nModel:\t\t" + myVehicle.getModel()  
				+ "\nRegistration:\t152 - C - %d\n----------------------------\n", v);
			}
		}
		else 
			System.out.printf("Invalid option.");
	}
		
	public SealedObject getVehicle() throws RemoteException {
		IVehicle temp;
		if (myVehicle != null) {
			temp = myVehicle;
			myVehicle = null;
			signedObject = signObject((Serializable) temp);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sealedObject = sealObject(signedObject);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Sealed");
			return sealedObject;
		}
		return null;
	}
	
	public IVehicle ready() throws RemoteException {
		return myVehicle;
	}

	@Override
	public void notifyDone() throws RemoteException{
		n.doneBuild();
	}
	
	public KeyPair generateKeyPair(String stringAlgorithm) throws NoSuchAlgorithmException {
	    // get keypair instance   init it with 1024 and securerandom object
	    // lastly call generatekeypair method
	    KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance(stringAlgorithm);
	    keypairgenerator.initialize(1024, new SecureRandom());
	    return keypairgenerator.generateKeyPair();
	  }
	
	public SignedObject signObject(Serializable object) {
		SignedObject s = null;
		try {
			System.out.println("Getting signature..");
			signature = Signature.getInstance("DSA");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Couldn't acquire signature");
			e.printStackTrace();
		}
		try {
			System.out.println("Signing object..");
			s = new SignedObject(object, privateKey, signature);
		} catch (InvalidKeyException | SignatureException | IOException e) {
			System.out.println("Couldn't sign object");
			e.printStackTrace();
		}
		System.out.println("Object Signed..");
		return s;
	}
	
	public SealedObject sealObject(Serializable object) {
		try {
			System.out.println("Acquiring key..");
			key = n.getKey();
		} catch (RemoteException e1) {
			System.out.println("Couldn't acquire key");
			//e1.printStackTrace();
		}
		SealedObject s = null;
		try {
			System.out.println("Getting cipher instance..");
			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.out.println("Couldn't get cipher instance");
			e.printStackTrace();
		}
		try {
			System.out.println("Init cipher..");
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			System.out.println("Couldn't init cipher");
			e.printStackTrace();
		}
		try {
			System.out.println("Sealing object..");
			s = new SealedObject(object, cipher);
		} catch (IllegalBlockSizeException | IOException e) {
			System.out.println("Couldn't seal object");
			e.printStackTrace();
		}
		return s;
	}
}
