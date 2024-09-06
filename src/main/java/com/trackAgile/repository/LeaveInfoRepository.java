package com.trackAgile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trackAgile.Entity.LeaveInfo;
@Repository
public interface LeaveInfoRepository extends JpaRepository<LeaveInfo, Long> {

	LeaveInfo findByEmployeeId(Long id);

}
