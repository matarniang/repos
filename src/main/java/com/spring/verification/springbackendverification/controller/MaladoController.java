package com.spring.verification.springbackendverification.controller;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.service.MaladoService;

//import springbackendverification.model.RegistrationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// /v1/registration
@RestController
@RequestMapping(path = "/api/v1/registration")
public class MaladoController {

    private final MaladoService registrationService;

    @Autowired
    public MaladoController(MaladoService registrationService) {
        this.registrationService = registrationService;
    }
    @PostMapping(path = "loginad")
    public @ResponseBody String loginad(@RequestParam String request) {
//    	return request.getEmail();
        return registrationService.loginad(request);
    }
    
    @PostMapping(path = "register")
    public String register(@RequestBody MaladoRequest request) {
        return registrationService.register(request);
        //return "registered";
    }
    
    @GetMapping(path = "confirm")
//    @PostMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
    
//    @GetMapping(path = "password")
    @PostMapping(path = "confirm")
    public String confirmPassword(@RequestParam String password,String confirmpassword,String token) {
      return registrationService.confirmPassword(password,confirmpassword,token);
//    	return String.format("password : " +password+ "confirmpassword :"+confirmpassword+ "token :"+token);
    }
    

}
