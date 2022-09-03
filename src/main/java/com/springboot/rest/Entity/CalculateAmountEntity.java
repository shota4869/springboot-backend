package com.springboot.rest.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * 
 * @author takaseshota
 *
 */
@Entity
public class CalculateAmountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "balance_date")
	private String balanceDate;

	@Column(name = "balance_flg")
	private String balanceFlg;

	@Column(name = "fix_flg")
	private String fixFlg;

	@Column(name = "amount")
	private int amount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(String date) {
		this.balanceDate = date;
	}

	public String getBalanceFlg() {
		return balanceFlg;
	}

	public void setBalanceFlg(String balanceFlg) {
		this.balanceFlg = balanceFlg;
	}

	public String getFixFlg() {
		return fixFlg;
	}

	public void setFixFlg(String fixFlg) {
		this.fixFlg = fixFlg;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
