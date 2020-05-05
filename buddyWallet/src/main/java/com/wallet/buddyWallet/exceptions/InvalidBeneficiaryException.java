package com.wallet.buddyWallet.exceptions;

public class InvalidBeneficiaryException extends Exception{
	private static final long serialVersionUID = -9079454849611061074L;

	public InvalidBeneficiaryException() {
		super();
	}
	
	public InvalidBeneficiaryException(final String message) {
		super(message);
	}
	
}
