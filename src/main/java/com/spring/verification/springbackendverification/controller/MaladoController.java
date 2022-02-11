package com.spring.verification.springbackendverification.controller;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.service.MaladoService;
import com.spring.verification.springbackendverification.service.MaladoUserService;

//import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.servlet.view.RedirectView;

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
    @PostMapping(path="loginad")
    public ResponseEntity<?> loginad(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");
        return maladoService.loginad(loginad);
    }
    @PostMapping(path="register")
    public String register(@RequestBody MaladoRequest request) {
        return maladoService.register(request);
    } 
    @GetMapping(path="confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
    	//redirectAttributes.addAttribute("token", token);
        return maladoService.confirmToken(token);
    }
        
    @PostMapping(path="confirmpassword")
    public ResponseEntity<?> confirmPassword(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
    	String password=object.getString("password");
    	String confirm=object.getString("confirm");
    	String loginad =  object.getString("loginad");
    	return maladoService.confirmPassword(password,confirm,loginad);
    }
    @PostMapping(path="password")
    public ResponseEntity<?> login(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad"); 
        String password=object.getString("password");
        return maladoService.login(loginad,password);
    }
    
    @PostMapping(path="enable")
    public Boolean EnableUser(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");  
        return maladoService.VerifEnable(loginad);
    }
    
    @PostMapping(path="passwordVerification")
    public Boolean PasswordUSER(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");  
        return maladoService.PasswordUser(loginad);
    }
    @PostMapping(path="token")
    public Boolean TokenVerification(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String token=object.getString("token");  
        return maladoUserService.TokenVerification(token);
    }

}