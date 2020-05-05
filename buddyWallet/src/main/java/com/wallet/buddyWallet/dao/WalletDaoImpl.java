package com.wallet.buddyWallet.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wallet.buddyWallet.enitites.Account;
import com.wallet.buddyWallet.enitites.Beneficiary;
import com.wallet.buddyWallet.enitites.Mails;
import com.wallet.buddyWallet.enitites.Transactions;
import com.wallet.buddyWallet.exceptions.InvalidLoginCredentialsException;
import com.wallet.buddyWallet.exceptions.ResourceNotFoundException;
import com.wallet.buddyWallet.exceptions.AccountBlockedException;

@Repository
public class WalletDaoImpl implements WalletDao  
{
	@Autowired
	EntityManager entityManager;

	/*  A single function for all the deposit,withdraw,fund transfer,edit-profile options
	 *	In all the operations balance or other details are set in service layer itself and calls dao layer
	 *  just to update (merge) it to existing database. So instead of writing all those functions,this is enough
	 */
	@Override
	public void updateAccount(Account account) {
		entityManager.merge(account);
	}
	
	
	/*Method:getAllUsers
	 * Description: Used in admin page to view all the existing account details
	 * @param: none
	 * @return List<Account>: a List of all the existing accounts
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@Override
	public List<Account> getAllUsers() throws ResourceNotFoundException{
		try {
		//retrieving a list of all the existing users in the database for admin page view
		String command="SELECT a from Account a";
		TypedQuery<Account> query= entityManager.createQuery(command,Account.class);
		List<Account> users=query.getResultList();
		return users;
		}
		catch(Exception e){
			throw new ResourceNotFoundException();
		}

	}
	
	
	/*Method:geenerateAcntNumb
	 * Description: Used generate a new account number whenevr a new account is created
	 * @param: none
	 * @return long: a new account number
    */
	@Override
	public long generateAcntNumb() {
		long tempId=500101000;
		
		//Checking whether any account exists or not initially
		String command = "SELECT count(a) from Account a";
		TypedQuery<Long> query = entityManager.createQuery(command, Long.class);
		long count=query.getSingleResult();	
		
		if(count>0) {
		//retrieving the maximum existing account number and setting the next value to current new account
		command = "SELECT max(a.accNum) from Account a";
		TypedQuery<Long> query2 = entityManager.createQuery(command, Long.class);
		long temp=query2.getSingleResult();
		tempId=temp+1;
		}
		return tempId;
	}		

	
	/*Method:geenerateTransactionId
	 * Description: Used generate a new transaction id whenever a new transaction is made
	 * @return int: a new transaction id
    */
	@Override
	public int generateTransactionId() {
		int tempId=1100;

		//Checking whether any transaction exists or not initially
		String command = "SELECT count(t) from Transactions t";
		TypedQuery<Long> query = entityManager.createQuery(command, Long.class);
		long count=query.getSingleResult();

		if(count>0) {
		//retrieving the maximum existing transaction id and setting the next value to current new account
		command = "SELECT max(t.id) from Transactions t";
		TypedQuery<Integer> query2 = entityManager.createQuery(command, Integer.class);
		int temp=query2.getSingleResult();
		tempId=temp+1;
		}
		return tempId;
	}

    
    /*Method:createAccount
	 * Description: Used to persist or add a new account into data table whenever a new account is created
	 * @param: none
	 * @return void: none
    */
	@Override
	public void createAccount(Account account) 
	{
		entityManager.persist(account);
	}
	
	
	/*Method:getAccount
	 * Description: Used to know an account details using it's account number
	 * @param long: Account number of the account about which details need to get
	 * @return Account: an Account object containing all the details is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	@Override
	public Account getAccount(long accNum) throws ResourceNotFoundException 
	{
		try {
		Account acnt=entityManager.find(Account.class, accNum);
		return acnt;
		}
		catch(NullPointerException e) {
			throw new ResourceNotFoundException();
		}
	}
	

	/*Method:findAccount
	 * Description: Used to know any account exists with a particular user name during signup 
	 * @param String: user name
	 * @return boolean: boolean 'true' if any account exists else false
	*/
	@Override
	public boolean findAccount(String user){
		//retrieving any account existing with that user name
		//As user name must be unique, retrieving data and returning boolean true if no account found
		//and throwing an exception if any account found with that user name
		String command = "SELECT acc FROM Account acc WHERE acc.userName=:user";
		TypedQuery<Account> query = entityManager.createQuery(command, Account.class);
		query.setParameter("user", user);
		try 
		{
			Account temp=query.getSingleResult();
			if(temp!=null)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	
	/*Method:printTransactions
	 * Description: Used in e-statement page to view the transaction history from that wallet
	 * @param long: Account number about which transactions need to be returned
	 * @return List<Transactions>: a List of all the transactions made from that wallet
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/	
	@Override
	public List<Transactions> printTransactions(long acNum) throws ResourceNotFoundException{
		
		try {
		//retrieving a list all the transactions performed by that account numbers
		Account account=getAccount(acNum);
		List<Transactions> list=account.getTransactions();
		Collections.reverse(list);
		return list;
		}
		catch(Exception e)
		{
			throw new ResourceNotFoundException();
		}
	}
	
	
	/*Method:login
	 * Description: Used in login page to access one's account by logging in
	 * @param String: userName- user name of that account
	 * @param String: password- password of that account
	 * @return long: respective Account Number if credentials are correct so that it is possible for further references
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws InvalidLoginCredentialsException: It is raised when an user enters an incorrect user name or password    
	 * @throws AccountBlockedException: It is raised when an user account is blocked by admin and that user tries to login
	*/
	@Override
	public long login(String userName, String password) throws InvalidLoginCredentialsException,AccountBlockedException{
		//for login purpose checking whether the credentials entered are valid or invalid
		//if valid, returning the account number to angular service for storing and using for further references
		//if invalid, throwing an exception
		String command = "SELECT acc FROM Account acc WHERE acc.userName=:user and acc.password=:pass";
		TypedQuery<Account> query = entityManager.createQuery(command, Account.class);
		query.setParameter("user", userName);
		query.setParameter("pass", password);
		try 
		{
			Account acnt=query.getSingleResult();
			if(acnt.getBlocked()==1)
			{
				throw new AccountBlockedException("Your account is temporarily Blocked!!");
			}
			return acnt.getAccNum();	
		}
		catch(NoResultException e)
		{
			throw new InvalidLoginCredentialsException("Invalid Username or Password!!");
		}
		catch(NullPointerException e)
		{
			throw new InvalidLoginCredentialsException("Invalid Username or Password!!");
		}
	}

	
	/*Method:getAllBeneficiaries
	 * Description: Used in manageBeneficiary page & fundTransfer page to view all the beneficiaries existing with that account
	 * @param long: Account number of that account whose beneficiaries need to be viewed
	 * @return List<Beneficiary>: a List of all beneficiaries linked to that account is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@Override 
	public List<Beneficiary> getAllBeneficiaries(long accNum) throws ResourceNotFoundException{
		Account account=getAccount(accNum);
		return account.getBeneficiaries();
	}
		
	
	/*Method:deleteBeneficiary
	 * Description: Used in manageBenficiary page to delete an existing beneficiary linked to that account
	 * @param int: beneficiaryId which is to be deleted
	 * @return boolean: boolean 'true' is returned if beneficiary is deleted successfully else 'false'0
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@Override
	public void deleteBeneficiary(int id) throws ResourceNotFoundException{
		try {
		//deleting the beneficiary with that beneficiaryID
		//if not found, throwing an exception
		String command = "DELETE Beneficiary b where b.id=:id";
		Query query = entityManager.createQuery(command);
		query.setParameter("id", id);
		query.executeUpdate();
		}
		catch(NoResultException e) {
			throw new ResourceNotFoundException();
		}
		catch(NullPointerException e) {
			throw new ResourceNotFoundException();
		}
	}
	
	
	/*Method:generateBeneficiaryId
	 * Description: Used generate a new beneficiary id whenever a new beneficiary is added	
	 * @return int: a new beneficiary id is returned
	 * @param: none
    */
	@Override
	public int generateBeneficiaryId() {
		int tempId=3300;

		//Checking whether any beneficiary exists or not initially
		String command = "SELECT count(b) from Beneficiary b";
		TypedQuery<Long> query = entityManager.createQuery(command, Long.class);
		long count=query.getSingleResult();

		if(count>0) {
		//retrieving the maximum existing beneficiary id and setting its next value to current new beneficiary
		command = "SELECT max(b.id) from Beneficiary b";
		TypedQuery<Integer> query2 = entityManager.createQuery(command, Integer.class);
		int temp=query2.getSingleResult();
		tempId=temp+1;
		}
		return tempId;
	}
	
	
	/*Method:findBeneficiary
	 * Description: Used to get the details of an beneficiary using that respective beneficairyId
	 * @param int: beneficiaryId whose details are needed
	 * @return Beneficiary: a Beneficiary object containing all the details of that beneficiaryId 
    */
	@Override
	public Beneficiary findBeneficiary(int beneficiaryId){
		//retrieving beneficiary details using beneficiary id
		try {
		Beneficiary beneficiary=entityManager.find(Beneficiary.class, beneficiaryId);
		return beneficiary;		
		}
		catch(NullPointerException e) {
			return null;
		}
	}

	
	/*Method:generateMailId
	 * Description: Used to generate a new Mail id whenever a new mail, feedback or complaint is added	
	 * @return int: a new mail id is returned
    */
	@Override
	public int generateMailId() {
		int tempId=100;
		//Checking whether any mail or feedback or complaint exists or not initially
		String command = "SELECT count(m) from Mails m";
		TypedQuery<Long> query = entityManager.createQuery(command, Long.class);
		long count=query.getSingleResult();

		if(count>0) {
		//retrieving the maximum existing beneficiary id and setting its next value to current new beneficiary
		command = "SELECT max(m.id) from Mails m";
		TypedQuery<Integer> query2 = entityManager.createQuery(command, Integer.class);
		int temp=query2.getSingleResult();
		tempId=temp+1;
		}
		return tempId;
	}

	
	/*Method:addMail
	 * Description: Used in feedback, contactUs and reportIssue pages to add a new feedback,mail or complaint
	 * @param Mails: a Mails Object containing all the details of new feedback, mail or complaint
	 * @return boolean: boolean 'true' is returned if mail,feedback or complaint is added successfully else 'false'0
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@Override
	public void addMail(Mails mail) {
		entityManager.persist(mail);
	}

	
	/*Method:getAllMails
	 * Description: Used in admin_Mails page to view all the feedbacks, mails and complaints
	 * @param: none
	 * @return List<Mails>: a List of all the existing feedbacks, mails and complaints is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	@Override
	public List<Mails> getAllMails() {
		String command = "select m from Mails m";
		TypedQuery<Mails>query = entityManager.createQuery(command, Mails.class);
		List<Mails> list=query.getResultList();
		return list;
	}
	
}
