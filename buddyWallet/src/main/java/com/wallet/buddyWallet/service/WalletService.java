package com.wallet.buddyWallet.service;

import java.util.List;

import com.wallet.buddyWallet.enitites.Account;
import com.wallet.buddyWallet.enitites.Beneficiary;
import com.wallet.buddyWallet.enitites.Mails;
import com.wallet.buddyWallet.enitites.Transactions;
import com.wallet.buddyWallet.exceptions.AccountBlockedException;
import com.wallet.buddyWallet.exceptions.InsufficientBalanceException;
import com.wallet.buddyWallet.exceptions.InvalidBeneficiaryException;
import com.wallet.buddyWallet.exceptions.InvalidLoginCredentialsException;
import com.wallet.buddyWallet.exceptions.InvalidTransactionPasswordException;
import com.wallet.buddyWallet.exceptions.ResourceNotFoundException;
import com.wallet.buddyWallet.exceptions.UnknownErrorException;
import com.wallet.buddyWallet.exceptions.UserNameExistsException;

public interface WalletService {
	
	List<Account> getAllUsers() throws ResourceNotFoundException,UnknownErrorException;
	boolean createAccount(Account acnt) throws ResourceNotFoundException,UserNameExistsException,UnknownErrorException;
	Account getAccountDetails(long accNum)throws ResourceNotFoundException;
	String deposit(long accNum,String tranPassword,double amount) throws ResourceNotFoundException,InvalidTransactionPasswordException,UnknownErrorException;
	String withdraw(long accNum,String tranPassword,double amount) throws ResourceNotFoundException,InvalidTransactionPasswordException,InsufficientBalanceException,UnknownErrorException;
	String fundTransfer(long accNum,String tranPassword,int beneficiaryId, double amount,String message) throws ResourceNotFoundException,InvalidTransactionPasswordException,InsufficientBalanceException,InvalidBeneficiaryException,UnknownErrorException;
	List<Transactions> printTransactions(long accNum) throws ResourceNotFoundException;	
	long login(String userName,String password) throws AccountBlockedException,ResourceNotFoundException,InvalidLoginCredentialsException;
	List<Beneficiary> getAllBeneficiaries(long accNum) throws ResourceNotFoundException;
	boolean addBeneficiary(long accNum,Beneficiary beneficiary) throws ResourceNotFoundException,UnknownErrorException;
	boolean updateBeneficiary(long accNum,int index,Beneficiary beneficiary) throws ResourceNotFoundException,UnknownErrorException;
	boolean deleteBeneficiary(int beneficiaryId) throws ResourceNotFoundException,UnknownErrorException;
	boolean addMail(Mails mail) throws UnknownErrorException;
	List<Mails> getAllMails() throws ResourceNotFoundException;
	boolean updateMyAccount(Account updatedAccount) throws ResourceNotFoundException;
	
//	void saveDp(long accNum, byte[] img) throws Exception;
}
