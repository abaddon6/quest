package com.volvo.jvs.quest.rest;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Validated
@PropertySource("classpath:/messages.properties")
public class GenericController {

	@Autowired
	private Environment env;
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleResourceNotFoundException(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : violations) {
			String msg = getEnv().getProperty(violation.getMessage());
			if(msg == null) {
				msg = violation.getMessage();
			}
			strBuilder.append(msg + "\n");		
		}
		return strBuilder.toString();
	}

	protected Environment getEnv() {
		return env;
	}
}
