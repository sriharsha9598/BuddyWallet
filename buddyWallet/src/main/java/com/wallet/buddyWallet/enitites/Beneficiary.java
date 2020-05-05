package com.wallet.buddyWallet.enitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="Beneficiary")
public class Beneficiary {
		@Id
		@Column(name="id")
		private int id;
		@Column(name="BACCNUM")
		private long bAccNum;
		@NotEmpty
		@Column(name="NAME")
		@Length(max=30)
		private String name;
		@NotEmpty
		@Column(name="NICKNAME")
		@Length(max=15)
		private String nickName;
		@Column(name="CATEGORY")
		@Length(max=15)
		private String category;
		@ManyToOne
		@JoinColumn(name="ACCOUNT")
		public Account account;
		
		public Beneficiary() {
		}		
		
		public Beneficiary(int id, long bAccNum, String name, String nickName, String category) {
			super();
			this.id = id;
			this.bAccNum = bAccNum;
			this.name = name;
			this.nickName = nickName;
			this.category = category;
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public long getbAccNum() {
			return bAccNum;
		}
		public void setbAccNum(long bAccNum) {
			this.bAccNum = bAccNum;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}

		public Account getAccount() {
			return account;
		}

		public void setAccount(Account account) {
			this.account = account;
		}

		@Override
		public String toString() {
			return "Beneficiary [id=" + id + ", bAccNum=" + bAccNum + ", name=" + name + ", nickName=" + nickName
					+ ", category=" + category + ", account=" + account + "]";
		}
				
}
