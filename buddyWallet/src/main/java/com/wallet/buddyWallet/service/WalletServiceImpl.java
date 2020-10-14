package com.wallet.buddyWallet.service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.wallet.buddyWallet.dao.WalletDao;
import com.wallet.buddyWallet.dao.WalletDaoImpl;
import com.wallet.buddyWallet.enitites.Account;
import com.wallet.buddyWallet.enitites.Beneficiary;
import com.wallet.buddyWallet.enitites.BuddyTransactions;
import com.wallet.buddyWallet.enitites.Mails;
import com.wallet.buddyWallet.exceptions.AccountBlockedException;
import com.wallet.buddyWallet.exceptions.InsufficientBalanceException;
import com.wallet.buddyWallet.exceptions.InvalidBeneficiaryException;
import com.wallet.buddyWallet.exceptions.InvalidLoginCredentialsException;
import com.wallet.buddyWallet.exceptions.InvalidTransactionPasswordException;
import com.wallet.buddyWallet.exceptions.ResourceNotFoundException;
import com.wallet.buddyWallet.exceptions.UnknownErrorException;
import com.wallet.buddyWallet.exceptions.UserNameExistsException;

@Service
@Transactional
public class WalletServiceImpl implements WalletService 
{

	@Autowired
	WalletDao dao=new WalletDaoImpl(); 
	
	SimpleMailMessage mail = new SimpleMailMessage();

	private JavaMailSender javaMailSender;
	
	@Autowired
	public WalletServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	/* Method:getAllUsers
	 * Description: Used in admin page to view all the existing account details
	 * @param: none
	 * @return List<Account>: a List of all the existing accounts
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@Override
	public List<Account> getAllUsers() throws ResourceNotFoundException {
		return dao.getAllUsers();
	}
	
	@Override
	public int verifyEmail(String email) {
		String numbers="0123456789";
		Random randomNumber=new Random();
		String otp="";
		
		for(int i=0;i<6;i++)
			otp+=numbers.charAt(randomNumber.nextInt(numbers.length()));

		mail.setTo(email);
		mail.setSubject("Email Verification Process");
		mail.setText("Welcome to Buddy Family!! \n Here is your One-Time Password to allow this emailid to get linked to Buddy Wallet and further communication. \n Do not share with anyone, OTP = "+otp);
		javaMailSender.send(mail);
		
		return Integer.parseInt(otp);

	}
	/* Method:createAccount
	 * Description: Used in signup page to create a new account
	 * @param Account: an Account object filled with new customer details
	 * @return boolean: if account is added successfully, boolean 'true' is returned else false;
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	 * @throws UserNameExistsException: It is raised when an username exists and new customer wanna use the same    
	*/
	@Override
	public boolean createAccount(Account acnt) throws ResourceNotFoundException,UserNameExistsException,UnknownErrorException
	{
//		when username already exists, to return an exception
		if(dao.findAccount(acnt.getUserName())==true){
			throw new UserNameExistsException("Oops! Username already taken by another user!!");
		}
		else {
		try {
			//for every new account , generating a new account number
			long accNum=dao.generateAcntNumb();
			acnt.setAccNum(accNum);
			acnt.setBalance(0);
			acnt.setTransCount(0);
			acnt.setBlocked(0);
			dao.createAccount(acnt);
			return true;
		}
		catch(Exception e)
		{
			System.out.println(e);
			throw new UnknownErrorException("Can't SignUp at this moment!!\n Due to some Background error.. Try again after sometime!!");
		}
		}
	} 

	
	/* Method:deposit
	 * Description: Used in deposit page to add money into wallet
	 * @param long: Account number into which amount is to be added
	 * @param String: Transaction Password to verify whether transaction done by correct user or not
	 * @param double: Amount to be deposited
	 * @return String: A message of 'amount successfully deposited' is returned if transaction successful
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	 * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
	*/
	@Override
	public String deposit(long accNum,String tranPassword,double amount) throws ResourceNotFoundException,InvalidTransactionPasswordException,UnknownErrorException 
	{
		Account account=dao.getAccount(accNum);
		//when user enter incorrect transaction password, returning an exception
		if(!account.getTranPassword().equals(tranPassword)) {
			throw new InvalidTransactionPasswordException("Incorrect Transaction Password!!");
		}
		
		try {
		account.setTransCount(account.getTransCount()+1);
		account.setBalance(account.getBalance()+amount);
		int id=dao.generateTransactionId();
		account.addTransaction(new BuddyTransactions(id,"Credit",amount,LocalDate.now(),LocalTime.now(),account.getBalance()));
		dao.updateAccount(account);
		return ("Rs. "+amount+"/- has been successfully deposited into your account!! Transaction Id: "+id);
		
		}
		catch(Exception e){
			throw new UnknownErrorException("some Background error...Please Try again after sometime!!");
		}
		
	}


	/* Method:withdraw
	 * Description: Used in withdraw page to withdraw money from wallet
	 * @param long: Account number from which amount is to be withdrawn
	 * @param String: Transaction Password to verify whether transaction done by correct user or not
	 * @param double: Amount to be withdrawn
	 * @return String: A message of 'amount successfully withdrawn' is returned if transaction successful
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	 * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
	 * @throws InsufficientBalanceException: It is raised when user tries to withdraw money more than existing balance in wallet
    */
	@Override
	public String withdraw(long accNum,String tranPassword,double amount) throws ResourceNotFoundException,InvalidTransactionPasswordException,InsufficientBalanceException,UnknownErrorException 
	{
		
		Account account=dao.getAccount(accNum);
		//when user enters an incorrect transaction password, returning an exception
		if(!account.getTranPassword().equals(tranPassword)) {
			throw new InvalidTransactionPasswordException("Incorrect Transaction Password!!");
		}
		//when user wants to withdraw amount more than his existing balance, returnin an exception
		if(account.getBalance()<amount) {
			throw new InsufficientBalanceException("Insufficient Funds in your account!!");
		}
		try {
		account.setBalance(account.getBalance()-amount);
		//For every new transaction, generating an unique id
		int id=dao.generateTransactionId();
		account.addTransaction(new BuddyTransactions(id,"Debit",-amount,LocalDate.now(),LocalTime.now(),account.getBalance()));
		account.setTransCount(account.getTransCount()+1);
		//updating account balance
		dao.updateAccount(account);
		mail.setFrom("BuddyCare");
		mail.setTo(account.getEmailId());
		mail.setSubject("Withdrawal request from your wallet");
		mail.setText("Rs. "+amount+"/- has been withdrawn from your wallet on "+LocalDate.now()+" at "+LocalTime.now());
		javaMailSender.send(mail);

		//adding new transaction
		return ("Rs. "+amount+"/- has been successfully withdrawn from your account !! Transaction Id: "+id);
		
		}
		catch(Exception e){
			throw new UnknownErrorException("some background error...Please Try again after sometime!!");
		}
		
	}

	
	/* Method:fundTransfer
	 * Description: Used in fundTransfer page to transfer money from one to another wallet
	 * @param long: Account number from which amount is to be transferred
	 * @param String: Transaction Password to verify whether transaction done by correct user or not
	 * @param int: BeneficiaryId to get details about beneficiary
	 * @param double: Amount to be withdrawn
	 * @param String: Message- a description about why that transfer is made
	 * @return String: A message of 'amount successfully transferred' is returned if transaction successful
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	 * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
	 * @throws InsufficientBalanceException: It is raised when user tries to transfer money more than existing balance in wallet
	 * @throws InvalidBeneficiaryException: It is raised when user tries to transfer money to an incorrect account Number or beneficiary
	*/
	@Override
	public String fundTransfer(long accNum,String tranPassword,int beneficiaryId,double amount,String message) throws ResourceNotFoundException,InvalidTransactionPasswordException,InsufficientBalanceException,InvalidBeneficiaryException,UnknownErrorException 
	{
		//finding whether any beneficiary exists with that beneficiary id or not
		Beneficiary beneficiary=dao.findBeneficiary(beneficiaryId);
		if(beneficiary==null) {
			throw new InvalidBeneficiaryException("Invalid Beneficiary!!");
		}
		Account account=dao.getAccount(accNum);
		Account bAccount=dao.getAccount(beneficiary.getbAccNum());

		//if no beneficiary exists with that id, throwing an exception
		if(bAccount==null) {
			throw new InvalidBeneficiaryException("Invalid Beneficiary!!");
		}
		account.setTransCount(account.getTransCount()+1);
		bAccount.setTransCount(bAccount.getTransCount()+1);
		
		//if user enters incorrect password, throwing an exception
		if(!account.getTranPassword().equals(tranPassword)) {
			throw new InvalidTransactionPasswordException("Incorrect Transaction Password!!");
		}
		//if user wants to transfer amount that is more than his existing balance, throwing an exception
		if(account.getBalance() < amount)
		{
			throw new InsufficientBalanceException("Due to Insufficient Funds in your account!!");	
		}
		try {
		account.setBalance(account.getBalance()-amount);
		bAccount.setBalance(bAccount.getBalance()+amount);
		int id1=dao.generateTransactionId();
		//adding two new transactions, one as credit in beneficiary account and another as debit in sender account
	
		//if message is entered, saving it in transaction string, else removing message from transaction
		if(!message.equals("NA")) {
			account.addTransaction(new BuddyTransactions(id1,("Transferred to "+beneficiary.getNickName()+"\n ("+message+")"),-amount,LocalDate.now(),LocalTime.now(),account.getBalance()));
			bAccount.addTransaction(new BuddyTransactions(id1+1,("Transferred by "+account.getFirstName()+"\n ("+message+")"),amount,LocalDate.now(),LocalTime.now(),bAccount.getBalance()));
		}
		else {		
			account.addTransaction(new BuddyTransactions(id1,("Transferred to "+beneficiary.getNickName()),-amount,LocalDate.now(),LocalTime.now(),account.getBalance()));
			bAccount.addTransaction(new BuddyTransactions(id1+1,("Transferred by "+account.getFirstName()+" "+account.getLastName()),amount,LocalDate.now(),LocalTime.now(),bAccount.getBalance()));
		}
		//changing balances in both accounts
		dao.updateAccount(account);
		dao.updateAccount(bAccount);
		mail.setTo(account.getEmailId());
		mail.setSubject("Funds transfered from your wallet");
		mail.setText("Rs. "+amount+"/- has been transferred to "+beneficiary.getNickName()+"("+beneficiary.getbAccNum()+") on "+LocalDate.now()+" at "+LocalTime.now());
		javaMailSender.send(mail);

		return ("Rs. "+amount+"/- has been successfully transferred to "+beneficiary.getNickName()+" !! Transaction Id: "+id1);		
		
		}
		catch(Exception e){
			throw new UnknownErrorException("some background error...Please Try again after sometime!!");
		}
	
	}
		
	
	/* Method:printTransactions
	 * Description: Used in e-statement page to view the transaction history from that wallet
	 * @param long: Account number about which transactions need to be returned
	 * @return List<Transactions>: a List of all the transactions made from that wallet
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/	
	@Override
	public List<BuddyTransactions> printTransactions(long accNum) throws ResourceNotFoundException
	{
		//getting the list of all the transactions made by that account number
		return dao.printTransactions(accNum);
	}

	
	/* Method:login
	 * Description: Used in login page to access one's account by logging in
	 * @param String: userName- user name of that account
	 * @param String: password- password of that account
	 * @return long: respective Account Number if credentials are correct so that it is possible for further references
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws InvalidLoginCredentialsException: It is raised when an user enters an incorrect user name or password    
	 * @throws AccountBlockedException: It is raised when an user account is blocked by admin and that user tries to login
	*/
	@Override
	public long login(String userName, String password)throws ResourceNotFoundException,InvalidLoginCredentialsException,AccountBlockedException {
		return dao.login(userName, password);
	}

	
	/* Method:getAccountDetails
	 * Description: Used to know an account details using it's account number
	 * @param long: Account number of the account about which details need to get
	 * @return Account: an Account object containing all the details is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	@Override
	public Account getAccountDetails(long accNum) throws ResourceNotFoundException {
		return dao.getAccount(accNum);
	}

	
	/* Method:updateMyAccount
	 * Description: Used in editProfile page & admin-editAcnt page to update profile details
	 * @param Account: an Account object containing all the new or changed details
	 * @return boolean: boolean 'true' is returned if account is updated successfully else 'false'
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@Override
	public boolean updateMyAccount(Account updatedAccount) throws ResourceNotFoundException {
		try {
			dao.updateAccount(updatedAccount);
			return true;
		}
		catch(Exception e) {
			throw new ResourceNotFoundException();
		}
	}
	
	
	/* Method:getAllBeneficiaries
	 * Description: Used in manageBeneficiary page & fundTransfer page to view all the beneficiaries existing with that account
	 * @param long: Account number of that account whose beneficiaries need to be viewed
	 * @return List<Beneficiary>: a List of all beneficiaries linked to that account is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@Override
	public List<Beneficiary> getAllBeneficiaries(long accNum) throws ResourceNotFoundException {
		return dao.getAllBeneficiaries(accNum);
	}

	
	/* Method:addBeneficiary
	 * Description: Used in addBenficiary page to link a new beneficiary to an account
	 * @param Beneficiary: a Beneficiary Object containing all the details of new beneficiary
	 * @return boolean: boolean 'true' is returned if beneficiary is added successfully else 'false'0
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@Override
	public boolean addBeneficiary(long accNum, Beneficiary beneficiary) throws ResourceNotFoundException,UnknownErrorException{
		Account account=dao.getAccount(accNum);
		try {
			//generating a new unique id for every new beneficiary
			int id=dao.generateBeneficiaryId();
			beneficiary.setId(id);
			account.addBeneficiary(beneficiary);
			return true;
		}
		catch(Exception e){
			throw new UnknownErrorException("some Background error...Please Try again after sometime!!");
		}
	}
	
	
	/* Method:updateBeneficiary
	 * Description: Used in editBenficiary page to edit and update an existing beneficiary linked to that account
	 * @param Beneficiary: a Beneficiary Object containing all the new or changed details of beneficiary
	 * @return boolean: boolean 'true' is returned if beneficiary is updated successfully else 'false'0
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@Override
	public boolean updateBeneficiary(long accNum,int index,Beneficiary beneficiary)throws ResourceNotFoundException,UnknownErrorException {
		try {
			//merging an existing beneficiary with changed details
			Account account=dao.getAccount(accNum);
			account.updateBeneficiary(index,beneficiary);
			return true;
		}
		catch(Exception e){
			throw new UnknownErrorException("some Background error...Please Try again after sometime!!");
		}
	}

	
	/* Method:deleteBeneficiary
	 * Description: Used in manageBenficiary page to delete an existing beneficiary linked to that account
	 * @param int: beneficiaryId which is to be deleted
	 * @return boolean: boolean 'true' is returned if beneficiary is deleted successfully else 'false'0
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@Override
	public boolean deleteBeneficiary(int beneficiaryId) throws ResourceNotFoundException,UnknownErrorException{
		try {
			dao.deleteBeneficiary(beneficiaryId);
			return true;
		}
		catch(Exception e){
			throw new UnknownErrorException("some Background error...Please Try again after sometime!!");
		}
	}
	
	
	/* Method:addMail
	 * Description: Used in feedback, contactUs and reportIssue pages to add a new feedback,mail or complaint
	 * @param Mails: a Mails Object containing all the details of new feedback, mail or complaint
	 * @return boolean: boolean 'true' is returned if mail,feedback or complaint is added successfully else 'false'0
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@Override
	public boolean addMail(Mails mail) throws UnknownErrorException{
		try {
			mail.setMailDate(LocalDate.now());
			mail.setMailTime(LocalTime.now());
			mail.setId(dao.generateMailId());
			dao.addMail(mail);
			return true;
		}
		catch(Exception e) {
			throw new UnknownErrorException();
		}
	}
	
	
	/* Method:getAllMails
	 * Description: Used in admin_Mails page to view all the feedbacks, mails and complaints
	 * @param: none
	 * @return List<Mails>: a List of all the existing feedbacks, mails and complaints is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	@Override
	public List<Mails> getAllMails() throws ResourceNotFoundException {
		return dao.getAllMails();
	}
	
	
	
	
//	@Override
//	public void saveDp(long accNum, byte[] img) throws Exception{
//		Account account=dao.getAccount(accNum);
//		account.setDp(img);
//		dao.update(account);
//	}
}
