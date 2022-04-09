package com.spring.verification.springbackendverification.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Demande {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String action;
    private String application;
    private String retour;
	private String satus;
    private LocalDateTime date_insertion;
    private LocalDateTime date_retour;
    //private LocalDateTime date_mise_a_jour;
    private String login;
    private String password;
	@ManyToOne
    @JoinColumn(nullable = false,
                name = "app_user_id")
    private AppUser appUser;
	@SuppressWarnings("static-access")
	public Demande(String action, String application,String login,String password) {
		this.action = action;
		this.application = application;
		this.login = login;
		this.password = password;
//		this.setPassword(null);
		this.setDate_insertion(date_insertion.now());
		this.setDate_retour(null);
		//this.setDate_mise_a_jour(null);
		this.setRetour(null);
		this.setSatus("i");		
	}
	public Demande() {
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public void setApplication(String application) {
		this.application = application;
	}
	public String getSatus() {
		return satus;
	}
	public void setSatus(String satus) {
		this.satus = satus;
	}
	public LocalDateTime getDatedebut() {
		return date_insertion;
	}
	public void setDatedebut(LocalDateTime datedebut) {
		this.date_insertion = datedebut;
	}
	public LocalDateTime getDatefin() {
		return date_retour;
	}
	public void setDatefin(LocalDateTime datefin) {
		this.date_retour = datefin;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public AppUser getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}
	
    public String getRetour() {
		return retour;
	}
	public void setRetour(String retour) {
		this.retour = retour;
	}
	public LocalDateTime getDate_insertion() {
		return date_insertion;
	}
	public void setDate_insertion(LocalDateTime date_insertion) {
		this.date_insertion = date_insertion;
	}
	public LocalDateTime getDate_retour() {
		return date_retour;
	}
	public void setDate_retour(LocalDateTime date_retour) {
		this.date_retour = date_retour;
	}
//	public LocalDateTime getDate_mise_a_jour() {
//		return date_mise_a_jour;
//	}
//	public void setDate_mise_a_jour(LocalDateTime date_mise_a_jour) {
//		this.date_mise_a_jour = date_mise_a_jour;
//	}
    
    

}
