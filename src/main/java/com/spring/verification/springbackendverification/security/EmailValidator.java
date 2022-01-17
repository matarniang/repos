package com.spring.verification.springbackendverification.security;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    private final String EMAIL_PATTERN = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private final String prefix_stg="STG_";
    private final String prefix_tmc="TMC_";
    @Override
    public boolean test(String email) {
        return email.matches(EMAIL_PATTERN);
    }
    
    public boolean startsWith_stg(String loginad){
    	return loginad.startsWith(prefix_stg);
    }
    public boolean startsWith_tmc(String loginad){
    	return loginad.startsWith(prefix_tmc);
    }
}
