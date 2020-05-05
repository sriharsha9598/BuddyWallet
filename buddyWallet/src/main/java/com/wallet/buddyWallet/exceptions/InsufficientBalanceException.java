package com.wallet.buddyWallet.exceptions;

public class InsufficientBalanceException  extends Exception {

	private static final long serialVersionUID = -9079454849611061074L;

	public InsufficientBalanceException() {
		super();
	}

	public InsufficientBalanceException(final String message) {
		super(message);

	} 

}
