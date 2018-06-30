package com.ge.rfr.workflow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ge.rfr.common.model.User;
import com.ge.rfr.workflow.model.RfrWorkflow;

@Repository
public interface WorkflowDao extends JpaRepository<RfrWorkflow, Integer> {

    RfrWorkflow findByEquipSerialNumberAndOutageId(String esnId, int outageId);

    List<RfrWorkflow> findByAssignedEngineersSso(String ssoId);
    
    List<RfrWorkflow> findByChangeTrackingCreatedBy(User user);
    
    RfrWorkflow findByIdAndAssignedEngineersSso(int id,String ssoId);
}
