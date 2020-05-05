package com.wallet.buddyWallet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.wallet.buddyWallet.service.WalletServiceImpl;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class WalletController{
	
	@Autowired
	WalletServiceImpl service;
	
	
	/* Method:getAllUsers
	 * Type: GetMapping
	 * Description: Called from admin page to view all the existing account details
	 * @param: none
	 * @return List<Account>: a List of all the existing accounts
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@GetMapping("/getAllUsers")
	public List<Account> getAllUsers() throws ResourceNotFoundException
	{
		return service.getAllUsers();
	}
	
	
	/* Method:login
	 * Description: Called from login page to access one's account by logging in
	 * Type: GetMapping
	 * @param String: userName- user name of that account
	 * @param String: password- password of that account
	 * @return long: respective Account Number if credentials are correct so that it is possible for further references
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws InvalidLoginCredentialsException: It is raised when an user enters an incorrect user name or password    
	 * @throws AccountBlockedException: It is raised when an user account is blocked by admin and that user tries to login
	*/
	@GetMapping("/login/{userName}/{password}")
	public long login(@PathVariable String userName,@PathVariable String password) throws AccountBlockedException,ResourceNotFoundException,InvalidLoginCredentialsException
	{
		return service.login(userName, password);
	}

	
	/* Method:getAccountDetails
	 * Type: GetMapping
	 * Description: Used to know an account details using it's account number
	 * @param long: Account number of the account about which details need to get
	 * @return Account: an Account object containing all the details is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	@GetMapping("/getAccountDetails/{accNum}")
	public Account getAccountDetails(@PathVariable long accNum) throws ResourceNotFoundException
	{
		return service.getAccountDetails(accNum);
	}

	
	/* Method:deposit
	 * Type: PutMapping
	 * Description: Called from deposit page to add money into wallet
	 * @param long: Account number into which amount is to be added
	 * @param String: Transaction Password to verify whether transaction done by correct user or not
	 * @param double: Amount to be deposited
	 * @return String: A message of 'amount successfully deposited' is returned if transaction successful
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	 * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
	*/
	@PutMapping("/deposit/{accNum}/{tranPassword}")
	public String deposit(@PathVariable long accNum,@PathVariable String tranPassword,@RequestBody double amount) throws ResourceNotFoundException,InvalidTransactionPasswordException,UnknownErrorException
	{
		return service.deposit(accNum,tranPassword,amount);
	}
	
	
	/* Method:withdraw
	 * Type:PutMapping
	 * Description: Called from withdraw page to withdraw money from wallet
	 * @param long: Account number from which amount is to be withdrawn
	 * @param String: Transaction Password to verify whether transaction done by correct user or not
	 * @param double: Amount to be withdrawn
	 * @return String: A message of 'amount successfully withdrawn' is returned if transaction successful
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	 * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
	 * @throws InsufficientBalanceException: It is raised when user tries to withdraw money more than existing balance in wallet
    */
	@PutMapping("/withdraw/{accNum}/{tranPassword}")
	public String withdraw(@PathVariable long accNum,@PathVariable String tranPassword,@RequestBody double amount) throws ResourceNotFoundException,InsufficientBalanceException,InvalidTransactionPasswordException,UnknownErrorException
	{
		return service.withdraw(accNum,tranPassword,amount);
	}

	
	/*Method:fundTransfer
	 * Type: PutMapping
	 * Description: Called from fundTransfer page to transfer money from one to another wallet
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
	@PutMapping("/fundTransfer/{accNum}/{tranPassword}/{beneficiaryId}/{amount}")
	public String fundTransfer(@PathVariable long accNum,@PathVariable String tranPassword,@PathVariable int beneficiaryId,@PathVariable double amount,@RequestBody String message) throws ResourceNotFoundException,InvalidTransactionPasswordException,InsufficientBalanceException,InvalidBeneficiaryException,UnknownErrorException
	{
		return service.fundTransfer(accNum,tranPassword, beneficiaryId, amount, message);
	}
	
	
	/*Method:eStatement
	 * Type: GetMapping
	 * Description: Called from e-statement page to view the transaction history from that wallet
	 * @param long: Account number about which transactions need to be returned
	 * @return List<Transactions>: a List of all the transactions made from that wallet
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	@GetMapping("/eStatement/{accNum}")
	public List<Transactions> eStatement(@PathVariable long accNum) throws ResourceNotFoundException
	{
		return service.printTransactions(accNum);
	}
	
	
	/*Method:updateMyAccount
	 * Type: PutMapping
	 * Description: Used in editProfile page & admin-editAcnt page to update profile details
	 * @param Account: an Account object containing all the new or changed details
	 * @return boolean: boolean 'true' is returned if account is updated successfully else 'false'
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@PutMapping("/updateMyAccount")
	public boolean updateMyAccount(@RequestBody Account updatedAccount) throws ResourceNotFoundException {
		return service.updateMyAccount(updatedAccount);
	}
	
	
	/* Method:signUp
	 * Type: PostMapping
	 * Description: Called from signup page to create a new account
	 * @param Account: an Account object filled with new customer details
	 * @return boolean: if account is added successfully, boolean 'true' is returned else false;
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	 * @throws UserNameExistsException: It is raised when an username exists and new customer wanna use the same    
	*/
	@PostMapping("/signUp")
	public boolean signUp(@RequestBody Account acnt) throws UserNameExistsException,UnknownErrorException,ResourceNotFoundException
	{
		return service.createAccount(acnt);
	}
	
	
	/* Method:addBeneficiary
	 * Type: PostMapping
	 * Description: Called from addBenficiary page to link a new beneficiary to an account
	 * @param Beneficiary: a Beneficiary Object containing all the details of new beneficiary
	 * @return boolean: boolean 'true' is returned if beneficiary is added successfully else 'false'0
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@PostMapping("/addBeneficiary/{accNum}")
	public boolean addBeneficiary(@PathVariable long accNum, @RequestBody Beneficiary beneficiary) throws ResourceNotFoundException,UnknownErrorException {
		return service.addBeneficiary(accNum, beneficiary);
	}

	
	/* Method:updateBeneficiary
	 * Type: PostMapping
	 * Description: Called from editBenficiary page to edit and update an existing beneficiary linked to that account
	 * @param Beneficiary: a Beneficiary Object containing all the new or changed details of beneficiary
	 * @return boolean: boolean 'true' is returned if beneficiary is updated successfully else 'false'0
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@PutMapping("/updateBeneficiary/{accNum}/{index}") 
	public boolean updateBeneficiary(@PathVariable long accNum,@PathVariable int index, @RequestBody Beneficiary beneficiary) throws ResourceNotFoundException,UnknownErrorException {
		return service.updateBeneficiary(accNum,index,beneficiary);
	}
	
	
	/* Method:getAllBeneficiaries
	 * Type: GetMapping
	 * Description: Called from manageBeneficiary page & fundTransfer page to view all the beneficiaries existing with that account
	 * @param long: Account number of that account whose beneficiaries need to be viewed
	 * @return List<Beneficiary>: a List of all beneficiaries linked to that account is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
    */
	@GetMapping("/getAllBeneficiaries/{accNum}")
	public List<Beneficiary> getAllBeneficiaries(@PathVariable long accNum) throws ResourceNotFoundException {
		return service.getAllBeneficiaries(accNum);
	}
	
	
	/* Method:deleteBeneficiary
	 * Type:DeleteMapping
	 * Description: Called from manageBenficiary page to delete an existing beneficiary linked to that account
	 * @param int: beneficiaryId which is to be deleted
	 * @return boolean: boolean 'true' is returned if beneficiary is deleted successfully else 'false'0
	 * @throws ResourceNotFoundException: It is raised when no data found
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@DeleteMapping("/deleteBeneficiary/{beneficiaryId}")
	public boolean deleteBeneficiary(@PathVariable int beneficiaryId) throws ResourceNotFoundException,UnknownErrorException{
		return service.deleteBeneficiary(beneficiaryId);
	}
	
	
	/* Method:addMail
	 * Type:PostMapping
	 * Description: Used in feedback, contactUs and reportIssue pages to add a new feedback,mail or complaint
	 * @param Mails: a Mails Object containing all the details of new feedback, mail or complaint
	 * @return boolean: boolean 'true' is returned if mail,feedback or complaint is added successfully else 'false'0
	 * @throws UnknownErrorException: It is raised when some unexpected error or exception occurs during the process
	*/
	@PostMapping("/mails/add")
	public boolean addMail(@RequestBody Mails mail)throws UnknownErrorException {
		return service.addMail(mail);
	}
	
	
	/* Method:getAllMails
	 * Type: GetMapping
	 * Description: Used in admin_Mails page to view all the feedbacks, mails and complaints
	 * @param: none
	 * @return List<Mails>: a List of all the existing feedbacks, mails and complaints is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	@GetMapping("mails/getAll")
	public List<Mails> getAllMails() throws ResourceNotFoundException{
		return service.getAllMails();
	}
	
	
	
	//This is for setting profile picture
//	@PostMapping("/uploadDp/accNum")
//	public MultipartFile uplaodImage(@RequestParam("imageFile") MultipartFile file, @PathVariable long accNum) throws IOException {
//
//		byte[] img=file.getBytes();
//		try
//		{
//			service.saveDp(accNum,img);
//			System.out.println("done");
//			return file;
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			System.out.println("failed");
//			return file;
//		}
//	}
}