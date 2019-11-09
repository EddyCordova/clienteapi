package com.indigital.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.indigital.bean.Cliente;
import com.indigital.bean.Respuesta;
import com.indigital.constants.Constants;
import com.indigital.service.ClienteService;

/**
 * 
 * @author Eddy Cordova
 *
 */

@RestController
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/listclientes")
	@RequestMapping(value = "/listclientes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cliente> listClientes() {
		return clienteService.obtenerDatosCliente();
	}

	@RequestMapping(value = "/kpideclientes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Respuesta> obtenerKpiClientes() {
		List<Respuesta> respuesta = new ArrayList<Respuesta>();

		try {
			List<Cliente> lstClientes = clienteService.obtenerDatosCliente();
			double promedio = obtenerPromedio(lstClientes);
			double desviacionEstandar = obtenerDesviacionestandar(lstClientes, promedio);
			respuesta.add(setRespuesta(Constants.PROMEDIO, String.valueOf(promedio)));
			respuesta.add(setRespuesta(Constants.DESVIACION_ESTANDAR, String.valueOf(desviacionEstandar)));
			return respuesta;
		} catch (Exception e) {
			respuesta.add(setRespuesta("Error", e.getMessage()));
			return respuesta;
		}
	}

	@RequestMapping(value = "/crearcliente", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Respuesta addClient(@RequestBody Cliente cliente) {
		Respuesta respuesta = new Respuesta();

		try {
			if (cliente.getNombres() == null || cliente.getNombres().isEmpty()) {
				throw new Exception("Debe ingresar nombre de cliente.");
			}
			if (cliente.getApellidos() == null || cliente.getApellidos().isEmpty()) {
				throw new Exception("Debe ingresar apellido de cliente.");
			}
			if (cliente.getEdad() == null) {
				throw new Exception("Debe ingresar edad de cliente.");
			}
			if (cliente.getFechaNacimiento() == null || cliente.getFechaNacimiento().isEmpty()) {
				throw new Exception("Debe ingresar fecha de nacimiento de cliente.");
			}else {
				if(!isValidFormat("dd/MM/yyyy", cliente.getFechaNacimiento())) {
					throw new Exception("Debe ingresar una fecha correcta (DD/MM/YYYY).");
				}
			}
			
			clienteService.insertarCliente(cliente);
			respuesta.setIdentificador("OK");
			respuesta.setRespuesta("Registro insertado correctamente");
			return respuesta;
		} catch (Exception e) {
			respuesta.setIdentificador("Error");
			respuesta.setRespuesta(e.getMessage());
			return respuesta;
		}
	}

	public Respuesta setRespuesta(String identificador, String mensaje) {
		Respuesta respuesta2 = new Respuesta();
		respuesta2.setIdentificador(identificador);
		respuesta2.setRespuesta(mensaje);
		return respuesta2;
	}

	public double obtenerPromedio(List<Cliente> lstClientes) {
		int sumaEdad = 0;
		for (int i = 0; i < lstClientes.size(); i++) {
			sumaEdad += lstClientes.get(i).getEdad();
		}

		double promedio = (double) sumaEdad / lstClientes.size();

		return promedio;

	}

	public double obtenerDesviacionestandar(List<Cliente> lstClientes, double promedio) {
		double suma = 0;
		for (int i = 0; i < lstClientes.size(); i++) {
			suma += Math.pow(((double) lstClientes.get(i).getEdad() - promedio), 2);
		}
		double desviacionEstandar = Math.sqrt((double) suma / lstClientes.size());
		return desviacionEstandar;
	}
	
    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

}
