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
@Table(name = "sitemaster")
public class SiteMaster{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sitemaster_seq")
	@SequenceGenerator(
		name="sitemaster_seq",
		sequenceName="sitemaster_sequence",
		allocationSize=1
	)
	@JsonProperty("sitemasterid")
    private long sitemasterid;

	@JsonProperty("enterprisemasterid")
    private long enterprisemasterid;

	@Transient
    @JsonProperty("enterprisename")
	private String enterprisename;

	@JsonProperty("sitename")
	private String sitename;

	@JsonProperty("address")
	@Column(length = 2000)
	private String address;

	@Column(length = 5000)
	@JsonProperty("description")
	private String description;

	@JsonProperty("contactperson")
	private String contactperson;

	@JsonProperty("contactnumber")
	private String contactnumber;

	@JsonProperty("contactemail")
	private String contactemail;

	@JsonProperty("licenseno")
	@Column(columnDefinition = "integer default 0")
    private long licenseno;
		
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
