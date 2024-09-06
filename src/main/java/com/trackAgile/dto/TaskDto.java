package com.trackAgile.dto;

import java.util.Date;

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
public class TaskDto {

	private Long id;
	private String taskId;
	private String title;
	private Date createDate;
	private Date assignDate;
	private Date completeDate;
	private String assignTo;
	private String createdBy;
	private String assignedBy;
	private String status;
	private String district;
	private String mandal;

//	private GPInfoDto gpInfo;

}
