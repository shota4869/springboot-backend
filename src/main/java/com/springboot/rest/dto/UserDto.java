package com.springboot.rest.dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * User dto.
 * 
 * @author takaseshota
 */
public class UserDto implements Serializable {

	private long id;

	private String username;

	private String email;

	private String password;

	private Date create;

	private Date update;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getUpdate() {
		return update;
	}

	public void setUpdate(Date update) {
		this.update = update;
	}

}
