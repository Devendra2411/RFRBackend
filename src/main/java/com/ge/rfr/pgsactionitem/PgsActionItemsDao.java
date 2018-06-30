package com.ge.rfr.pgsactionitem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ge.rfr.actionitem.model.AttributeType;
import com.ge.rfr.pgsactionitem.model.PgsActionItem;

@Repository
public interface PgsActionItemsDao extends JpaRepository<PgsActionItem, Integer> {

    public List<PgsActionItem> findByAttributeTypeInAndAttributeValueIn(List<AttributeType> attributesList,
                                                                        List<String> attrValuesList);
}
