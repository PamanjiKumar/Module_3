package com.example.usermanagementservice.exception;

public class RoleChangeNotAllowedException extends RuntimeException{
	public RoleChangeNotAllowedException(String msg) {
		super(msg);
	}

}
