package com.spring.verification.springbackendverification.service;

import com.spring.verification.springbackendverification.email.EmailSender;
//import com.orange.sonatel.Malado.Models.AdUser;
import com.spring.verification.springbackendverification.model.AppUser;
//import com.spring.verification.springbackendverification.model.RegistrationRequest;
import com.spring.verification.springbackendverification.repository.AppUserRepository;
import com.spring.verification.springbackendverification.security.EmailValidator;
import com.spring.verification.springbackendverification.security.LoginadValidator;
import com.spring.verification.springbackendverification.security.Message;
import com.spring.verification.springbackendverification.security.PasswordEncoder;
import com.spring.verification.springbackendverification.security.token.ConfirmationToken;
import com.spring.verification.springbackendverification.security.token.ConfirmationTokenService;
//import springbackendverification.service.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

@Service
public class MaladoUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final EmailValidator emailValidator;
    private final LoginadValidator loginadValidator;
    private final ConfirmationTokenService confirmationTokenService;
    @Autowired
    public MaladoUserService(LoginadValidator loginadValidator,AppUserRepository appUserRepository,EmailSender emailSender,PasswordEncoder passwordEncoder,EmailValidator emailValidator, ConfirmationTokenService confirmationTokenService) {
        this.loginadValidator =loginadValidator;
    	this.appUserRepository = appUserRepository;
        this.emailSender= emailSender;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = emailValidator;
        this.confirmationTokenService = confirmationTokenService;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
    }
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<?> loginUpUser(String request, HttpServletRequest httprequest){
    	AppUser appUser = appUserRepository.getUser(request);
        boolean userExists = appUserRepository.findByLoginad(request).isPresent();
        boolean tmc_prefixe = loginadValidator.startsWith_tmc(request);
        boolean stg_prefixe = loginadValidator.startsWith_stg(request);
        if ((tmc_prefixe)||(stg_prefixe)){
            if (userExists==true) {
                AppUser appUserPrevious =  appUserRepository.findByLoginad(appUser.getLoginad()).get();
                Boolean isEnabled = appUserPrevious.getEnabled();
                    if (!isEnabled) {
                        boolean isValidEmail = emailValidator.test(appUser.getEmail());
                    	if (isValidEmail) {
                            String token = UUID.randomUUID().toString();
                            saveConfirmationToken(appUserPrevious, token);    
                            persistToken(token, httprequest);
                            String link = "http://localhost:8080/api/confirm?token=" + token;
                            emailSender.sendEmail(appUser.getEmail(), buildEmail(appUser.getLastName(),appUser.getFirstName(), link));
                            return new ResponseEntity(new Message("Verifie your emai",HttpStatus.OK.value()), HttpStatus.OK);
                    	}
                    	return new ResponseEntity(new Message(" email is not valide ",HttpStatus.OK.value()), HttpStatus.OK);
                    }
                    return new ResponseEntity(new Message(String.format("User with %s Connected", appUser.getEmail()),HttpStatus.OK.value()), HttpStatus.OK);
           }
            return new ResponseEntity(new Message(String.format("%s does not existe in database",request),HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new Message( String.format("Please verif your loginAD format"),HttpStatus.NOT_ACCEPTABLE.value()), HttpStatus.NOT_ACCEPTABLE);
    }
    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if (userExists==false) {
            String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(appUser.getPassword());
            appUser.setPassword(encodedPassword);
            appUserRepository.save(appUser);
            return String.format("User create");
        }
        return String.format("User Existe in database");
    }
    private void saveConfirmationToken(AppUser appUser, String token) {
//    	persistToken(appUser.getLoginad(), httprequest);
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(2), appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
    }
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
    public void persistToken(String token, HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		List<String> tokens = (List<String>) request.getSession().getAttribute("MY_SESSION_TOKEN");
		if (tokens == null) {
			tokens = new ArrayList<>();
			request.getSession().setAttribute("MY_SESSION_TOKEN", tokens);
		}
		tokens.removeAll(tokens);
		tokens.add(token);
		request.getSession().setAttribute("MY_SESSION_TOKEN", tokens);
	}
    private String buildEmail(String lastname, String firstname, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#EE762E\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#EE762E\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#FFD7B5\n"
                + "\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Bonjour " + lastname + firstname + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"><a href=\"" + link + "\">Activate Now</a></p></blockquote>\n Link will expire in 1 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
    
    
    
}