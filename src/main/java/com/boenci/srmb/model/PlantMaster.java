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
@Table(name = "plantmaster")
public class PlantMaster{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plantmaster_seq")
	@SequenceGenerator(
		name="plantmaster_seq",
		sequenceName="plantmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("plantmasterid")
    private long plantmasterid;

	@JsonProperty("enterprisemasterid")
	@Column(columnDefinition = "integer default 0")
    private long enterprisemasterid;

	@JsonProperty("sitemasterid")
	@Column(columnDefinition = "integer default 0")
    private long sitemasterid;

	@Transient
    @JsonProperty("enterprisename")
	private String enterprisename;

	@Transient
	@JsonProperty("sitename")
	private String sitename;

	@JsonProperty("plantname")
	private String plantname;

	@JsonProperty("address")
	@Column(length = 2000)
	private String address;

	@Column(length = 2000)
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
