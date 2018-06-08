package com.volvo.jvs.quest.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.jvs.quest.service.email.EmailService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthenticationController extends GenericController{

	@Value("${spring.security.expiration-time}")
    public long expirationTime;
	
	@Value("${spring.security.secret}")
    private String secret;
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public String authenticationRequest(@RequestParam String emailAddress) {
		String token = Jwts.builder()
                .setSubject(emailAddress)
                .setExpiration(new Date(System.currentTimeMillis() + getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, getSecret().getBytes())
                .compact();
		getEmailService().sendSimpleMessage(emailAddress, "Quest application - token test", "http://localhost:3000/admin/?token="+token);
		return "Send"; 
	}

	protected EmailService getEmailService() {
		return emailService;
	}

	protected long getExpirationTime() {
		return expirationTime;
	}

	protected String getSecret() {
		return secret;
	}
}
