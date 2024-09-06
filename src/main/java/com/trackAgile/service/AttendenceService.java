package com.trackAgile.service;

import org.locationtech.jts.io.ParseException;

import com.trackAgile.dto.ApiResponse;
import com.trackAgile.dto.AttendenceDto;

public interface AttendenceService {

	public ApiResponse markAttendence(Long id, AttendenceDto attendenceDto);

	// using geometry --using
	public ApiResponse saveLocation(Long id, AttendenceDto attendenceDto) throws ParseException;

	public ApiResponse updateLocation(Long id, AttendenceDto attendenceDto);

	// for geometry ----using
	AttendenceDto getAttendence(Long id);

	// for getting multipoint locations --using
	public ApiResponse getLocationAsWKT(Long id);

	public ApiResponse getAllLocations();

}
