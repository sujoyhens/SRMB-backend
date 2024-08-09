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
@Table(name = "smssummary")
public class SmsSummary extends SrmbCommon{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smssummary_seq")
	@SequenceGenerator(
		name="smssummary_seq",
		sequenceName="smssummary_sequence",
		allocationSize=1
	)
	@JsonProperty("smssummaryid")
    private long smssummaryid;

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

	@JsonProperty("unittype")
	private String unittype;
	
	@Column(columnDefinition = "integer default 0")
	@JsonProperty("userregisterid")
	private long userregisterid;
    

	@JsonProperty("productiondate")
	private String productiondate;	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotchargingpercentage")
	private long hotchargingpercentage;

	// delete from here
	// @Column(columnDefinition = "integer default 0")
	// @JsonProperty("hotoutpowercut")
	// private long hotoutpowercut;

	// @Column(columnDefinition = "integer default 0")
	// @JsonProperty("hotoutfurnace")
	// private long hotoutfurnace;

	// @Column(columnDefinition = "integer default 0")
	// @JsonProperty("hotoutlrf")
	// private long hotoutlrf;

	// @JsonProperty("remarks")
    // @Column(length = 5000)
	// private String remarks;

	//delete end here

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("nooffurnacesequencebreak")
	private long nooffurnacesequencebreak;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("noofpowersequencebreak")
	private long noofpowersequencebreak;
	
	@Column(columnDefinition = "integer default 0")
	@JsonProperty("noofsequencebreak")
	private long noofsequencebreak;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalfurnacesequencebreaktime")
	private long totalfurnacesequencebreaktime;
	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalpowersequencebreaktime")
	private long totalpowersequencebreaktime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalsequencebreaktime")
	private long totalsequencebreaktime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("mechanicaldowntime")
	private long mechanicaldowntime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("electricaldowntime")
	private long electricaldowntime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("powercutdowntime")
	private long powercutdowntime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("shutdowndowntime")
	private long downtime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("lrfdowntime")
	private long lrfdowntime;
	
	@Column(columnDefinition = "integer default 0")
	@JsonProperty("smsproduction")
	private long smsproduction;

	// add new here

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("multiplyingfactor")
	private double multiplyingfactor;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("totalbilletnos")
	private double totalbilletnos;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("totalbilletsize")
	private double totalbilletsize;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("totalbilletweight")
	private double totalbilletweight;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutccmnos")
	private double hotoutccmnos;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutccmsize")
	private double hotoutccmsize;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutccmweight")
	private double hotoutccmweight;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutrmnos")
	private double hotoutrmnos;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutrmsize")
	private double hotoutrmsize;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutrmweight")
	private double hotoutrmweight;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutpowernos")
	private double hotoutpowernos;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutpowersize")
	private double hotoutpowersize;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotoutpowerweight")
	private double hotoutpowerweight;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("hotcharging")
	private double hotcharging;
	
    
}
