package com.app.autenticacion.requests;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioPw {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "Username cannot be null")
	@Size(max = 20)
	@Indexed(unique = true)
	private String username;

	@NotNull(message = "Password cannot be null")
	@Size(min = 6, max = 20, message = "About Me must be between 6 and 20 characters")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	private Boolean enabled;
	private Integer attempts;
	private Integer code;
	private List<Roles> roles;

}
