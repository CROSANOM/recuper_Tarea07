package alquilerVehiculos.mvc.modelo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import alquilerVehiculos.mvc.modelo.dominio.Alquiler;
import alquilerVehiculos.mvc.modelo.dominio.Cliente;
import alquilerVehiculos.mvc.modelo.dominio.ExcepcionAlquilerVehiculos;
import alquilerVehiculos.mvc.modelo.dominio.vehiculo.Vehiculo;

public class Alquileres {

	// atributos

	private final int MAX_ALQUILERES = 5;
	private Alquiler[] alquileres;
	// declarar constante guarda la ruta relativa del fichero
	private final String FICHERO_ALQUILERES = "datos/alquileres.dat";

	// constructor
	public Alquileres() {
		alquileres = new Alquiler[MAX_ALQUILERES];
	}

	// get alquiler

	/**
	 * @return the alquileres
	 */
	public Alquiler[] getAlquileres() {
		return alquileres.clone();
	}

	/**
	 * @return the mAX_ALQUILERES
	 */
	public int getMAX_ALQUILERES() {
		return MAX_ALQUILERES;
	}

	// metodos de lectura y escritura de ficheros

	// metodo de lectura

	public void leerAlquileres() {
		// 1 objeto fichero ( Ruta )

		File fichero = new File(FICHERO_ALQUILERES);

		ObjectInputStream entrada;
		try {
			entrada = new ObjectInputStream(new FileInputStream(fichero));
			try {
				alquileres = (Alquiler[]) entrada.readObject();
				entrada.close();
				System.out.println("Fichero alquileres lei�do satisfactoriamente.");
			} catch (ClassNotFoundException e) {
				System.out.println("No puedo encontrar la clase que tengo que leer.");
			} catch (IOException e) {
				System.out.println("Error inesperado de Entrada/Salida.");
			}
		} catch (IOException e) {
			System.out.println("No puedo abrir el fichero de alquileres.");
		}
	}

	public void escribirAlquileres() {
		File fichero = new File(FICHERO_ALQUILERES);
		try {
			ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero));
			salida.writeObject((Alquiler[]) alquileres);
			salida.close();
			System.out.println("Fichero alquileres escrito satisfactoriamente");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de alquileres");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida");
		}
	}

	// abrir alquiler (buscarIndiceLibre, indiceNoSupera)
	/**
	 * @param cliente
	 * @param vehiculo
	 */

	public void abrirAlquiler(Cliente cliente, Vehiculo vehiculo) {
		int indice = buscarPrimerIndiceLibreComprobandoExistencia(vehiculo);// indiceConAlquiler
		if (indiceNoSuperaTamano(indice)) {
			alquileres[indice] = new Alquiler(cliente, vehiculo);
		} else {
			throw new ExcepcionAlquilerVehiculos("El array esta lleno");
		}
	}

	// Metodo buscarPrimerIndiceLibreComprobandoExistencia (noSuperaTamano)

	/**
	 * @param vehiculo
	 * @return indice
	 */
	private int buscarPrimerIndiceLibreComprobandoExistencia(Vehiculo vehiculo) {
		int indice = 0;
		boolean encontrado = false;
		while (indiceNoSuperaTamano(indice) && !encontrado) {
			if (alquileres[indice] == null) {
				encontrado = true;
			} else if (alquileres[indice].getVehiculo().getMatricula().equals(vehiculo.getMatricula())
					&& !alquileres[indice].getVehiculo().getDisponible()) {
				throw new ExcepcionAlquilerVehiculos("El turismo no esta disponible, ya esta alquilado");
			} else {
				indice++; // pasar a la siguiente posicion
			}
		}
		return indice;
	}

	// metodo indiceNoSuperaTama�o(int indice)
	/**
	 * @param indice
	 * @return boolean
	 */
	private boolean indiceNoSuperaTamano(int indice) {
		return indice < alquileres.length;
	}

	// cerrar alquiler pasandole solo un vehiculo (buscarIndiceAlquilerAbierto ,
	// indiceNoSuperaTamano)

	/**
	 * @param vehiculo
	 */
	public void cerrarAlquiler(Vehiculo vehiculo) {
		int indice = buscarAlquilerAbierto(vehiculo);
		if (indiceNoSuperaTamano(indice))
			alquileres[indice].cerrar();
		else
			throw new ExcepcionAlquilerVehiculos("No hay ningun alquiler Abierto");
	}

	// private int buscarAlquilerAbierto(indiceNoSuperaTama�o)

	/**
	 * @param vehiculo
	 * @return
	 */

	private int buscarAlquilerAbierto(Vehiculo vehiculo) {
		int indice = 0;
		boolean encontradoAlquiler = false;
		while (indiceNoSuperaTamano(indice) && !encontradoAlquiler) {
			if (alquileres[indice].getVehiculo().getMatricula().equals(vehiculo.getMatricula())
					&& alquileres[indice] != null && alquileres[indice].getDias() == 0)
				encontradoAlquiler = true;
			else
				indice++;
		}
		return encontradoAlquiler ? indice : alquileres.length;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Alquileres [MAX_ALQUILERES=" + MAX_ALQUILERES + ", alquileres=" + Arrays.toString(alquileres) + "]";
	}

}
