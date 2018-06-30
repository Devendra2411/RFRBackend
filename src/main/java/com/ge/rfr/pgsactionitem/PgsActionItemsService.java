package com.ge.rfr.pgsactionitem;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ge.rfr.actionitem.model.AttributeType;
import com.ge.rfr.pgsactionitem.model.PgsActionItem;

@Service
public class PgsActionItemsService {

    private PgsActionItemsDao pgsActionItemsDao;

    public PgsActionItemsService(PgsActionItemsDao pgsActionItemsDao) {
        this.pgsActionItemsDao = pgsActionItemsDao;
    }

    public List<PgsActionItem> findWorkflowActionActionItems(List<AttributeType> attributesList,
                                                             List<String> attrValuesList) {
        return pgsActionItemsDao.findByAttributeTypeInAndAttributeValueIn(attributesList, attrValuesList);
    }

}
