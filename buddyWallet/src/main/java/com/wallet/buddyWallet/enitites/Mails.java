package com.wallet.buddyWallet.enitites;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="Mails")
public class Mails {

	@Id
	@Column(name="id")
	private int id;
	@NotEmpty
	@Column(name="NAME")
	@Length(max=25)
	private String name;
	@NotEmpty
	@Column(name="CATEGORY")
	@Length(max=10)
	private String category;
	@NotEmpty
	@Column(name="SUBJECT")
	@Length(max=50)
	private String subject;
	@NotEmpty
	@Column(name="MESSAGE")
	@Length(max=250)
	private String message;
	@Column(name="MAILDATE")
	private LocalDate mailDate;
	@Column(name="MAILTIME")
	private LocalTime mailTime;
	
	public Mails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Mails(int id, String name, String category, String subject, String message, LocalDate mailDate,
			LocalTime mailTime) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.subject = subject;
		this.message = message;
		this.mailDate = mailDate;
		this.mailTime = mailTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDate getMailDate() {
		return mailDate;
	}

	public void setMailDate(LocalDate mailDate) {
		this.mailDate = mailDate;
	}

	public LocalTime getMailTime() {
		return mailTime;
	}

	public void setMailTime(LocalTime mailTime) {
		this.mailTime = mailTime;
	}

	@Override
	public String toString() {
		return "Mails [id=" + id + ", name=" + name + ", category=" + category + ", subject=" + subject + ", message="
				+ message + ", mailDate=" + mailDate + ", mailTime=" + mailTime + "]";
	}
	

}
