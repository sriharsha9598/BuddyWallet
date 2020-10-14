package com.wallet.buddyWallet.dao;
import java.util.List;

import com.wallet.buddyWallet.enitites.Account;
import com.wallet.buddyWallet.enitites.Beneficiary;
import com.wallet.buddyWallet.enitites.Mails;
import com.wallet.buddyWallet.enitites.BuddyTransactions;
import com.wallet.buddyWallet.exceptions.AccountBlockedException;
import com.wallet.buddyWallet.exceptions.InvalidLoginCredentialsException;
import com.wallet.buddyWallet.exceptions.ResourceNotFoundException;


public interface WalletDao {
		List<Account> getAllUsers() throws ResourceNotFoundException;
		long generateAcntNumb();
		int generateTransactionId();
		void updateAccount(Account account);
		void addMail(Mails mail);
		void createAccount(Account acnt);
		Account getAccount(long accNum) throws ResourceNotFoundException;
		boolean findAccount(String user) throws ResourceNotFoundException;
		List<BuddyTransactions> printTransactions(long acNum) throws ResourceNotFoundException;
		long login(String userName,String password)throws ResourceNotFoundException,InvalidLoginCredentialsException,AccountBlockedException; 
		List<Beneficiary> getAllBeneficiaries(long accNum) throws ResourceNotFoundException;
		void deleteBeneficiary(int id) throws ResourceNotFoundException;
		Beneficiary findBeneficiary(int beneficiaryId);
		int generateBeneficiaryId();
		int generateMailId();
		List<Mails> getAllMails();
}
