package com.wallet.buddyWallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.wallet.buddyWallet.enitites.Account;
import com.wallet.buddyWallet.enitites.Beneficiary;
import com.wallet.buddyWallet.enitites.Mails;
import com.wallet.buddyWallet.exceptions.InsufficientBalanceException;
import com.wallet.buddyWallet.exceptions.InvalidBeneficiaryException;
import com.wallet.buddyWallet.exceptions.InvalidLoginCredentialsException;
import com.wallet.buddyWallet.exceptions.InvalidTransactionPasswordException;
import com.wallet.buddyWallet.exceptions.UserNameExistsException;
import com.wallet.buddyWallet.service.WalletServiceImpl;

@SpringBootTest
@Transactional
@Rollback(true)
class BuddyWalletApplicationTests {

	@Autowired
	public WalletServiceImpl service;

	@Test
	void contextLoads() {
	}
	
//	Creating a temporary account to test various operations
	public void createAccount() throws Exception{
		Account acc = new Account(0,"Pravallika",null,"Konduru", 100.0, 789632145, "pravallik@gmail.com", "pravallika98", "Pravallika@98", "Pravallika@1998", "Tenali", 0, 0);
		service.createAccount(acc);
	}
	
// 	Test1: Testing invalidLoginCredentialsException by entering incorrect credentials
	@Test
	public void wrongCredentialLogin() throws Exception {
		createAccount();
		assertThrows(InvalidLoginCredentialsException.class, ()->{
			service.login("pravallika99","pravallika@98");
		});
	}
	
//	Test2: Testing the Login operation using correct credentials
	@Test
	public void login() throws Exception{
		createAccount();
		long accNum=500101002;
		assertEquals(accNum,service.login("pravallika98","Pravallika@98"));
	}
	
//	Test3: Testing getAccountDetails method in service layer by comparing the account details
	@Test
	public void getAccountDetails() throws Exception{
		createAccount();
		Account account=service.getAccountDetails(500101002);
		assertEquals("Pravallika", account.getFirstName());
		assertEquals(null, account.getMiddleName());
		assertEquals("pravallika98", account.getUserName());
		assertEquals("Pravallika@98", account.getPassword());
		assertEquals("Pravallika@1998", account.getTranPassword());
	}

//	Test4: Testing updateMyAccount method in service layer by comparing the updated details
	@Test
	public void updateAccount()throws Exception {
		createAccount();
		service.updateMyAccount(new Account(500101002,"Pravallika","Garu","Konduru", 100.0, 789632145, "pravallik@gmail.com", "pravallika98", "Pravallika@98", "Pravallika@1998", "Tenali", 0, 0));
		assertEquals("Garu", service.getAccountDetails(500101002).getMiddleName());
	}
	
//	Test5: Testing InvalidTransactionPasswordException by entering wrong transaction password
	@Test
	public void wrongTransactionPassword() throws Exception {
		createAccount();
		assertThrows(InvalidTransactionPasswordException.class, ()->{
			service.deposit(500101002,"Pravallika@98",100);
		});
	}
	
//	Test6: Testing InsufficientFundsException by entering amount more than existing balance
	@Test
	public void insufficientFundsException() throws Exception {
		createAccount();
		assertThrows(InsufficientBalanceException.class, ()->{
			service.withdraw(500101002,"Pravallika@1998",1000);
		});
	}
	
//	Test7: Testing InvalidBeneficiaryException by entering incorrect beneficiary account number
	@Test
	public void invalidBeneficiaryException() throws Exception {
		createAccount();
		assertThrows(InvalidBeneficiaryException.class, ()->{
			service.fundTransfer(500101002, "Pravallika@1998", 500, 50, "trial");
		});
	}
	
//	Test8: Testing UserNameExistsException by entering an existing username and creating account
	@Test
	public void usernameExistException()throws Exception{
		Account acc = new Account(0,"Pravallika",null,"Konduru", 100.0, 789632145, "pravallik@gmail.com", "pravallika98", "Pravallika@98", "Pravallika@1998", "Tenali", 0, 0);
		service.createAccount(acc);
		assertThrows(UserNameExistsException.class, ()->{
			service.createAccount(acc);
		});
	}

//	Test9: Testing the deposit operation and comparing the boolean result
//	@Test
//	public void deposit()throws Exception{
//		assertEquals("Rs. 100.0/- has been successfully deposited into your account!! ",service.deposit(500101000, "Harsha@1998", 100));
//	}
//
////	Test10: Testing the withdraw operation and comparing the boolean result
//	@Test
//	public void withdraw()throws Exception{
//		assertEquals("Rs. 50.0/- has been successfully withdrawn from your account !!",service.withdraw(500101000, "Harsha@1998", 50));
//	}
//
////	Test11: Testing the fund transfer operation and comparing the boolean result
//	@Test
//	public void fundTransfer() throws Exception{
//		assertEquals("Rs. 90.0/- has been successfully transferred to kumar !!",service.fundTransfer(500101000, "Harsha@1998", 3300, 90, "NA"));
//	}
	
//	Test12: Testing the add beneficiary operation and comparing the boolean result
	@Test
	public void addBeneficiary()throws Exception{
		Beneficiary beneficiary=new Beneficiary(0, 500101001, "P. Sai Kumar", "kumar", "Family");
		assertEquals(true, service.addBeneficiary(500101000,beneficiary));
	}
	
//	Test13: Testing the update beneficiary operation and comparing the boolean result
	@Test
	public void updateBeneficiary()throws Exception{
		Beneficiary beneficiary=new Beneficiary(0, 500101001, "P. Sai Kumar", "kumar", "Family");
		service.addBeneficiary(500101000, beneficiary);
		List<Beneficiary> beneficiaries= service.getAllBeneficiaries(500101000);
		beneficiary.setName("kumar123");
		assertEquals(true,service.updateBeneficiary(500101000,beneficiaries.size()-1,beneficiary));
	}
	
//	Test14: Testing the delete beneficiary operation and comparing the boolean result
	@Test
	public void deleteBeneficiary()throws Exception{
		Beneficiary beneficiary=new Beneficiary(0, 500101001, "P. Sai Kumar", "kumar", "Family");
		service.addBeneficiary(500101000, beneficiary);
		List<Beneficiary> beneficiaries= service.getAllBeneficiaries(500101000);
		assertEquals(true,service.deleteBeneficiary(beneficiaries.get(beneficiaries.size()-1).getId()));
	}

//	Test15: Testing the add mail operation and comparing the boolean result
	@Test
	public void addMail()throws Exception{
		Mails mail=new Mails(0, "Harsha", "feedback", "4-star", "Good!", LocalDate.now(), LocalTime.now());
		assertEquals(true,service.addMail(mail));
	}
	
	
}	

