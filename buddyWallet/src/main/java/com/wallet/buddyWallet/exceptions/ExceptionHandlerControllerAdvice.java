package com.wallet.buddyWallet.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler{

	/* Method: handleResourceNotFound
	 *  ResourceNotFoundException: It is raised when no data found 
	 *  httpResponseStatus: 404- NOT_FOUND
	 *  return responseBody: error message 'User Not Found error!!' is returned
	*/
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final ResourceNotFoundException exception) {
		ExceptionResponse error = new ExceptionResponse("User Not Found error!!");
		return error;
	}

	
	/* Method: handleInvalidTransactionPassword
	 *  InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password
	 *  httpResponseStatus: 401-UNAUTHORIZED 
	 *  return responseBody: error message 'Invalid Transaction Password' is returned
	*/
	@ExceptionHandler(InvalidTransactionPasswordException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody ExceptionResponse handleInvalidTransactionPassword(final InvalidTransactionPasswordException exception) {
		ExceptionResponse error = new ExceptionResponse(exception.getLocalizedMessage());
		return error;
	}

	
	/* Method: handleAccountBlockedExeption
	 *  AccountBlockedException: It is raised when an user account is blocked by admin and that user tries to login
	 *  httpResponseStatus: 403-FORBIDDEN 
	 *  return responseBody: error message given 'Your account is temporarily blocked' is returned
	*/
	@ExceptionHandler(AccountBlockedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody ExceptionResponse handleAccountBlockedException(final AccountBlockedException  exception) {
		ExceptionResponse error = new ExceptionResponse(exception.getLocalizedMessage());
		return error;
	}
	

	/* Method: handleUserNameExistsException
	 *  UserNameExistsException: It is raised when an user name exists and new customer wanna use the same
	 *  httpResponseStatus: 403-FORBIDDEN 
	 *  return responseBody: error message 'User Name already taken by another user' is returned
	*/
	@ExceptionHandler(UserNameExistsException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody ExceptionResponse handleUserNameExistsException(final UserNameExistsException exception) {
		ExceptionResponse error = new ExceptionResponse(exception.getLocalizedMessage());
		return error;
	}

	
	/* Method: handleInsufficientBalanceException
	 *  InsufficientBalanceException: It is raised when user tries to transfer money more than existing balance in wallet
	 *  httpResponseStatus: 403-FORBIDDEN 
	 *  return responseBody: error message 'Insufficient Funds!!' is returned
	*/
 	@ExceptionHandler(InsufficientBalanceException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody ExceptionResponse handleInsufficientBalance(final InsufficientBalanceException exception) {
		ExceptionResponse error = new ExceptionResponse(exception.getLocalizedMessage());
		return error;
	}

 	
 	/* Method: handleInvalidBeneficiary
	 *  InvalidBeneficiaryException: It is raised when user tries to transfer money to an incorrect account Number or beneficiary
	 *  httpResponseStatus: 404-NOT_FOUND 
	 *  return responseBody: error message 'Invalid Beneficiary!!' is returned
	*/
	@ExceptionHandler(InvalidBeneficiaryException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ExceptionResponse handleInvalidBeneficiary(final InvalidBeneficiaryException exception) {
		ExceptionResponse error = new ExceptionResponse(exception.getLocalizedMessage());
		return error;
	}

	
	/* Method: handleInvalidLogin
	 *  InvalidLoginCredentialsException: It is raised when an user enters an incorrect user name or password
	 *  httpResponseStatus: 401-UNAUTHORIZED 
	 *  return responseBody: error message 'Invalid Login Credentials!!' is returned
	*/
    @ExceptionHandler(InvalidLoginCredentialsException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody ExceptionResponse handleInvalidLogin(final InvalidLoginCredentialsException exception) {
		ExceptionResponse error = new ExceptionResponse(exception.getLocalizedMessage());
		return error;
	}

	
    /* Method: handleUnknownError
	 * UnknownErrorException: It is raised when an unexpected error or exception occurred during the process
	 *  httpResponseStatus: BAD_REQUEST 
	 *  return responseBody: error message 'some Background error!' is returned
	*/
	@ExceptionHandler(UnknownErrorException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionResponse handleUnknownError(final UnknownErrorException exception) {
		ExceptionResponse error = new ExceptionResponse(exception.getLocalizedMessage());
		return error;
	}

}