package com.spring.verification.springbackendverification.service;
import com.spring.verification.springbackendverification.model.AppUser;
import com.spring.verification.springbackendverification.model.MaladoUserRole;
import com.spring.verification.springbackendverification.repository.AppUserRepository;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.security.EmailValidator;
import com.spring.verification.springbackendverification.security.LoginadValidator;
import com.spring.verification.springbackendverification.security.Message;
import com.spring.verification.springbackendverification.security.token.ConfirmationToken;
//import com.spring.verification.springbackendverification.security.token.ConfirmationTokenRepository;
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
    private final LoginadValidator loginadValidator;
   

    public MaladoService(LoginadValidator loginadValidator,AppUserRepository appUserRepository,MaladoUserService appUserService,EmailValidator emailValidator,PasswordEncoder passwordEncoder,ConfirmationTokenService confirmTokenService) {
    	
    	this.loginadValidator = loginadValidator;
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
        	//return new RedirectView("confirmpassword");
            return new ResponseEntity(new Message("Token not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        if (confirmToken.get().getConfirmedAt() != null) {
        	//return new RedirectView("confirmpassword");
        	return new ResponseEntity(new Message("Email is already confirmed",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        LocalDateTime expiresAt = confirmToken.get().getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
        	//return new RedirectView("confirmpassword");
        	return new ResponseEntity(new Message("Token is already expired!",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        confirmTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmToken.get().getAppUser().getEmail());   
        //return new RedirectView("verification");
    	return new ResponseEntity(new Message("Token is conformed",HttpStatus.OK.value()), HttpStatus.OK);

    }    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
    public ResponseEntity<?> confirmPassword(String password,String confirmpassword,String loginad) {
    	AppUser appUser = appUserRepository.getUser(loginad);
    	//Optional<ConfirmationToken> confirmToken=confirmTokenService.getToken(token);
    	int compareto = password.compareTo(confirmpassword);
        if(compareto==0){
           //if(token!=""){
        //    	Optional<ConfirmationToken> confirmToken=confirmTokenService.getToken(token);
            //if (confirmToken.isEmpty()) {
              //  return new ResponseEntity(new Message("Token not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
            //}
            //LocalDateTime expiresAt=confirmToken.get().getExpiresAt();
            //if (expiresAt.isBefore(LocalDateTime.now())) {
            //	return new ResponseEntity(new Message("Token is already expired!",HttpStatus.OK.value()), HttpStatus.OK);
            //}
            //if (confirmToken.get().getConfirmedAt()!=null){
                String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(password);
                appUser.setPassword(encodedPassword);
                appUserRepository.save(appUser);
                return new ResponseEntity(new Message("Registration reussie,vous pouvez vous connecter now",HttpStatus.OK.value()), HttpStatus.OK);
               }
            return new ResponseEntity(new Message("Email not confrim",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
           // }
           //return new ResponseEntity(new Message("Votre Session a expire",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
         //}
        //return new ResponseEntity(new Message("les mots de passe saisis ne correspondent pas",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
   } 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<?> login(String loginad,String password){
      	AppUser appUser = appUserRepository.getUser(loginad);
        boolean userExists = appUserRepository.findByLoginad(loginad).isPresent();
//        boolean tmc_prefixe = loginadValidator.startsWith_tmc(loginad);
//        boolean stg_prefixe = loginadValidator.startsWith_stg(loginad);
        //if ((tmc_prefixe)||(stg_prefixe)) {
            if (userExists==true) {
            	String passwordbase = appUser.getPassword();
            	if(passwordEncoder.bCryptPasswordEncoder().matches(password,passwordbase)) {
                return new ResponseEntity(new Message("Welcome to malado",HttpStatus.OK.value()), HttpStatus.OK);
            }  	
                return new ResponseEntity(new Message("password not found!",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
           }
            return new ResponseEntity(new Message(String.format("%s does not existe in database",loginad),HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
      //  }
      //  return new ResponseEntity(new Message( String.format("Please verifie your loginAD format"),HttpStatus.NOT_ACCEPTABLE.value()), HttpStatus.NOT_ACCEPTABLE);
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
		


}