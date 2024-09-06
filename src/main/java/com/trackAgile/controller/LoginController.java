package com.trackAgile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackAgile.dto.ApiResponse;
import com.trackAgile.dto.AuthRequestDto;
import com.trackAgile.dto.UserDto;
import com.trackAgile.security.JwtService;
import com.trackAgile.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtservice;

	@Autowired
	private LoginService loginService;

	//Login 
	@PostMapping("/authenticate")
	public String authenticateAndetToken(@RequestBody AuthRequestDto authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			return jwtservice.generateToken(userDetails);
		} else
			throw new UsernameNotFoundException("Invalid User");
	}

	//For saving user 
	@PostMapping("/save-user")
	public ApiResponse saveUsers(@RequestBody UserDto usersDto) {
		return loginService.saveUsers(usersDto);

	}
	
	//Updating password
	@PostMapping("/update-password")
    public ApiResponse updatePassword(@RequestBody UserDto usersDto) {
		return loginService.updatePassword(usersDto);
       
    }

}
