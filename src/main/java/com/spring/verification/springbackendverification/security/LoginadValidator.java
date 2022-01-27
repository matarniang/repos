package com.spring.verification.springbackendverification.security;
import org.springframework.stereotype.Service;

@Service
public class LoginadValidator {
	
    private final String prefix_stg="STG_";
    private final String prefix_tmc="TMC_";

    public boolean startsWith_stg(String loginad){
    	return loginad.startsWith(prefix_stg);
    }
    public boolean startsWith_tmc(String loginad){
    	return loginad.startsWith(prefix_tmc);
    }

}