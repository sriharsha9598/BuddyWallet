package com.wallet.buddyWallet.exceptions;

public class ExceptionResponse {

	private String errorMessage;

	public ExceptionResponse(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public ExceptionResponse() {
		super();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
