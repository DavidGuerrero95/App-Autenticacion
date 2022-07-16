package com.app.autenticacion.requests;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Roles {

	@Id
	private String id;
	private String name;

}