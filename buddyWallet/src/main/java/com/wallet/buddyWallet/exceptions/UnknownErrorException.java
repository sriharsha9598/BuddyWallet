package com.wallet.buddyWallet.exceptions;

public class UnknownErrorException extends Exception {
	private static final long serialVersionUID = -9079454849611061074L;

	public UnknownErrorException() {
		super();
	}

	public UnknownErrorException(final String message) {
		super(message);

	}

}
