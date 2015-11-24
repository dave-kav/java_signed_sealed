package sealed;

import java.io.Serializable;

public class Van implements IVehicle, Serializable {
	String model = "Van";
	
	public Van() {};
	
	public String getModel() {  return model; }
}
