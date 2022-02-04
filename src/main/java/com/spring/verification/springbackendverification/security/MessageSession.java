package com.spring.verification.springbackendverification.security;

public class MessageSession {
	
	private String message;
	
	private int code ;
	
	private String SessionID ;

	public MessageSession(String message, int code, String sessionID) {
		
		this.message = message;
		this.code = code;
		this.SessionID = sessionID;
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
