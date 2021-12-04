package com.app.autenticacion.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.autenticacion.clients.UsersFeignClient;
import com.app.autenticacion.models.Roles;
import com.app.autenticacion.models.UsuarioPw;
import com.app.autenticacion.repository.UsuarioPwRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class AutenticacionController {

	private final Logger logger = LoggerFactory.getLogger(AutenticacionController.class);

	@SuppressWarnings("rawtypes")
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	UsuarioPwRepository upRepository;

	@Autowired
	UsersFeignClient uClient;

	@PostMapping("/autenticacion/crear")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Boolean crearUsuario(@RequestBody UsuarioPw usuarioPw) throws IOException {
		try {
			upRepository.save(usuarioPw);
			return true;
		} catch (Exception e) {
			throw new IOException("Usuario: no existe! " + e.getMessage());
		}
	}

	@DeleteMapping("/autenticacion/eliminar")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public Boolean eliminarUsuario(@RequestBody UsuarioPw usuarioPw) throws IOException {
		try {
			upRepository.delete(usuarioPw);
			return true;
		} catch (Exception e) {
			throw new IOException("Error: " + e.getMessage());
		}
	}

	@PutMapping("/autenticacion/editar")
	public Boolean editarUsuarioAuth(@RequestParam String username, @RequestParam String uEdit,
			@RequestParam String newPassword, @RequestParam List<Roles> roles) throws IOException {
		try {
			UsuarioPw uP = upRepository.findByUsername(username);
			if (newPassword.isEmpty() && roles.isEmpty())
				uP.setUsername(uEdit);
			else if (uEdit.isEmpty() && roles.isEmpty())
				uP.setPassword(newPassword);
			else if (newPassword.isEmpty() && uEdit.isEmpty())
				uP.setRoles(roles);
			upRepository.save(uP);
			return true;
		} catch (Exception e) {
			throw new IOException("Error: " + e.getMessage());
		}
	}

	@CircuitBreaker(name = "usuarios", fallbackMethod = "iniciarSesionAlternativo")
	public UsuarioPw obtenerUsuario(String username) {
		return uClient.autenticacion(username);
	}

	@SuppressWarnings("unused")
	private UsuarioPw iniciarSesionAlternativo(String username, Throwable e) {
		logger.info(e.getMessage());
		return upRepository.findByUsername(username);
	}

	@PostMapping("/autenticacion/arreglar")
	@ResponseStatus(code = HttpStatus.OK)
	public String arreglar() {
		List<UsuarioPw> usuario = cbFactory.create("registro").run(() -> uClient.listarUsuariosPw(),
				e -> errorArreglarAuth(e));
		if (usuario != null) {
			upRepository.saveAll(usuario);
			return "arreglado";
		}
		return "Error";
	}

	private List<UsuarioPw> errorArreglarAuth(Throwable e) {
		logger.info("Error: " + e.getMessage());
		return null;
	}

}
