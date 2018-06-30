package com.ge.rfr.phonecalldetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ge.rfr.phonecalldetails.model.PhoneCallDetails;

@Repository
public interface PhoneCallDao extends JpaRepository<PhoneCallDetails, Integer> {

    List<PhoneCallDetails> findByRfrWorkflowId(int rfrworkflowId);

}
