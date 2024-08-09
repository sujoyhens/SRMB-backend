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
@Table(name = "laboratorylog")
public class LaboratoryLog extends SrmbCommon{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laboratorylog_seq")
	@SequenceGenerator(
		name="laboratorylog_seq",
		sequenceName="laboratorylog_sequence",
		allocationSize=1
	)
	@JsonProperty("laboratorylogid")
    private long laboratorylogid;

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
	
	@JsonProperty("productionid")
	private String productionid;

	@JsonProperty("laboratorylogdate")
	private String laboratorylogdate;

	@JsonProperty("laboratorylogtime")
	private String laboratorylogtime;
// to be shifted to smsproduction
	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("sponge")
	// private double sponge;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("scrapmelt")
	// private double scrapmelt;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("pigiron")
	// private double pigiron;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("siliconmanganese")
	// private double siliconmanganese;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("aluminium")
	// private double aluminium;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("cpc")
	// private double cpc;
	
	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("ferroshot")
	// private double ferroshot;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("silicon")
	// private double silicon;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("chrome")
	// private double chrome;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("mfopowder")
	// private double mfopowder;

	// // needs to change to nfapowder from mfopwder

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("millscale")
	// private double millscale;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("nickel")
	// private double nickel;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("copper")
	// private double copper;

	// @Column(columnDefinition = "decimal default 0.0")
	// @JsonProperty("totalchargemix")
	// private double totalchargemix;

	// end of smsproduction shift

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("carbon")
	private double carbon;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("manganese")
	private double manganese;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("phosphorus")
	private double phosphorus;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("chromium")
	private double chromium;
	
	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("chargemixnickel")
	private double chargemixnickel;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("chargemixsilicon")
	private double chargemixsilicon;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("isproductionlogupdated")
	private String isproductionlogupdated;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("ismillproductionupdated")
	private String ismillproductionupdated;

	//new field added

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("sulphur")
	private double sulphur;
   

}
