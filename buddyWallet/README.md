##Please run the below DDL query before you execute this application.  

CREATE TABLE Account(accNum number primary key,
firstName varchar2(15) not null,
middleName varchar2(15),
lastName varchar2(15) not null,
balance number not null,
phoneNum number,
emailId varchar2(30) not null,
userName varchar2(15) not null,
password varchar2(15) not null,
tranPassword varchar2(15) not null,
city varchar2(20),
transCount number);

CREATE TABLE BuddyTransactions(id number primary key,
transDate date not null,
transaction varchar2(50) not null,
amount number not null,
balance number not null);

CREATE TABLE Beneficiary(id number primary key,
name varchar2(30) not null,
bAccNum number not null,
nickName varchar2(20) not null,
category varchar2(20));

CREATE TABLE Mails(id number primary key,
name varchar2(25) not null,
category varchar2(10) not null,
subject varchar2(50) not null,
message varchar2(250) not null,
mailDate date not null,
mailTime date not null);



INSERT into Account values(500101000,'Sri Harsha','','Pasupuleti',500,9177424002,'sriharsha@gmail.com','harsha98','Harsha@98','Harsha@1998','Tirupati',0);

INSERT into Transactions values(1100,null,500101000,'Account Created!',0,0,0);
INSERT into Beneficiary values(3300,500101000,'P. Sai Kumar',500101001,'Kumar','Family');
INSERT into Mails values(99,'Sri Harsha','complaint','Refund Issue','Hi, I have made a transaction which is failed but money was debited from my account..when will I get my refund back??', DATE'2020-04-25',null);
INSERT into Mails values(101,'Sai Kumar','helpMail','Location Information','Hi, I liked your web site and wanna open account but before may i know where is your branch or head quarters located at??', DATE'2020-04-26,'11:15:51');
INSERT into Mails values(102,'500101001','feedback','5-star','Gooood!!', DATE'2020-04-28','12:01:14');

commit;