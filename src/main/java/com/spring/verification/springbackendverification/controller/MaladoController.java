package com.spring.verification.springbackendverification.controller;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.service.MaladoService;
import com.spring.verification.springbackendverification.service.MaladoUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(path="/api/")
public class MaladoController {
    private final MaladoService maladoService;
    private final  MaladoUserService maladoUserService;
    @Autowired 
    public MaladoController(MaladoService maladoService,MaladoUserService maladoUserService) {
        this.maladoService= maladoService;
        this.maladoUserService =maladoUserService;   
    }
    //Enregistrer un utilisateur
    @PostMapping(path="register")
    public String register(@RequestBody MaladoRequest request) {
        return maladoService.register(request);
    } 
    // Verification loginad dans la base
    @PostMapping(path="loginad")
    public ResponseEntity<?> loginad(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");
        return maladoService.loginad(loginad);
    }
    //Recuperation du token a partir du lien et confirmation de email a partir du token ( enable passe de l'etat 0 a l'etat 1) 
    @GetMapping(path="confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        return maladoService.confirmToken(token);
    }
    //Permet a l'utlisateur de saisir son mot de passe & confimer son mot de passe
    @PostMapping(path="confirmpassword")
    public ResponseEntity<?> confirmPassword(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
    	String password=object.getString("password");
    	String loginad =  object.getString("loginad");
    	return maladoService.confirmPassword(password,loginad);
    }
    //Permet de verifier si le mot de passe matches avec le mot de passe base
    @PostMapping(path="password")
    public ResponseEntity<?> login(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad"); 
        String password=object.getString("password");
        return maladoService.login(loginad,password);
    }
    //Verifie si enable a passe de l'etat 0 a l'etat 1 en utilisant le loginad
    @PostMapping(path="enable")
    public Boolean EnableUser(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");  
        return maladoService.VerifEnable(loginad);
    }
    //Verifie si le mot d'un utilisateur est null ou pas a partir de son loginad
    @PostMapping(path="passwordVerification")
    public Boolean PasswordUSER(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");  
        return maladoService.PasswordUser(loginad);
    }
    //Permet de verifier la validation d'un token
    @PostMapping(path="token")
    public Boolean TokenVerification(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String token=object.getString("token");  
        return maladoUserService.TokenVerification(token);
    }
                                             ///// Forgot Password //////
    
    //D'envoyer un lien de confirmation a partir de l'email 
    @PostMapping(path="forgotpassword")
    public Boolean forgotPassword(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String email=object.getString("email");  
        return maladoUserService.forgotPassword(email);
    }
    //Permet a l'utlisateur de saisir son mot de passe & confimer son mot de passe
    @PostMapping(path="confirmPasswordForgot")
    public ResponseEntity<?> ForgotPassword(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
    	String password=object.getString("password");
    	String email =  object.getString("email");
    	return maladoService.confirmPasswordForgot(password,email);
    }
}