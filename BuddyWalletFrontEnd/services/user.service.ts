import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { eStatement } from '../models/transactionsDB';
import { Account } from '../models/walletDB';
import { Beneficiary } from '../models/beneficiaryDB';
import { Mails } from '../models/MailDB';
import { Text } from '@angular/compiler/src/i18n/i18n_ast';

@Injectable({
	providedIn: 'root'
})

export class UserService {


	// Stores the account number on login and used for all the further calls
	static myAccountNumber: number = 0;


	//Used to store the beneficiary details that is to be edited ...stored from manage beneficiary page and retrieved in edit beneficiary to set the values
	static beneficiary: Beneficiary;
	static tempBeneficiaryIndex: number;


	constructor(private http: HttpClient) {

	}


	/* Method:getAllUsers
	   * Type: GetMapping
	   * Description: this http request is called from admin page to view all the existing account details
	   * @param: none
	   * @return List<Account>: a List of all the existing accounts
	   * @throws ResourceNotFoundException: It is raised when no data found
	*/
	getAllUsers() {
		return this.http.get<Account[]>("http://localhost:8094/api/getAllUsers");
	}


	/* Method:login
	   * Description: this http request is called from login page to access one's account by logging in
	   * Type: GetMapping
	   * @param String: userName- user name of that account
	   * @param String: password- password of that account
	   * @return long: respective Account Number if credentials are correct so that it is possible for further references
	   * @throws InvalidLoginCredentialsException: It is raised when an user enters an incorrect user name or password    
	   * @throws AccountBlockedException: It is raised when an user account is blocked by admin and that user tries to login
	  */
	login(userName: string, password: string) {
		return this.http.get<number>("http://localhost:8094/api/login/" + userName + "/" + password);
	}


	/* Method:getAccountDetails
	 * Type: GetMapping
	 * Description: this http request is used to know the account details using it's account number
	 * @return Account: an Account object containing all the details is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	getAccountDetails() {
		return this.http.get<Account>("http://localhost:8094/api/getAccountDetails/" + UserService.myAccountNumber)
	}


	/* Method:deposit
	   * Type: PutMapping
	   * Description: this http request is called from deposit page to add money into wallet
	   * @param String: Transaction Password to verify whether transaction done by correct user or not
	   * @param double: Amount to be deposited
	   * @return String: A message of 'amount successfully deposited' is returned if transaction successful
	   * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
	  */
	deposit(tranPassword: string, amount: number) {
		return this.http.put("http://localhost:8094/api/deposit/" + UserService.myAccountNumber + "/" + tranPassword, amount,{responseType:'text'});
	}


	/* Method:withdraw
   * Type:PutMapping
   * Description: this http request is called from withdraw page to withdraw money from wallet
   * @param String: Transaction Password to verify whether transaction done by correct user or not
   * @param double: Amount to be withdrawn
   * @return String: A message of 'amount successfully withdrawn' is returned if transaction successful
   * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
   * @throws InsufficientBalanceException: It is raised when user tries to withdraw money more than existing balance in wallet
	*/
	withdraw(tranPassword: string, amount: number) {
		return this.http.put("http://localhost:8094/api/withdraw/" + UserService.myAccountNumber + "/" + tranPassword, amount,{responseType:'text'});
	}


	/*Method:fundTransfer
	   * Type: PutMapping
	   * Description: this http request is called from fundTransfer page to transfer money from one to another wallet
	   * @param String: Transaction Password to verify whether transaction done by correct user or not
	   * @param int: BeneficiaryId to get details about beneficiary
	   * @param double: Amount to be withdrawn
	   * @param String: Message- a description about why that transfer is made
	   * @return String: A message of 'amount successfully transferred' is returned if transaction successful
	   * @throws InvalidTransactionPasswordException: It is raised when an user enters an incorrect Transaction Password    
	   * @throws InsufficientBalanceException: It is raised when user tries to transfer money more than existing balance in wallet
	   * @throws InvalidBeneficiaryException: It is raised when user tries to transfer money to an incorrect account Number or beneficiary
	  */
	fundTransfer(tranPassword: string, beneficiaryId, amount: number, message: string) {
		return this.http.put("http://localhost:8094/api/fundTransfer/" + UserService.myAccountNumber + "/" + tranPassword + "/" + beneficiaryId + "/" + amount, message ,{responseType:'text'});
	}


	/*Method:eStatement
	   * Type: GetMapping
	   * Description: this http request is called from e-statement page to view the transaction history from that wallet
	   * @param long: Account number about which transactions need to be returned
	   * @return List<Transactions>: a List of all the transactions made from that wallet
	   * @throws ResourceNotFoundException: It is raised when no data found
	  */
	eStatement() {
		return this.http.get<eStatement[]>("http://localhost:8094/api/eStatement/" + UserService.myAccountNumber);
	}


	/* Method:signUp
	   * Type: PostMapping
	   * Description: this http request is called from signup page to create a new account
	   * @param Account: two Account objects filled with new customer details one with personal details and other with username and password details
	   * @return boolean: if account is added successfully, boolean 'true' is returned else false;
	   * @throws UserNameExistsException: It is raised when an username exists and new customer wanna use the same    
	*/
	signUp(acntForm1: Account, acntForm2: Account) {
		acntForm1.userName = acntForm2.userName;
		acntForm1.password = acntForm2.password;
		acntForm1.tranPassword = acntForm2.tranPassword;
		return this.http.post("http://localhost:8094/api/signUp", acntForm1);
	}


	/*Method:updateMyAccount
	   * Type: PutMapping
	   * Description: this http request is called from editProfile page & admin-editAcnt page to update profile details
	   * @param Account: an Account object containing all the new or changed details
	   * @return boolean: boolean 'true' is returned if account is updated successfully else 'false'
	 */
	updateMyAccount(updatedAccount: Account) {
		return this.http.put("http://localhost:8094/api/updateMyAccount", updatedAccount);
	}


	/* Method:addBeneficiary
	 * Type: PostMapping
	 * Description: this http request is called from addBenficiary page to link a new beneficiary to an account
	 * @param Beneficiary: a Beneficiary Object containing all the details of new beneficiary
	 * @return boolean: boolean 'true' is returned if beneficiary is added successfully else 'false'0
	*/
	addBeneficiary(beneficiary: Beneficiary) {
		beneficiary.id = 0;
		return this.http.post("http://localhost:8094/api/addBeneficiary/" + UserService.myAccountNumber, beneficiary);
	}


	/* Method:updateBeneficiary
	   * Type: PostMapping
	   * Description: this http request is called from editBenficiary page to edit and update an existing beneficiary linked to that account
	   * @param Beneficiary: a Beneficiary Object containing all the new or changed details of beneficiary
	   * @return boolean: boolean 'true' is returned if beneficiary is updated successfully else 'false'0
	  */
	editBeneficiary(beneficiary: Beneficiary) {
		return this.http.put("http://localhost:8094/api/updateBeneficiary/" + UserService.myAccountNumber + "/" + UserService.tempBeneficiaryIndex, beneficiary);
	}


	/* Method:deleteBeneficiary
   * Type:DeleteMapping
   * Description: this http request is called from manageBenficiary page to delete an existing beneficiary linked to that account
   * @return boolean: boolean 'true' is returned if beneficiary is deleted successfully else 'false'0
  */
	deleteBeneficiary() {
		return this.http.delete("http://localhost:8094/api/deleteBeneficiary/" + UserService.beneficiary.id);
	}


	/* Method:getAllBeneficiaries
	   * Type: GetMapping
	   * Description: this http request is called from manageBeneficiary page & fundTransfer page to view all the beneficiaries existing with that account
	   * @return List<Beneficiary>: a List of all beneficiaries linked to that account is returned
	   * @throws ResourceNotFoundException: It is raised when no data found
	*/
	getAllBeneficiaries() {
		return this.http.get<Beneficiary[]>("http://localhost:8094/api/getAllBeneficiaries/" + UserService.myAccountNumber);
	}


	/* Method:addMail
	   * Type:PostMapping
	   * Description: this http request is called from feedback, contactUs and reportIssue pages to add a new feedback,mail or complaint
	   * @param Mails: a Mails Object containing all the details of new feedback, mail or complaint
	   * @param string: Category to which this mail belongs to
	   * @return boolean: boolean 'true' is returned if mail,feedback or complaint is added successfully else 'false'0
	  */
	addMail(mail: Mails, category: string) {
		mail.category = category;
		mail.id = 0;
		if (category == "feedback")
			mail.name = UserService.myAccountNumber.toString();
		return this.http.post("http://localhost:8094/api/mails/add", mail);
	}



	/* Method:getAllMails
	 * Type: GetMapping
	 * Description: this http request is called from admin_Mails page to view all the feedbacks, mails and complaints
	 * @return List<Mails>: a List of all the existing feedbacks, mails and complaints is returned
	 * @throws ResourceNotFoundException: It is raised when no data found
	*/
	getAllMails() {
		return this.http.get<Mails[]>("http://localhost:8094/api/mails/getAll");
	}





	//This is used to set profile picture
	upload(dp: File) {
		return this.http.post("http://localhost:8094/api/uploadImage", dp)
	}

}

