package com.trackAgile.dto;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AttendenceDto {

	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	
	@CreationTimestamp
	private LocalDate date;
	
	private Time logInTime;
	private Time logoutTime;
	private String status;
	private List<Point> locationTrack;

	// last location time on 07/08/24
	private LocalDateTime lastKnownTime;

	// list for location track
	private Double locTrackX;
	private Double locaTrackY;

	//for getting multipoint 
	private String pointWkt;
	
	private Double lastLocX;
	private Double lastLocY;
	public void getLastLocX(double x) {
		
	}

}
