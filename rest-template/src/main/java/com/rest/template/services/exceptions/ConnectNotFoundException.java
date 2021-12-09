package com.rest.template.services.exceptions;

public class ConnectNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ConnectNotFoundException(String msg) {
		super(msg);
	}
}
