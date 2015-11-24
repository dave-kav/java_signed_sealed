package sealed;

import java.io.Serializable;

public class Car implements IVehicle, Serializable {
	String model = "Car";
	
	public Car() {}
	
	public String getModel() {  return model; }	
}
