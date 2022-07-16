package com.app.autenticacion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.autenticacion.clients.UsersFeignClient;
import com.app.autenticacion.requests.UsuarioPw;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AutenticacionController {

	@Autowired
	UsersFeignClient uClient;

	@CircuitBreaker(name = "usuarios", fallbackMethod = "iniciarSesionAlternativo")
	public UsuarioPw obtenerUsuario(String username) {
		return uClient.autenticacion(username);
	}

	@SuppressWarnings("unused")
	private UsuarioPw iniciarSesionAlternativo(String username, Throwable e) {
		log.info(e.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio Usuarios no disponible");
	}

}
