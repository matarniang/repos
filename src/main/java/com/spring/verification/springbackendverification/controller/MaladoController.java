package com.spring.verification.springbackendverification.controller;
import com.spring.verification.springbackendverification.model.AppUser;
import com.spring.verification.springbackendverification.model.Demande;
import com.spring.verification.springbackendverification.model.DemandeRequest;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.repository.AppUserRepository;
import com.spring.verification.springbackendverification.repository.DemandeRepository;
import com.spring.verification.springbackendverification.security.Message;
import com.spring.verification.springbackendverification.service.MaladoService;
import com.spring.verification.springbackendverification.service.MaladoUserService;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*" , maxAge = 3600)
@RequestMapping(path="/api/")
public class MaladoController {
    private final MaladoService maladoService;
    private final MaladoUserService maladoUserService;
    private final DemandeRepository demandeRepository;
    private final AppUserRepository appUserRepository;
    @Autowired 
    public MaladoController(DemandeRepository demandeRepository,AppUserRepository appUserRepository,MaladoService maladoService,MaladoUserService maladoUserService) {
        this.demandeRepository = demandeRepository;
    	this.appUserRepository = appUserRepository;
    	this.maladoService= maladoService;
        this.maladoUserService =maladoUserService;   
    }
    @PreAuthorize("hasRole('ADMIN')")
    //Enregistrer un utilisateur
    @PostMapping(path="register")
    public String register(@RequestBody MaladoRequest request) {
        return maladoService.register(request);
    }
    @PreAuthorize("hasRole('USER')")
    //Enregistrer une demande
    @PostMapping(path="demande")
    public String demande(@RequestBody DemandeRequest request) {
        return maladoService.demande(request);
    }
    @PreAuthorize("hasRole('USER')")
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(path="listeDemande")
    public ResponseEntity<List<Demande>> listDemande(@RequestBody String request){
    	JSONObject object=new JSONObject(request);
        String login=object.getString("loginad");
        List<Demande> list = demandeRepository.GetDemande(login);
        return new ResponseEntity(list, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
 	@PostMapping(path="deletedemande")
    public String delete(@RequestBody String request){
    	JSONObject object=new JSONObject(request);
        Long id=object.getLong("id");
        if(!demandeRepository.existsById(id))
            return "demande n'existe pas dans la base";
        demandeRepository.deleteById(id);
        return "demande suprimer";
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(path="demandeI/{login}")
    public ResponseEntity<List<Demande>> list(@PathVariable("login") String login){
        List<Demande> list = demandeRepository.GetDemandeI(login);
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    //Retourner les utilisateurs existant dans la base
    @PreAuthorize("hasRole('ADMIN')")
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(path="maladoUsers")
    public ResponseEntity<List<AppUser>> list(){
        List<AppUser> list = appUserRepository.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    // Retourner les informations d'un utilisateur a partir de son loginad
    @PreAuthorize("hasRole('USER')")
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(path="maladoUser")
    public ResponseEntity<AppUser> getById(@RequestBody String request){
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");
        if(appUserRepository.getUser(loginad)==null)
            return new ResponseEntity(new Message("no existe",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        AppUser appUser = appUserRepository.getUser(loginad);
        return new ResponseEntity(appUser, HttpStatus.OK);
    }
    
    //Verification loginad dans la base
    @PreAuthorize("hasRole('USER')")
    @PostMapping(path="loginad")
    public ResponseEntity<?> loginad(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");
        return maladoService.loginad(loginad);
    }
    //Recuperation du token a partir du lien et confirmation de email a partir du token ( enable passe de l'etat 0 a l'etat 1) 
    @PreAuthorize("hasRole('USER')")   
    @GetMapping(path="confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        return maladoService.confirmToken(token);
    }
    @PreAuthorize("hasRole('USER')")
    //Permet a l'utlisateur de saisir son mot de passe & confimer son mot de passe
    @PostMapping(path="confirmpassword")
    public ResponseEntity<?> confirmPassword(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
    	String password=object.getString("password");
    	String loginad =  object.getString("loginad");
    	return maladoService.confirmPassword(password,loginad);
    }
    @PreAuthorize("hasRole('USER')")
    //Permet de verifier si le mot de passe matches avec le mot de passe base
    @PostMapping(path="password")
    public ResponseEntity<?> login(@RequestBody String request) {
    	JSONObject object=new JSONObject(request); 
        String loginad=object.getString("loginad"); 
        String password=object.getString("password");
        return maladoService.login(loginad,password);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping(path="connexion")
    public ResponseEntity<?> Connexion(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad"); 
        String password=object.getString("password");
        return maladoService.login(loginad,password);
    }
    //Verifie si enable a passe de l'etat 0 a l'etat 1 en utilisant le loginad
    @PreAuthorize("hasRole('USER')")
    @PostMapping(path="enable")
    public Boolean EnableUser(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");  
        return maladoService.VerifEnable(loginad);
    }
    //Verifie si le mot d'un utilisateur est null ou pas a partir de son loginad
    @PreAuthorize("hasRole('USER')")
    @PostMapping(path="passwordVerification")
    public Boolean PasswordUSER(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");  
        return maladoService.PasswordUser(loginad);
    }
    @PreAuthorize("hasRole('USER')")
    //Permet de verifier la validation §d'un token
    @PostMapping(path="token")
    public Boolean TokenVerification(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String token=object.getString("token");  
        return maladoUserService.TokenVerification(token);
    }
    @PreAuthorize("hasRole('USER')")
    //Recuperation du token a partir du lien et confirmation de email a partir du token ( enable passe de l'etat 0 a l'etat 1) 
    @PostMapping(path="user")
    public AppUser GetUser(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");
        return maladoService.GetUser(loginad);
    }
                                             ///// Forgot Password //////
 
    //D'envoyer un lien de confirmation a partir de l'email 
    @PostMapping(path="forgotpassword")
    public Boolean forgotPassword(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String email=object.getString("email"); 
        //String loginad=object.getString("loginad"); 
        return maladoUserService.forgotPassword(email);
    }

}