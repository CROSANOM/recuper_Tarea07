package alquilerVehiculos.aplicacion;



import alquilerVehiculos.modelo.AlquilerVehiculos;
import alquilerVehiculos.modelo.IModeloAlquilerVehiculos;
import alquilerVehiculos.mvc.controlador.ControladorAlquilerVehiculos;
import alquilerVehiculos.mvc.controlador.IControladorAlquilerVehiculo;
import alquilerVehiculos.mvc.vista.IUTextual;
import alquilerVehiculos.mvc.vista.IUTextualVista;

// Accedo a las clases que controlan la aplicación incluida la consola no se instancian sus metodos Staticos
public class PrincipalAlquilerVehiculos {

	// metodo main 
	
	/**
	 * @param args llama a Interfaz y al metodo comenzar
	 */
	public static void main(String[] args) {
		IModeloAlquilerVehiculos modelo = new AlquilerVehiculos();
		IUTextualVista vista = new IUTextual();
		IControladorAlquilerVehiculo controlador = new ControladorAlquilerVehiculos(modelo, vista);

		controlador.comenzar();
		
		
		
		
	}

}
