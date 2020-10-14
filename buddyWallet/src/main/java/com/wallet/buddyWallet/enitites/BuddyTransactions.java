package com.wallet.buddyWallet.enitites;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="BuddyTransactions")
public class BuddyTransactions{
	
	@Id
	@Column(name="ID")
	private int id;
	@NotEmpty
	@Column(name="TRANSACTION")
	@Length(max=50)
	private String transaction;
	@Column(name="AMOUNT")
	private double amount;
	@Column(name="TRANSDATE")
	private LocalDate transDate;
	@Column(name="TRANSTIME")
	private LocalTime transTime;
	@Column(name="BALANCE")
	private double balance;
	@ManyToOne
	@JoinColumn(name="ACCOUNT")
	public Account account;
	
	public BuddyTransactions() {	}

	public BuddyTransactions(int id, String transaction, double amount, LocalDate transDate, LocalTime transTime,
			double balance) {
		super();
		this.id = id;
		this.transaction = transaction;
		this.amount = amount;
		this.transDate = transDate;
		this.transTime = transTime;
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getTransDate() {
		return transDate;
	}

	public void setTransDate(LocalDate transDate) {
		this.transDate = transDate;
	}

	public LocalTime getTransTime() {
		return transTime;
	}

	public void setTransTime(LocalTime transTime) {
		this.transTime = transTime;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Transactions [id=" + id + ", transaction=" + transaction + ", amount=" + amount + ", transDate="
				+ transDate + ", transTime=" + transTime + ", balance=" + balance + ", account=" + account + "]";
	}

}
