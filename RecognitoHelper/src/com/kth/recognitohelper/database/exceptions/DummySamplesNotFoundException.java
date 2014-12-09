package com.kth.recognitohelper.database.exceptions;

/**
 * Created by georgios.savvidis on 26/09/14.
 */
public class DummySamplesNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public DummySamplesNotFoundException() {
		super();
	}

	public DummySamplesNotFoundException(String detailedMessage) {
		super(detailedMessage);
	}
}
