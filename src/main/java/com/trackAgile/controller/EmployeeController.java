package com.trackAgile.controller;

import java.io.IOException;

import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackAgile.dto.ApiResponse;
import com.trackAgile.dto.EmployeeDto;
import com.trackAgile.dto.SiteInfoDto;
import com.trackAgile.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService empService;

	// saving employee and users at a time
	@PostMapping("/save")
	public ApiResponse saveEmpolyee(@RequestBody EmployeeDto employeeDto) {
		return empService.saveEmpolyee(employeeDto);

	}

	// creating employee and their emergency details and vehicle details
	@PostMapping("/create")
	public ApiResponse createEmployee(@RequestPart("empDTo") String empDTo, @RequestPart("file") MultipartFile file)
			throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		EmployeeDto employeeDto = objectMapper.readValue(empDTo, EmployeeDto.class);
		return empService.createEmployee(employeeDto, file);

	}

	@GetMapping("/get")
	public ApiResponse getEmployee() {
		return empService.getEmployee();

	}
	
	@GetMapping("/getById")
	public ApiResponse getEmployeeById(Long id) {
		return empService.getEmployeeById(id);
		
	}

}
