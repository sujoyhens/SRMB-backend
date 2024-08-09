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
@Table(name = "smsproduction")
public class SmsProduction extends SrmbCommon{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smsproduction_seq")
	@SequenceGenerator(
		name="smsproduction_seq",
		sequenceName="smsproduction_sequence",
		allocationSize=1
	)
	@JsonProperty("smsproductionid")
    private long smsproductionid;

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

	@Transient
	@JsonProperty("totalgrossweight")
	private String totalgrossweight;

	@Transient
	@JsonProperty("totalnetweight")
	private String totalnetweight;

	@Transient
	@JsonProperty("totaltareweight")
	private String totaltareweight;

	@JsonProperty("smsname")
	private String smsname;

	@JsonProperty("laboratoryname")
	private String laboratoryname;

	@JsonProperty("ccmname")
	private String ccmname;

	@JsonProperty("rmname")
	private String rmname;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("furnacetemperature")
	private Double furnacetemperature;
	 
    @JsonProperty("smsproductiondate")
	private String smsproductiondate;

	@JsonProperty("smsproductiontime")
	private String smsproductiontime;
   
	@JsonProperty("heatno")
	private String heatno;

	@JsonProperty("fcno")
	private String fcno;	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("patchinglife")
	private Long patchinglife;
	
	@JsonProperty("heatontime")
	private String heatontime;
	
	@JsonProperty("heatoftime")
	private String heatoftime;

	@JsonProperty("cycletime")
	private String cycletime;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("loadcellweight")
	private Double loadcellweight;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("tappingtemperature")
	private Double tappingtemperature;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("powercutdelay")
	private long powercutdelay;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("mechanicaldelay")
	private long mechanicaldelay;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("electricaldelay")
	private long electricaldelay;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("cranedelay")
	private long cranedelay;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("ladledelay")
	private long ladledelay;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("mpdelay")
	private long mpdelay;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("othersdelay")
	private long othersdelay;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totaldelay")
	private long totaldelay;
	
	@JsonProperty("remarks")
    @Column(length = 5000)
	private String remarks;

	@JsonProperty("productionid")
	private String productionid;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("islaboratorylogupdated")
	private String islaboratorylogupdated;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("isproductionlogupdated")
	private String isproductionlogupdated;

	@Column(columnDefinition = "varchar(255) default 'no'")
	@JsonProperty("ismillproductionupdated")
	private String ismillproductionupdated;

	//newly added fields

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("electricityconsumtion")
	private double electricityconsumtion;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("grossweight")
	private double grossweight;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("tareweight")
	private double tareweight;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("netweight")
	private double netweight;	

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("sponge")
	private double sponge;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("scrapmelt")
	private double scrapmelt;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("pigiron")
	private double pigiron;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("silicomanganese")
	private double silicomanganese;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("aluminium")
	private double aluminium;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("cpc")
	private double cpc;
	
	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("ferroshot")
	private double ferroshot;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("silicon")
	private double silicon;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("chrome")
	private double chrome;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("nfapowder")
	private double nfapowder;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("millscale")
	private double millscale;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("nickel")
	private double nickel;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("copper")
	private double copper;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("totalchargemix")
	private double totalchargemix;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("pellets")
	private double pellets;

    
}
