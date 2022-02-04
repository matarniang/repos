package com.spring.verification.springbackendverification.controller;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.service.MaladoService;

//import java.security.Principal;
//import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.websocket.Session;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.session.FindByIndexNameSessionRepository;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/")
public class MaladoController {
    private final MaladoService maladoService;

    @Autowired  
    public MaladoController(MaladoService maladoService) {
        this.maladoService= maladoService;
        
    }
    @PostMapping(path="loginad")
    public ResponseEntity<?> loginad(@RequestBody String request,HttpServletRequest httprequest) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad");  
        return maladoService.loginad(loginad,httprequest);
    }
    @PostMapping(path="register")
    public String register(@RequestBody MaladoRequest request) {
        return maladoService.register(request);
    } 
    @GetMapping(path="confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token,HttpSession session) {
        return maladoService.confirmToken(token,session);
    }
    @PostMapping(path="confirmpassword")
    public ResponseEntity<?> confirmPassword(@RequestBody String request,HttpSession session) {
    	JSONObject object=new JSONObject(request);
    	String password=object.getString("password");
    	String confirm=object.getString("confirm");
    	return maladoService.confirmPassword(password,confirm,session);
    }
    @PostMapping(path="login")
    public ResponseEntity<?> login(@RequestBody String request) {
    	JSONObject object=new JSONObject(request);
        String loginad=object.getString("loginad"); 
        String password=object.getString("password");
        return maladoService.login(loginad,password);
    }

}