package com.ge.rfr.boxapi;

public class FolderDetails {

    private String name;

    private ParentFolderId parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParentFolderId getParent() {
        return parent;
    }

    public void setParent(ParentFolderId parent) {
        this.parent = parent;
    }

}
