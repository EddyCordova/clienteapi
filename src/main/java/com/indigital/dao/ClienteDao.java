package com.indigital.dao;

import java.util.List;

import com.indigital.bean.Cliente;

public interface ClienteDao {


    void insertClientToDb(Cliente cliente);
    
    List<Cliente> obtenerDatosCliente();
}