package br.com.masterdelivery.controller.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.masterdelivery.controller.exception.StandardError;
import br.com.masterdelivery.controller.exception.ValidationError;
import br.com.masterdelivery.service.exception.AuthorizationException;
import br.com.masterdelivery.service.exception.DataIntegrityException;
import br.com.masterdelivery.service.exception.LocationException;
import br.com.masterdelivery.service.exception.ObjectFoundException;
import br.com.masterdelivery.service.exception.ObjectNotFoundException;


@ControllerAdvice
public class ControllerExceptionHandler {
	
	private static final String VOCE_NAO_CHEGOU_AO_LOCAL = "Você não chegou ao local !";

	private static final String ACESSO_NEGADO = "Acesso negado";

	private static final String OBJETO_DUPLICADO = "Objeto duplicado";

	private static final String ERRO_DE_VALIDACAO = "Erro de validação";

	private static final String INTEGRIDADE_DE_DADOS = "Integridade de dados";
	
	private static final String NAO_ENCONTRADO = "Não encontrado";

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), NAO_ENCONTRADO, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), INTEGRIDADE_DE_DADOS, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		
		ValidationError err = new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(), ERRO_DE_VALIDACAO, e.getMessage(), request.getRequestURI());
		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			err.addError(x.getField(), x.getDefaultMessage());
		}		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}
	
	@ExceptionHandler(ObjectFoundException.class)
	public ResponseEntity<StandardError> objectFound(ObjectFoundException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.FOUND.value(), OBJETO_DUPLICADO, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.FOUND).body(err);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(), ACESSO_NEGADO, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}
	
	@ExceptionHandler(LocationException.class)
	public ResponseEntity<StandardError> authorization(LocationException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.I_AM_A_TEAPOT.value(), VOCE_NAO_CHEGOU_AO_LOCAL, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(err);
	}
}
