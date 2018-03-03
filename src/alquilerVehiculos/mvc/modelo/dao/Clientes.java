package alquilerVehiculos.mvc.modelo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import alquilerVehiculos.mvc.modelo.dominio.Cliente;
import alquilerVehiculos.mvc.modelo.dominio.ExcepcionAlquilerVehiculos;

public class Clientes {

	private Cliente[] clientes;
	private final int MAX_CLIENTES = 5;

	// atributo ruta del fichero

	private final String FICHERO_CLIENTES = "/datos/clientes.dat";

	// constructor

	public Clientes() {
		clientes = new Cliente[MAX_CLIENTES];
	}

	// getClientes ( se usa return de obtenerClientes )

	/**
	 * @return Clientes( copias )
	 */
	public Cliente[] getClientes() {
		return clientes.clone();
	}

	// Metodos de lectura - escritura de fichero

	public void leerClientes() {

		// 1º crear fichero usando la constante ruta

		File fichero = new File(FICHERO_CLIENTES);

		// 2º crear Objeto de la clase ObjectInputStrean

		ObjectInputStream entrada;

		try {

			entrada = new ObjectInputStream(new FileInputStream(fichero));

			try {
				clientes = (Cliente[]) entrada.readObject();
				entrada.close();
				System.out.println("fichero leido correctamente");
				Cliente.aumentarUltimoIdentificador(calcularUltimoIdentificador());

			} catch (ClassNotFoundException e) {

				System.out.println("No puedo encontrar la clase ");

			} catch (IOException e) {

				System.out.println("Error inesperado de Entrada/Salida");
			}

		} catch (IOException e) {

			System.out.println("No puede abrir el fichero de clientes");

		}

	}

	private Object calcularUltimoIdentificador() {
		int ultimoIdentificador = 0;
		int i = 0;
		while (clientes[i] != null) {
			if (clientes[i].getIdentificador() > ultimoIdentificador)
				ultimoIdentificador = clientes[i].getIdentificador();
		}
		return ultimoIdentificador;
	}

	// escribirClientes

	public void escribirClientes() {
		File fichero = new File(FICHERO_CLIENTES);
		try {
			ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero));
			salida.writeObject((Cliente[]) clientes);
			salida.close();
			System.out.println("Fichero clientes escrito satisfactoriamente.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de clientes");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida");
		}
	}

	/*
	 * anadir Clientes (BuscarIndiceLibreComprobandoExistencia,
	 * indiceNoSuperaTamano)
	 */

	/**
	 * @param cliente
	 */

	public void anadirCliente(Cliente cliente) {
		int indice = buscarPrimerIndiceLibreComprobandoExistencia(cliente);
		if (indiceNoSuperaTamano(indice))
			clientes[indice] = new Cliente(cliente);
		else
			throw new ExcepcionAlquilerVehiculos("El array de clientes esta lleno.");
	}

	// metodo buscarPrimerIndiceLibreComprobandoExistencia
	/**
	 * @param cliente
	 * @return indice
	 */
	private int buscarPrimerIndiceLibreComprobandoExistencia(Cliente cliente) {
		int indice = 0;
		boolean clienteEncontrado = false;
		while (indiceNoSuperaTamano(indice) && !clienteEncontrado) {
			if (clientes[indice] == null)
				clienteEncontrado = true;
			else if (clientes[indice].getDni().equals(cliente.getDni()))
				throw new ExcepcionAlquilerVehiculos("Ya existe un cliente con ese DNI");
			else
				indice++;
		}
		return indice;
	}

	// metodo indiceNoSuperaTamano

	/**
	 * @param indice
	 * @return boolean
	 */
	private boolean indiceNoSuperaTamano(int indice) {
		return indice < clientes.length;
	}

	/*
	 * metodo Borrar Cliente (buscarIndice,
	 * 
	 * indiceNoSuperaTamano,desplazarUnaPosiciónHaciaIzquierda)
	 */

	/**
	 * @param dni
	 */
	public void borrarCliente(String dni) {
		int indice = buscarIndiceCliente(dni);
		if (indiceNoSuperaTamano(indice)) {
			desplazarUnaPosicionHaciaIzquierda(indice);
		} else {
			throw new ExcepcionAlquilerVehiculos("El cliente a borrar no existe");
		}
	}

	// metodo buscarIndiceCliente (indiceNoSuperaTamano)
	/**
	 * @param dni
	 * @return indice
	 */

	private int buscarIndiceCliente(String dni) {
		int indice = 0;
		boolean clienteEncontrado = false;
		while (indiceNoSuperaTamano(indice) && !clienteEncontrado && clientes[indice] != null) {
			if (clientes[indice].getDni().equals(dni))
				clienteEncontrado = true;
			else
				indice++;
		}
		return clienteEncontrado ? indice : clientes.length;
	}

	// metodo desplazarUnaPosicionHaciaIzquierda
	/**
	 * @param indice
	 */
	private void desplazarUnaPosicionHaciaIzquierda(int indice) {
		for (int i = indice; i < clientes.length - 1 && clientes[i] != null; i++) {
			clientes[i] = clientes[i + 1];// desplar un indice
		}
		if (indice == clientes.length - 1)
			clientes[clientes.length - 1] = null;
	}

	/* buscarCliente (indiceNoSuperaTamano */

	/**
	 * @param dni
	 * @return cliente
	 */
	public Cliente buscarCliente(String dni) {
		int posicion = buscarIndiceCliente(dni);
		if (indiceNoSuperaTamano(posicion))
			return new Cliente(clientes[posicion]);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Clientes [clientes=" + Arrays.toString(clientes) + ", MAX_CLIENTES=" + MAX_CLIENTES + "]";
	}

}