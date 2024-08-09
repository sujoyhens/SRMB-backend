package com.boenci.srmb.model.production;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import com.boenci.srmb.model.SrmbCommon;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "productionlog")
public class ProductionLog extends SrmbCommon{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productionlog_seq")
	@SequenceGenerator(
		name="productionlog_seq",
		sequenceName="productionlog_sequence",
		allocationSize=1
	)
	@JsonProperty("productionlogid")
    private long productionlogid;

	@JsonProperty("userregisterid")
    private long userregisterid;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("unitmasterid")
	private long unitmasterid;	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("areamasterid")
	private long areamasterid;	
	
	@Transient
	@JsonProperty("unitname")
	private String unitname;

	@Transient
	@JsonProperty("areaname")
	private String areaname;

	@JsonProperty("smsname")
	private String smsname;

	@JsonProperty("laboratoryname")
	private String laboratoryname;

	@JsonProperty("ccmname")
	private String ccmname;

	@JsonProperty("rmname")
	private String rmname;

	@Transient
	@JsonProperty("starndoperatorname1")
	private String starndoperatorname1;

	@Transient
	@JsonProperty("starndoperatorname2")
	private String starndoperatorname2;

	@Transient
	@JsonProperty("starndoperatorname3")
	private String starndoperatorname3;

	@JsonProperty("productionlogdate")
	private String productionlogdate;

	@JsonProperty("productionlogtime")
	private String productionlogtime;


	@Column(columnDefinition = "integer default 0")
	@JsonProperty("purgingtemperature")
	private long purgingtemperature;

    @JsonProperty("sequenceno")
	private String sequenceno;

	@JsonProperty("starttime")
	private String starttime;	

    @JsonProperty("fctappingtime")
	private String fctappingtime;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("fcladletemperature")
	private long fcladletemperature;

    @JsonProperty("ladleno")
	private String ladleno;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("ladletemperature")
	private long ladletemperature;

    @JsonProperty("ladleopentime")
	private String ladleopentime; 
	
	@JsonProperty("ladleclosetime")
	private String ladleclosetime;

	@JsonProperty("ladlelife")
	private String ladlelife;

    @JsonProperty("ladletipsused")
	private String ladletipsused;
    
	@Column(columnDefinition = "integer default 0")
    @JsonProperty("starndmoldoperatortime1")
	private long starndmoldoperatortime1;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("starndoperatorid1")
	private long starndoperatorid1;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("starndmoldoperatortime2")
	private long starndmoldoperatortime2;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("starndoperatorid2")
	private long starndoperatorid2;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("starndmoldoperatortime3")
	private long starndmoldoperatortime3;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("starndoperatorid3")
	private long starndoperatorid3;

    @JsonProperty("lancingpipeused")
	private String lancingpipeused;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("tundishtemperature")
	private long tundishtemperature;

    @JsonProperty("purgingtime")
	private String purgingtime;

    @JsonProperty("remarks")
    @Column(length = 5000)
	private String remarks;

	@JsonProperty("productionid")
	private String productionid;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("ismillproductionupdated")
	private String ismillproductionupdated;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("breakout")
	private String breakout;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("standardloss")
	private String standardloss;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("closingtime")
	private String closingtime;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("starndmouldnumber1")
	private String starndmouldnumber1;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("starndmouldnumber2")
	private String starndmouldnumber2;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("starndmouldnumber3")
	private String starndmouldnumber3;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("castingstarttime")
	private String castingstarttime;

    
}
