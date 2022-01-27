package com.spring.verification.springbackendverification.security;

public class Message {
	
	private String message;
	
	private int code ;
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Message(String message,int code) {
	this.code = code;
    this.message=message;		
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	

}
