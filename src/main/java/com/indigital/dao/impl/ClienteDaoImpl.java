package com.indigital.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.indigital.bean.Cliente;
import com.indigital.dao.ClienteDao;


@Repository("mysql")
public class ClienteDaoImpl implements ClienteDao{

	@Autowired 
	private JdbcTemplate jdbcTemplate;
	
	private static class ClientRowMapper implements RowMapper<Cliente>{

		@Override
		public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Cliente client = new Cliente();
			client.setId(rs.getInt("id"));
			client.setEdad(rs.getInt("edad"));
			client.setNombres((rs.getString("nombres")));
			client.setApellidos((rs.getString("apellidos")));
			client.setFechaNacimiento(rs.getString("fechanacimiento"));
			return client;
		}
		
	};
	
	@Override
	public void insertClientToDb(Cliente cliente) {
		// TODO Auto-generated method stub
		final String sql = "INSERT INTO CLIENTE (nombres,apellidos, edad, fechanacimiento) VALUES (?,?,?,?)";
		
		jdbcTemplate.update(sql, cliente.getNombres(), cliente.getApellidos(), cliente.getEdad(), cliente.getFechaNacimiento());
	}

	@Override
	public List<Cliente> obtenerDatosCliente() {
		// TODO Auto-generated method stub
		final String sql = "SELECT id, COALESCE(nombres,'') nombres, COALESCE(apellidos, '') apellidos, COALESCE(edad, 0) edad, COALESCE(fechanacimiento, '') fechanacimiento FROM CLIENTE";
		List<Cliente> clientes = jdbcTemplate.query(sql, new ClientRowMapper());
		return clientes;
	}

}
