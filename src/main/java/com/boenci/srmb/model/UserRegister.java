package com.boenci.srmb.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "userregister")
public class UserRegister{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userregister_seq")
	@SequenceGenerator(
		name="userregister_seq",
		sequenceName="userregister_sequence",
		allocationSize=1
	)
	@JsonProperty("userregisterid")
    private long userregisterid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("rolemasterid")
    private long rolemasterid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("enterprisemasterid")
    private long enterprisemasterid;

	@Transient
	@JsonProperty("enterprisename")
	private String enterprisename;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("sitemasterid")
    private long sitemasterid;

	@Transient
	@JsonProperty("sitename")
	private String sitename;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("plantmasterid")
    private long plantmasterid;

	@Transient
	@JsonProperty("plantname")
	private String plantname;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("unitmasterid")
    private long unitmasterid;

	@Transient
	@JsonProperty("unitname")
	private String unitname;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("areamasterid")
    private long areamasterid;

	@Transient
	@JsonProperty("areaname")
	private String areaname;

	@JsonProperty("employeeno")
	private String employeeno;

	@JsonProperty("username")
	private String username;

	@JsonProperty("useremail")
	private String useremail;

	@JsonProperty("contactemail")
	private String contactemail;	

	@JsonProperty("password")
	private String password;	

	@JsonProperty("phoneumber")
	private String phoneumber;

	@JsonProperty("imagepath")
	private String imagepath;

	@JsonProperty("usermode")
    private String usermode;

	@JsonProperty("unittype")
    private String unittype;

	@JsonProperty("theme")
    private String theme;

	@JsonProperty("division")
    private String division;	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("workingunit1id")
	private long workingunit1id;

	@JsonProperty("workingunit1name")
    private String workingunit1name;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("workingunit2id")
	private long workingunit2id;

	@JsonProperty("workingunit2name")
	private String workingunit2name;

	@JsonProperty("isallunit")
	private String isallunit;

	@JsonProperty("caller")
    private String caller;

	@JsonProperty("status")
	private String status;

	@JsonProperty("deletedflag")
	private String deletedflag;

	@JsonProperty("createdby")
    private String createdby;
	
	@JsonProperty("createdat")
    private String createdat;

    @JsonProperty("updatedat")
    private String updatedat;

	@JsonProperty("updatedby")
    private String updatedby;

	@JsonProperty("action")
    private String action;

	

	
}
