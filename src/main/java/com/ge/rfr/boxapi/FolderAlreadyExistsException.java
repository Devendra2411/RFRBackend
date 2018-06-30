package com.ge.rfr.boxapi;

public class FolderAlreadyExistsException extends RuntimeException{

	public FolderAlreadyExistsException() {
		super("Folder Already Exists in Box");
	}

}
