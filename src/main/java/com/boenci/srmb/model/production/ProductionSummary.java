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
@Table(name = "productionsummary")
public class ProductionSummary extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productionsummary_seq")
	@SequenceGenerator(
		name="productionsummary_seq",
		sequenceName="productionsummary_sequence",
		allocationSize=1
	)
	@JsonProperty("productionsummaryid")
    private long productionsummaryid;

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
	@JsonProperty("noofheat")
	private long noofheat;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotchargingpercentage")
	private long hotchargingpercentage;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotoutccm")
	private long hotoutccm;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotoutrm1")
	private long hotoutrm1;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotoutrm2")
	private long hotoutrm2;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotoutpowercut")
	private long hotoutpowercut;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotoutfurnace")
	private long hotoutfurnace;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("hotoutlrf")
	private long hotoutlrf;

	@JsonProperty("remarks")
    @Column(length = 5000)
	private String remarks;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("noofccmsequencebreak")
	private long noofccmsequencebreak;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalsequencebreaktime")
	private long totalsequencebreaktime;	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("nooffurnacesequencebreak")
	private long nooffurnacesequencebreak;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalfurnacesequencebreaktime")
	private long totalfurnacesequencebreaktime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("noofpowersequencebreak")
	private long noofpowersequencebreak;	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalpowersequencebreaktime")
	private long totalpowersequencebreaktime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("processdowntime")
	private long processdowntime;

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

	// @Column(columnDefinition = "integer default 0")
	// @JsonProperty("laboratoryproduction")
	// private long laboratoryproduction;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("ccmproduction")
	private long ccmproduction;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("rmproduction")
	private long rmproduction;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalproduction")
	private long totalproduction;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("totalccmsequencebreaktime")
	private long totalccmsequencebreaktime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("noofsequencebreak")
	private long noofsequencebreak;
}
