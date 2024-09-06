package com.trackAgile.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trackAgile.Entity.User;
import com.trackAgile.dto.ApiResponse;
import com.trackAgile.dto.UserDto;
import com.trackAgile.repository.UserRepository;
import com.trackAgile.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	UserRepository usersRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ApiResponse saveUsers(UserDto usersDto) {

		if (usersDto != null) {
			Optional<User> userOpt = usersRepository.findByUserName(usersDto.getUserName());
			if (userOpt.isPresent()) {
				// If username already exists, return ApiResponse indicating the conflict
				return new ApiResponse("Username already exists", HttpStatus.CONFLICT);
			} else {
				// If username does not exist, create a new user
				User user = new User();
				user.setUserName(usersDto.getUserName());
				user.setPassword(passwordEncoder.encode(usersDto.getPassword()));
				user.setEmail(usersDto.getEmail());
				user.setFullName(usersDto.getFullName());
				user.setId(usersDto.getId());
				user.setIsFirstLogIn(usersDto.getIsFirstLogIn());
				user.setStatus(usersDto.getStatus());
				user.setRole(usersDto.getRole());

				usersRepository.save(user);
				return new ApiResponse("User saved successfully", HttpStatus.OK);
			}
		} else {
			// If usersDto is null, return ApiResponse indicating bad request
			return new ApiResponse("No data found to insert", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ApiResponse updatePassword(UserDto usersDto) {
		if (usersDto != null) {
			// Check if the username already exists
			Optional<User> userOpt = usersRepository.findByUserName(usersDto.getUserName());
			if (userOpt.isPresent()) {
				// If username exists, update the password
				User user = userOpt.get();
				user.setPassword(passwordEncoder.encode(usersDto.getPassword()));
//		            user.setIsFirstLogin(false);
				usersRepository.save(user);
				return new ApiResponse("Password reset successfully", HttpStatus.OK);
			} else {
				// If username does not exist, return ApiResponse indicating user not found
				return new ApiResponse("User not found", HttpStatus.NOT_FOUND);
			}
		} else {
			// If usersDto is null, return ApiResponse indicating bad request
			return new ApiResponse("No data found to insert", HttpStatus.BAD_REQUEST);
		}

	}
}
