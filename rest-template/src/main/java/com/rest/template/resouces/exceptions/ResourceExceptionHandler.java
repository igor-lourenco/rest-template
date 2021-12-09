package com.rest.template.resouces.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rest.template.services.exceptions.ConnectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@ExceptionHandler(ConnectNotFoundException.class)
	public ResponseEntity<StandardError> connectNotFound(ConnectNotFoundException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND; // 404
		StandardError err = new StandardError();
		err.setTimestamp(LocalDateTime.now().format(formatter));
		err.setStatus(status.value());
		err.setError("Conexão não encontrado");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
}