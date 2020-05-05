package com.wallet.buddyWallet.exceptions;

public class UserNameExistsException  extends Exception {

	private static final long serialVersionUID = -9079454849611061074L;

	public UserNameExistsException() {
		super();
	}

	public UserNameExistsException(final String message) {
		super(message);

	}
}
