package com.volvo.jvs.quest.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.jvs.quest.service.email.EmailService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthenticationController extends GenericController{

	public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 60_000; // 60 sec.
    
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public String authenticationRequest(@RequestParam String emailAddress) {
		String token = Jwts.builder()
                .setSubject(emailAddress)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
		getEmailService().sendSimpleMessage(emailAddress, "Quest application - token test", "http://localhost:3000/admin/?token="+token);
		return "Send"; 
	}

	protected EmailService getEmailService() {
		return emailService;
	}
}
