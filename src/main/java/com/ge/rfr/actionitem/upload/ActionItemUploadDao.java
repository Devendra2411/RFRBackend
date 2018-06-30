package com.ge.rfr.actionitem.upload;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ge.rfr.actionitem.upload.model.ActionItemUpload;

@Repository
public interface ActionItemUploadDao extends JpaRepository<ActionItemUpload, Integer> {
    List<ActionItemUpload> findByActionItemId(int actionItemId);

    @Transactional
    int deleteByFileId(String fileId);
}
