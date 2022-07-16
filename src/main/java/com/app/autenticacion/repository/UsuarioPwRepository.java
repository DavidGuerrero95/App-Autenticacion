package com.app.autenticacion.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.autenticacion.requests.UsuarioPw;

public interface UsuarioPwRepository extends MongoRepository<UsuarioPw, String> {

	@RestResource(path = "buscar")
	public UsuarioPw findByUsername(@Param("username") String username);
	
	@RestResource(path = "exist-user")
	public Boolean existsByUsername(@Param("username") String username);

}