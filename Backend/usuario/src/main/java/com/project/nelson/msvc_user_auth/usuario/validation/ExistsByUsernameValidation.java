package com.project.nelson.msvc_user_auth.usuario.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.project.nelson.msvc_user_auth.usuario.service.UsuarioService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

	@Autowired
	private UsuarioService service;

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if (service == null) {
			return true;
		}
		return !service.existsByUsername(username);
	}

}
