package com.app.autenticacion.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.autenticacion.models.UsuarioPw;

@FeignClient(name = "app-usuarios")
public interface UsersFeignClient {

	@GetMapping("/users/listarPw/")
	public List<UsuarioPw> listarUsuariosPw();
	
	@GetMapping("/users/iniciarSesion/{username}")
	public UsuarioPw autenticacion(@PathVariable String username);

}
