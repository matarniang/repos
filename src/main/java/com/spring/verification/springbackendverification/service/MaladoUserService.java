package com.spring.verification.springbackendverification.service;

import com.spring.verification.springbackendverification.email.EmailSender;
//import com.orange.sonatel.Malado.Models.AdUser;
import com.spring.verification.springbackendverification.model.AppUser;
//import com.spring.verification.springbackendverification.model.RegistrationRequest;
import com.spring.verification.springbackendverification.repository.AppUserRepository;
import com.spring.verification.springbackendverification.security.EmailValidator;
import com.spring.verification.springbackendverification.security.PasswordEncoder;
import com.spring.verification.springbackendverification.security.token.ConfirmationToken;
import com.spring.verification.springbackendverification.security.token.ConfirmationTokenService;

//import springbackendverification.service.AppUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MaladoUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public MaladoUserService(AppUserRepository appUserRepository,EmailSender emailSender,PasswordEncoder passwordEncoder,EmailValidator emailValidator, ConfirmationTokenService confirmationTokenService) {
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

    public String loginUpUser(String request){
    	AppUser appUser = appUserRepository.getUser(request);
    	System.out.print(appUser);
//        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        boolean userExists = appUserRepository.findByLoginad(request).isPresent();
        System.out.print(userExists);
        
        boolean tmc_prefixe = emailValidator.startsWith_tmc(request);
        boolean stg_prefixe = emailValidator.startsWith_stg(request);
        
        if ((tmc_prefixe)||(stg_prefixe)) {
        	
            if (userExists==true) {
                AppUser appUserPrevious =  appUserRepository.findByLoginad(appUser.getLoginad()).get();
//                AppUser appUserPrevious =  appUserRepository.findByEmail(eappUser.getEmail()).get();
                
                Boolean isEnabled = appUserPrevious.getEnabled();
//                if (appUserPrevious!=null) {
                    if (!isEnabled) {
                        boolean isValidEmail = emailValidator.test(appUser.getEmail());
                    	if (isValidEmail) {
                            String token = UUID.randomUUID().toString();
                            //A method to save user and token in this class
                            saveConfirmationToken(appUserPrevious, token);                            
                            String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
                            emailSender.sendEmail(appUser.getEmail(), buildEmail(appUser.getLastName(),appUser.getFirstName(), link));
                            return token;
                    	}
                    	
                    	return String.format("email  is not valide ");
                    }
                   return String.format("User with email %s Connected !!!!", appUser.getEmail());
//                }
//                return String.format(" %s does not existe in database",request);
           }
            return String.format(" %s loginAD does not existe in database",request);
        }
        return String.format("Please verif your loginAD format : STG_***** || TMC_*****");
    }
    
    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        System.out.print(userExists);
        if (userExists==false) {
//            MaladoUser appUserPrevious =  appUserRepository.findByEmail(appUser.getEmail()).get();
//            Boolean isEnabled = appUserPrevious.getEnabled();
//            if (!isEnabled) {
//                return String.format("User create");
//            }
////            return String.format("User with email %s already exists!", appUser.getEmail());
            String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(appUser.getPassword());
            appUser.setPassword(encodedPassword);
            //Saving the user after encoding the password
            appUserRepository.save(appUser);
            //Returning token
            return String.format("User create");
        }
        return String.format("User Existe in database");
    }
    
    private void saveConfirmationToken(AppUser appUser, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);

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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Bonjour " + lastname + firstname + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
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
