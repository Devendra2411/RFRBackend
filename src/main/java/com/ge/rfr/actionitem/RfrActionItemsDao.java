package com.ge.rfr.actionitem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ge.rfr.actionitem.model.RfrActionItem;

@Repository
public interface RfrActionItemsDao extends JpaRepository<RfrActionItem, Integer> {

    List<RfrActionItem> findByWorkflowIdOrderByStatus(int workflowId);

}
