package com.indigital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.indigital.bean.Cliente;
import com.indigital.dao.ClienteDao;

@Service
public class ClienteService {
	@Autowired
    @Qualifier("mysql")
    private ClienteDao clienteDao;
	
	public void insertarCliente(Cliente cliente) {
		clienteDao.insertClientToDb(cliente);
	}
	
	public List<Cliente> obtenerDatosCliente() {
		return clienteDao.obtenerDatosCliente();
	}
}
