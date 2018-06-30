package com.ge.rfr.actionitem.notes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ge.rfr.actionitem.notes.model.RfrNotes;

@Repository
public interface RfrNotesDao extends JpaRepository<RfrNotes, Integer> {
    List<RfrNotes> findByActionItemId(int actionItemId);
}
