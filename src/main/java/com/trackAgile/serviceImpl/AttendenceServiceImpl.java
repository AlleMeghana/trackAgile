package com.trackAgile.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.trackAgile.Entity.Attendence;
import com.trackAgile.Entity.User;
import com.trackAgile.dto.ApiResponse;
import com.trackAgile.dto.AttendenceDto;
import com.trackAgile.repository.AttendenceRepository;
import com.trackAgile.repository.UserRepository;
import com.trackAgile.service.AttendenceService;

@Service
public class AttendenceServiceImpl implements AttendenceService {

	private static final Logger logger = LoggerFactory.getLogger(AttendenceService.class);

	@Autowired
	private AttendenceRepository attenedenceRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public ApiResponse markAttendence(Long id, AttendenceDto attendenceDto) {
//		Optional<User> userOpt = userRepo.findById(id);
//		Attendence attendance = new Attendence();
//		GeometryFactory geometryFactory = new GeometryFactory();
//		if (userOpt.isPresent()) {
//			attendance.setDate(attendenceDto.getDate());
//			attendance.setStatus(attendenceDto.getStatus());
//			attendance.setLogInTime(attendenceDto.getLogInTime());
//			attendance.setLogoutTime(attendenceDto.getLogoutTime());
//
//			// Initialize the list if it's null
//			if (attendance.getLocationTrack() == null) {
//				attendance.setLocationTrack(new ArrayList<>());
//			}
//
//			// Add all Points from the DTO to the attendance location track
//			List<Point> points = new ArrayList<>();
//			Point point=null;
//			for (int i = 0; i < attendenceDto.getLastLocX().size(); i++) {
//				Double lastLocX = attendenceDto.getLastLocX().get(i);
//				Double lastLocY = attendenceDto.getLastLocY().get(i);
//				 point = geometryFactory.createPoint(new Coordinate(lastLocX, lastLocY));
//				points.add(point);
//			}
//			String pointsWKT = points.stream()
//	                .map(Point::toText)
//	                .collect(Collectors.joining(", ", "MULTIPOINT(", ")"));
//			
//			for (Point point1 : points) {
//		        String pointTrack = "POINT(" + point.getX() + " " + point.getY() + ")";
////		        attenedenceRepo.saveAttendance(attendance.getDate(), attendance.getStatus(), pointTrack);
//		    }
////			 attendance.getLocationTrack().addAll(points);
//			
//
////	        attendance.setLastNowLoaction(points.get(points.size() - 1)); // Set the last point as the current location
//	        attendance.getLocationTrack().addAll(points); // Add all points to the location track
//	        logger.info("Atten+dance saved successfully for user with ID: {}", attendance);
//	        attenedenceRepo.saveAttendance(attendance.getDate(), attendance.getStatus(), attendance.getLocationTrack());
//	    }
//
//		
//		return new ApiResponse(attendance, HttpStatus.OK);
//	}

		Optional<User> userOpt = userRepo.findById(id);
		Attendence attendence = new Attendence();

		if (userOpt.isPresent()) {
			attendence.setDate(attendenceDto.getDate());

			GeometryFactory geometryFactory = new GeometryFactory();
			Point point = geometryFactory
					.createPoint(new Coordinate(attendenceDto.getLastLocX(), attendenceDto.getLastLocY()));
			attendence.setStatus(attendenceDto.getStatus());
//			attendence.setLogInTime(attendenceDto.getLogInTime());
			attendence.setLogoutTime(attendenceDto.getLogoutTime());

			Point pointTrack = geometryFactory
					.createPoint(new Coordinate(attendenceDto.getLastLocX(), attendenceDto.getLastLocY()));

//			if (attendence.getLocationTrack() == null) {
//				attendence.setLocationTrack(new ArrayList<>());
//			}

//			attendence.getLocationTrack().add(pointTrack);

			// Ensure locationTrack is initialized
//			if (attendence.getLocationTrack() == null) {
//				attendence.setLocationTrack(new ArrayList<>());
//			}
			// Add the new point to the list
//			attendence.getLocationTrack().add(pointTrack);

			//// adding code for list of coordinates #points

			attenedenceRepo.saveAttendence(attendence.getDate(), attendence.getStatus(), point.toText(),
					pointTrack.toText());
		}
		return new ApiResponse(attendence, HttpStatus.OK);
	}

	// saving the location //using
	@Override
	public ApiResponse saveLocation(Long id, AttendenceDto attendenceDto) throws ParseException {
		Optional<User> userOpt = userRepo.findById(id);
		Attendence attendence = new Attendence();
		if (userOpt.isPresent()) {
			WKTReader wktReader = new WKTReader();
			Geometry geometry = wktReader
					.read("POINT(+" + attendenceDto.getLastLocX() + " " + attendenceDto.getLastLocY() + " )");

			attendence.setPoint(geometry);
			attendence.setLastKnownLocation(geometry);
			attendence.setDate(attendenceDto.getDate());
			attendence.setStatus(attendenceDto.getStatus());
			attendence.setUser(userOpt.get());

			attenedenceRepo.save(attendence);
		}
		return new ApiResponse(attendence, HttpStatus.OK);

	}

	// for updating the Locations in points
	@Override
	public ApiResponse updateLocation(Long id, AttendenceDto attendenceDto) {

		Optional<Attendence> userOpt = attenedenceRepo.findById(id);
		Attendence attendence = null;

		if (userOpt.isPresent()) {
			// Check if the attendance record exists for this user
			Optional<Attendence> attendenceOpt = attenedenceRepo.findById(id);
			if (attendenceOpt.isPresent()) {
				// If it exists, update it
				attendence = attendenceOpt.get();
			} else {
				// If it doesn't exist, create a new one
				attendence = new Attendence();
//		        attendence.setUser(userOpt.get()); // Set the user in the attendance record
			}

			// Create a GeometryFactory to create new points
			GeometryFactory geometryFactory = new GeometryFactory();
			Point newPoint = geometryFactory
					.createPoint(new Coordinate(attendenceDto.getLastLocX(), attendenceDto.getLastLocY()));

			Geometry existingGeometry = attendence.getPoint();

			if (existingGeometry == null) {
				// If no geometry exists, set the new point
				attendence.setPoint(newPoint);
			} else if (existingGeometry instanceof Point) {
				// If the existing geometry is a point, create a MultiPoint with the new point
				Point[] points = new Point[] { (Point) existingGeometry, newPoint };
				MultiPoint multiPoint = geometryFactory.createMultiPoint(points);
				attendence.setPoint(multiPoint);
			} else if (existingGeometry instanceof MultiPoint) {
				// If the existing geometry is a MultiPoint, add the new point to it
				MultiPoint multiPoint = (MultiPoint) existingGeometry;
				Coordinate[] newCoordinates = new Coordinate[multiPoint.getNumGeometries() + 1];
				for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
					newCoordinates[i] = multiPoint.getGeometryN(i).getCoordinate();
				}
				newCoordinates[newCoordinates.length - 1] = newPoint.getCoordinate();
				attendence.setPoint(geometryFactory.createMultiPointFromCoords(newCoordinates));
			}

			// Update the last known location, date, and status
			attendence.setLastKnownLocation(newPoint);
			attendence.setDate(attendenceDto.getDate());
			attendence.setStatus(attendenceDto.getStatus());
			logger.info("Attendence saved successfully for user with ID: {}", attendence);

			// Save the updated attendance record
			attenedenceRepo.save(attendence);

		}
		return new ApiResponse(attendence, HttpStatus.OK);

	}

	@Override

	public AttendenceDto getAttendence(Long id) {
		Attendence attendence = attenedenceRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Attendence not found"));

		// Create and return AttendenceDto
		AttendenceDto attendenceDto = new AttendenceDto();
		attendenceDto.setId(attendence.getId());
		attendenceDto.setDate(attendence.getDate());
		attendenceDto.setStatus(attendence.getStatus());

		if (attendence.getLastKnownLocation() != null) {
			attendenceDto.setLastLocX(attendence.getLastKnownLocation().getCoordinate().x);
			attendenceDto.setLastLocY(attendence.getLastKnownLocation().getCoordinate().y);
		}
		if (attendence.getPoint() != null) {
			attendenceDto.setLocTrackX(attendence.getLastKnownLocation().getCoordinate().x);
			attendenceDto.setLocaTrackY(attendence.getLastKnownLocation().getCoordinate().y);
		}

		return attendenceDto;
	}

	// for getting list of points
	@Override
	public ApiResponse getLocationAsWKT(Long id) {
		Optional<Attendence> attendenceOpt = attenedenceRepo.findById(id);

		Attendence attendence = attendenceOpt.get();
		AttendenceDto attendenceDto = new AttendenceDto();
		attendenceDto.setId(attendence.getId());
		attendenceDto.setDate(attendence.getDate());
		attendenceDto.setStatus(attendence.getStatus());
		if (attendence.getLastKnownLocation() != null) {
			attendenceDto.setLastLocX(attendence.getLastKnownLocation().getCoordinate().x);
			attendenceDto.setLastLocY(attendence.getLastKnownLocation().getCoordinate().y);
		}
		if (attendence != null) {
			WKTWriter writer = new WKTWriter();
			String pointWkt = writer.write(attendence.getPoint());
			attendenceDto.setPointWkt(pointWkt);
		}
		return new ApiResponse(attendenceDto, HttpStatus.OK);
	}

	@Override
	public ApiResponse getAllLocations() {
		List<Attendence> attendecneList = attenedenceRepo.findAll();
		List<AttendenceDto> attendenceDtoList = new ArrayList<AttendenceDto>();
		for (Attendence attendence : attendecneList) {
			AttendenceDto attendenceDto = new AttendenceDto();
			attendenceDto.setId(attendence.getId());
			attendenceDto.setDate(attendence.getDate());
			attendenceDto.setStatus(attendence.getStatus());
			if (attendence.getLastKnownLocation() != null) {
				attendenceDto.setLastLocX(attendence.getLastKnownLocation().getCoordinate().x);
				attendenceDto.setLastLocY(attendence.getLastKnownLocation().getCoordinate().y);
			}
			if (attendence != null) {
				WKTWriter writer = new WKTWriter();
				String pointWkt = writer.write(attendence.getPoint());
				attendenceDto.setPointWkt(pointWkt);
			}
			attendenceDtoList.add(attendenceDto);

		}
		return new ApiResponse(attendenceDtoList, HttpStatus.OK);
	}

}
