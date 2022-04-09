package com.spring.verification.springbackendverification.model;

public class DemandeRequest {
	
    private String action;
    private String application;
    private String login;
    private String password;
    private AppUser appUser;

	public DemandeRequest(String action, String application,String login,String password) {
		
		this.action=action;
		this.application=application;
		this.login = login;
		this.password = password;
	}
	public AppUser getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApplication() {
		return application;
	}

	public String getLogin() {
		return login;
	}
	public void setLoginad(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    

}
