package com.ge.rfr.workflow.model.dto;

/**
 * This is used in conjunction with Jackson's JSON view feature to hide
 * certain fields when a DTO is used within a summary or list-view context.
 */
public interface Views {

    interface EsnListView {
    }

    interface OutageListView {
    }
    
    interface SiteNamesListView{
    	
    }
    interface CompleteObjectsView{
    	
    }
}
