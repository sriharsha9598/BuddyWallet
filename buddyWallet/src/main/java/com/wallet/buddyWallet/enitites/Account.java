package com.wallet.buddyWallet.enitites;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
	@JsonIgnoreProperties({"hibenateLazyInitializer","handler"})
    @Entity
	@Table(name="Account")
	public class Account{
		
		@Id
		@Column(name="ACCNUM")
		private long accNum;
		@NotEmpty
		@Column(name="FIRSTNAME")
		@Length(max=15)
		private String firstName;
		@Column(name="MIDDLENAME")
		@Length(max=15)
		private String middleName;
		@NotEmpty
		@Column(name="LASTNAME")
		@Length(max=15)
		private String lastName;
		@Column(name="BALANCE")
		private double balance;
		@Column(name="PHONENUM")
		private long phoneNum;
		@NotEmpty
		@Column(name="EMAILID")
		@Length(max=30)
		private String emailId;
		@NotEmpty
		@Column(name="USERNAME")
		@Length(max=15)
		private String userName;
		@NotEmpty
		@Column(name="PASSWORD")
		@Length(max=15)
		private String password;
		@NotEmpty
		@Column(name="TRANPASSWORD")
		@Length(max=15)
		private String tranPassword;
		@NotEmpty
		@Column(name="CITY")
		@Length(max=15)
		private String city;
		@Column(name="TRANSCOUNT")
		private int transCount;
		@Column(name="blocked")
		private int blocked;
		@JsonBackReference(value="transactions")
		@OneToMany(mappedBy="account",cascade=CascadeType.ALL)
		public List<Transactions> transactions;
		@JsonBackReference(value="beneficiaries")
		@OneToMany(mappedBy="account",cascade=CascadeType.ALL)
		public List<Beneficiary> beneficiaries;

		public Account() {}

		public Account(long accNum, String firstName, String middleName, String lastName, double balance, long phoneNum,
				String emailId, String userName, String password, String tranPassword, String city, int transCount,
				int blocked) {
			super();
			this.accNum = accNum;
			this.firstName = firstName;
			this.middleName = middleName;
			this.lastName = lastName;
			this.balance = balance;
			this.phoneNum = phoneNum;
			this.emailId = emailId;
			this.userName = userName;
			this.password = password;
			this.tranPassword = tranPassword;
			this.city = city;
			this.transCount = transCount;
			this.blocked = blocked;
		}

		public long getAccNum() {
			return accNum;
		}

		public void setAccNum(long accNum) {
			this.accNum = accNum;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getMiddleName() {
			return middleName;
		}

		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public double getBalance() {
			return balance;
		}

		public void setBalance(double balance) {
			this.balance = balance;
		}

		public long getPhoneNum() {
			return phoneNum;
		}

		public void setPhoneNum(long phoneNum) {
			this.phoneNum = phoneNum;
		}

		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getTranPassword() {
			return tranPassword;
		}

		public void setTranPassword(String tranPassword) {
			this.tranPassword = tranPassword;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public int getTransCount() {
			return transCount;
		}

		public void setTransCount(int transCount) {
			this.transCount = transCount;
		}

		public int getBlocked() {
			return blocked;
		}

		public void setBlocked(int blocked) {
			this.blocked = blocked;
		}

		public List<Transactions> getTransactions() {
			return transactions;
		}

		public void setTransactions(List<Transactions> transactions) {
			this.transactions = transactions;
		}

		public void addTransaction(Transactions transaction) {
			transaction.setAccount(this);
			this.getTransactions().add(transaction);
		}
		
		public List<Beneficiary> getBeneficiaries() {
			return beneficiaries;
		}

		public void setBeneficiaries(List<Beneficiary> beneficiaries) {
			this.beneficiaries = beneficiaries;
		}

		public void addBeneficiary(Beneficiary beneficiary) {
			beneficiary.setAccount(this);
			this.getBeneficiaries().add(beneficiary);
		}

		public void updateBeneficiary(int index, Beneficiary beneficiary) {
			this.getBeneficiaries().get(index).setName(beneficiary.getName());
			this.getBeneficiaries().get(index).setNickName(beneficiary.getNickName());
			this.getBeneficiaries().get(index).setCategory(beneficiary.getCategory());
			this.getBeneficiaries().get(index).setbAccNum(beneficiary.getbAccNum());
			this.getBeneficiaries().get(index).setAccount(this);
		}

		@Override
		public String toString() {
			return "Account [accNum=" + accNum + ", firstName=" + firstName + ", middleName=" + middleName
					+ ", lastName=" + lastName + ", balance=" + balance + ", phoneNum=" + phoneNum + ", emailId="
					+ emailId + ", userName=" + userName + ", password=" + password + ", tranPassword=" + tranPassword
					+ ", city=" + city + ", transCount=" + transCount + ", blocked=" + blocked + ", transactions="
					+ transactions + "]";
		}
		
							
}