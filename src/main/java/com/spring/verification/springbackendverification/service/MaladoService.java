package com.spring.verification.springbackendverification.service;
import com.spring.verification.springbackendverification.model.AppUser;
import com.spring.verification.springbackendverification.model.MaladoUserRole;
import com.spring.verification.springbackendverification.repository.AppUserRepository;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.security.EmailValidator;
import com.spring.verification.springbackendverification.security.Message;
import com.spring.verification.springbackendverification.security.token.ConfirmationToken;
import com.spring.verification.springbackendverification.security.token.ConfirmationTokenService;
import com.spring.verification.springbackendverification.security.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MaladoService {
    private final MaladoUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmTokenService;
    private final AppUserRepository appUserRepository;
    
    public MaladoService(AppUserRepository appUserRepository,MaladoUserService appUserService,EmailValidator emailValidator,PasswordEncoder passwordEncoder,ConfirmationTokenService confirmTokenService) {
       	this.appUserService = appUserService;
        this.passwordEncoder= passwordEncoder;
        this.emailValidator = emailValidator;
        this.confirmTokenService = confirmTokenService;
        this.appUserRepository = appUserRepository;
    }
    public ResponseEntity<?> loginad(String request) {
    	ResponseEntity<?> tokenForNewUser=appUserService.loginUpUser(request);
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
       
    	return new ResponseEntity(new Message("Token is conformed",HttpStatus.OK.value()), HttpStatus.OK);

    }    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
    public ResponseEntity<?> confirmPassword(String password,String loginad) {
    	AppUser appUser = appUserRepository.getUser(loginad);
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(password);
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        return new ResponseEntity(new Message("Registration reussie",HttpStatus.OK.value()), HttpStatus.OK);
   } 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<?> login(String loginad,String password){
      	AppUser appUser = appUserRepository.getUser(loginad);
        boolean userExists = appUserRepository.findByLoginad(loginad).isPresent();
            if (userExists==true) {
            	String passwordbase = appUser.getPassword();
            	if(passwordEncoder.bCryptPasswordEncoder().matches(password,passwordbase)) {
                return new ResponseEntity(new Message("Welcome to malado",HttpStatus.OK.value()), HttpStatus.OK);
            }  	
                return new ResponseEntity(new Message("password not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
           }
            return new ResponseEntity(new Message(String.format("%s does not existe in database",loginad),HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
	public Boolean VerifEnable(String loginad){
		AppUser appUser = appUserRepository.getUser(loginad);
        Boolean isEnabled = appUser.getEnabled();
        if (isEnabled) {
        	return isEnabled;
        }
        return isEnabled;
	}
	public Boolean PasswordUser(String loginad) {
		AppUser appUser = appUserRepository.getUser(loginad);
        String isPassword = appUser.getPassword();
        if (isPassword!=null) {	
        	return true;
        }
        return false;
	}	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<?> confirmPasswordForgot(String password,String email) {
		AppUser appUser = appUserRepository.getUserEmail(email);
		String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(password);
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
		return new ResponseEntity(new Message("Welcome to malado",HttpStatus.OK.value()), HttpStatus.OK);
	}
}