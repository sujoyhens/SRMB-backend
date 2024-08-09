package com.boenci.srmb.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "enterprisemaster")
public class EnterpriseMaster{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enterprisemaster_seq")
	@SequenceGenerator(
		name="enterprisemaster_seq",
		sequenceName="enterprisemaster_sequence",
		allocationSize=1
	)
	@JsonProperty("enterprisemasterid")
    private long enterprisemasterid;

	@JsonProperty("enterprisename")
	private String enterprisename;

	@Column(length = 2000)
	@JsonProperty("address")
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
