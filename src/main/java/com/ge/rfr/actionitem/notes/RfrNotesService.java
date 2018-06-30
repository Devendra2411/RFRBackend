package com.ge.rfr.actionitem.notes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ge.rfr.actionitem.RfrActionItemNotFoundException;
import com.ge.rfr.actionitem.RfrActionItemsService;
import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.actionitem.notes.model.RfrNotes;
import com.ge.rfr.actionitem.notes.model.dto.RfrNotesDto;
import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;

@Service
public class RfrNotesService {

    RfrNotesDao rfrNotesDao;
    RfrActionItemsService rfrActionItemsService;

    @Autowired
    public RfrNotesService(RfrNotesDao rfrNotesDao, RfrActionItemsService rfrActionItemsService) {
        this.rfrNotesDao = rfrNotesDao;
        this.rfrActionItemsService = rfrActionItemsService;
    }

    /**
     * Retrieves the list of notes for the given action item id
     *
     * @param actionItemId
     * @return List<RfrNotes>
     * @throws RfrActionItemNotFoundException Thrown if the action item id is invalid
     */
    public List<RfrNotes> getAllNotes(int actionItemId) {
        RfrActionItem rfrActionItem = rfrActionItemsService.getRfrActionItemDetails(actionItemId);
        if (rfrActionItem == null)
            throw new RfrActionItemNotFoundException(actionItemId);
        else {
            return rfrNotesDao.findByActionItemId(actionItemId);
        }

    }

    /**
     * Creates a note for the given action item id
     *
     * @param user         Captures the user details who logged in
     * @param actionItemId
     * @param rfrNotesDto
     * @return RfrNotes
     * @throws RfrActionItemNotFoundException Thrown if the action item id is invalid
     */
    public RfrNotes createNote(SsoUser user, int actionItemId, RfrNotesDto rfrNotesDto) {
        RfrActionItem rfrActionItem = rfrActionItemsService.getRfrActionItemDetails(actionItemId);
        if (rfrActionItem == null)
            throw new RfrActionItemNotFoundException(actionItemId);
        else {
            RfrNotes rfrNotes = new RfrNotes();
            rfrNotes.setNotes(rfrNotesDto.getNotes());
            rfrNotes.setActionItem(rfrActionItem);
            rfrNotes.setChangeTracking(new ChangeTracking(User.fromSsoUser(user)));
            return rfrNotesDao.saveAndFlush(rfrNotes);
        }
    }
}
