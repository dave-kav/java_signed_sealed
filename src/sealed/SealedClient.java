package sealed;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

public class SealedClient {

	public static void main(String argv[]) {
		PublicKey publicKey = null;
		PrivateKey privateKey = null;
		KeyPair keyPair = null;
		SealedFactory factory = null;
		SealedObject sealedObject = null;
		SignedObject signedObject = null;
		Cipher cipher = null;
		IVehicle iv = null;
		CheckDone check;
		Signature signature = null;
		Key key = null;
		try {
			key = KeyGenerator.getInstance("DES").generateKey();
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			keyPair = generateKeyPair("DSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();

		String model = Validate.readString("\nWhich vehicle do you wish to order?\n\t-\tCar\n\t-\tVan\n\n\t\t");
		try  {
			/*get reference to server from
	                  server’s registry*/
			factory = (SealedFactory) Naming.lookup("rmi://" + argv[0] + "/SealedServer");
			check = new CheckDone(factory, key);
			factory.build(model, check);
		}catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			System.out.println("Getting sealed object..");
			sealedObject = factory.getVehicle();
		} catch (RemoteException e) {
			System.out.println("Couldn't get sealed object..");
			//e.printStackTrace();
		}
		try {
			System.out.println("Getting cipher instance..");
			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
			System.out.println("Couldn't get cipher instance..");
			//e1.printStackTrace();
		}
		try {
			System.out.println("Init cipher ..");
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException e1) {
			System.out.println("Couldn't init cipher..");
			e1.printStackTrace();
		}
		try {
			System.out.println("Unsealing object..");
			signedObject = (SignedObject) sealedObject.getObject(cipher);
		} catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException | IOException e1) {
			System.out.println("Couldn't unseal object..");
			//e1.printStackTrace();
		}
		try {
			System.out.println("Getting signature instance..");
			signature = Signature.getInstance("DSA");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Couldn't  get signature instance..");
			e.printStackTrace();
		}
		try {
			System.out.println("Verifying signed object..");
			signedObject.verify(publicKey, signature);
		} catch (InvalidKeyException | SignatureException e) {
			System.out.println("Couldn't verify signed object..");
			e.printStackTrace();
		}
		try {
			System.out.println("Unsign object..");
			iv = (IVehicle)signedObject.getObject();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Couldn't unsign object..");
			//e.printStackTrace();
		}
		if (iv != null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.printf("\nCompleted production of model: %s\n", iv.getModel());
		}
		
		else
			System.out.println("\nInvalid choice!!\n");
		System.exit(0);
		}
	
	
	public static KeyPair generateKeyPair(String stringAlgorithm) throws NoSuchAlgorithmException {
	    // get keypair instance   init it with 1024 and securerandom object
	    // lastly call generatekeypair method
	    KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance(stringAlgorithm);
	    keypairgenerator.initialize(1024, new SecureRandom());
	    return keypairgenerator.generateKeyPair();
	  }
}
