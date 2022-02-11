package com.spring.verification.springbackendverification.security;

public class MessageSession {
	
	private String message;
	
	private int code ;
	
	private String SessionID ;
	
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public MessageSession(String message, int code, String sessionID,String token) {
		
		this.message = message;
		this.code = code;
		this.SessionID = sessionID;
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getSessionID() {
		return SessionID;
	}

	public void setSessionID(String sessionID) {
		SessionID = sessionID;
	}
	

}
