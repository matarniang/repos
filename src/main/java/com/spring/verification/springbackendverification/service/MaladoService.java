package com.spring.verification.springbackendverification.service;
import com.spring.verification.springbackendverification.model.AppUser;
import com.spring.verification.springbackendverification.model.MaladoUserRole;
import com.spring.verification.springbackendverification.repository.AppUserRepository;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.security.EmailValidator;
import com.spring.verification.springbackendverification.security.LoginadValidator;
import com.spring.verification.springbackendverification.security.Message;
import com.spring.verification.springbackendverification.security.token.ConfirmationToken;
import com.spring.verification.springbackendverification.security.token.ConfirmationTokenService;
import com.spring.verification.springbackendverification.security.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class MaladoService {
    private final MaladoUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmTokenService;
    private final AppUserRepository appUserRepository;
    private final LoginadValidator loginadValidator;
    public MaladoService(LoginadValidator loginadValidator,AppUserRepository appUserRepository,MaladoUserService appUserService,EmailValidator emailValidator,PasswordEncoder passwordEncoder,ConfirmationTokenService confirmTokenService) {
        this.loginadValidator = loginadValidator;
    	this.appUserService = appUserService;
        this.passwordEncoder= passwordEncoder;
        this.emailValidator = emailValidator;
        this.confirmTokenService = confirmTokenService;
        this.appUserRepository = appUserRepository;
    }
    public ResponseEntity<?> loginad(String request,HttpServletRequest httprequest) {
    	ResponseEntity<?> tokenForNewUser=appUserService.loginUpUser(request,httprequest);
                return tokenForNewUser;
    }
    public String register(MaladoRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (isValidEmail) {
            String CreateNewUser = appUserService.signUpUser(new AppUser(request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getLoginad(),
                    MaladoUserRole.USER));
            return CreateNewUser;
        } else {
            return String.format("Email %s, not valid", request.getEmail());
        }
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
    public ResponseEntity<?> confirmToken(String token) {
        Optional<ConfirmationToken> confirmToken = confirmTokenService.getToken(token);
        if (confirmToken.isEmpty()) {
            return new ResponseEntity(new Message("Token not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        if (confirmToken.get().getConfirmedAt() != null) {
        	return new ResponseEntity(new Message("Email is already confirmed",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        LocalDateTime expiresAt = confirmToken.get().getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
        	return new ResponseEntity(new Message("Token is already expired!",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        confirmTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmToken.get().getAppUser().getEmail());
    	return new ResponseEntity(new Message("Your email is confirmed",HttpStatus.OK.value()), HttpStatus.OK);
    }    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
    public ResponseEntity<?> confirmPassword(String password,String confirmpassword,HttpSession session) {
    	String token = GetToken(session);
    	System.out.print("token :"+token);
    	int compareto = password.compareTo(confirmpassword);
        if(compareto==0){
           if(token!=""){
            	Optional<ConfirmationToken> confirmToken=confirmTokenService.getToken(token);
            if (confirmToken.isEmpty()) {
                return new ResponseEntity(new Message("Token not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
            }
            LocalDateTime expiresAt=confirmToken.get().getExpiresAt();
            if (expiresAt.isBefore(LocalDateTime.now())) {
            	return new ResponseEntity(new Message("Token is already expired!",HttpStatus.OK.value()), HttpStatus.OK);
            }
            if (confirmToken.get().getConfirmedAt()!=null){
            	//String passwordbase = confirmToken.get().getAppUser().getPassword();
            	AppUser appUser = confirmToken.get().getAppUser();
                String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(password);
                appUser.setPassword(encodedPassword);
                appUserRepository.save(appUser);
            	//if(passwordEncoder.bCryptPasswordEncoder().matches(password,passwordbase)) {
                return new ResponseEntity(new Message("Welcome to malado",HttpStatus.OK.value()), HttpStatus.OK);
           // }  	
               // return new ResponseEntity(new Message("password not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
               }
            return new ResponseEntity(new Message("Email not confrim",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
            }
           return new ResponseEntity(new Message("Session Exprire",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
         }
        return new ResponseEntity(new Message("password != confirmpassword",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
   } 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<?> login(String loginad,String password){
      	AppUser appUser = appUserRepository.getUser(loginad);
        boolean userExists = appUserRepository.findByLoginad(loginad).isPresent();
        boolean tmc_prefixe = loginadValidator.startsWith_tmc(loginad);
        boolean stg_prefixe = loginadValidator.startsWith_stg(loginad);
        if ((tmc_prefixe)||(stg_prefixe)) {
            if (userExists==true) {
            	String passwordbase = appUser.getPassword();
            	if(passwordEncoder.bCryptPasswordEncoder().matches(password,passwordbase)) {
                return new ResponseEntity(new Message("Welcome to malado",HttpStatus.OK.value()), HttpStatus.OK);
            }  	
                return new ResponseEntity(new Message("password not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
           }
            return new ResponseEntity(new Message(String.format("%s does not existe in database",loginad),HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new Message( String.format("Please verif your loginAD format"),HttpStatus.NOT_ACCEPTABLE.value()), HttpStatus.NOT_ACCEPTABLE);
    }
    
	public String GetToken(HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> messages = (List<String>) session.getAttribute("MY_SESSION_TOKEN");
		try {
			return messages.get(0);
			}
			catch(Exception e) {
			  return "";
		}
	}
}