package com.myapi.apiservice.entity.myconnb;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="MEMB_USER")
public class MembUser implements Serializable {
	private static final long serialVersionUID = 908184875955715426L;
	
	@Id
	@Column(name="USER_ID")
	private String userId;

	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="ADDRESS")
	private String address;

	@Column(name="EMAIL")
	private String email;

	@Column(name="TEL")
	private String tel;

	@Column(name="MOBILE")
	private String mobile;

	@Column(name="STATUS")
	private String status;
}