package com.spring.verification.springbackendverification.model;

public class MaladoRequest {

    private final String firstName;
	private final String lastName;
    private final String email;
    private final String password;
    private final String loginad;

    public MaladoRequest(String firstName,String lastName,String email,String password,String loginad) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.loginad = loginad;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public String getLoginad() {
		return loginad;
	}
}
