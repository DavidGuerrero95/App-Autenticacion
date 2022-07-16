package com.app.autenticacion.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.autenticacion.requests.UsuarioPw;

@FeignClient(name = "app-usuarios")
public interface UsersFeignClient {

	@GetMapping("/users/iniciarSesion/{username}")
	public UsuarioPw autenticacion(@PathVariable String username);

}
