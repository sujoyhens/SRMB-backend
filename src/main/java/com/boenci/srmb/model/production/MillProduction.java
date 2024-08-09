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
@Table(name = "millproduction")
public class MillProduction extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "millproduction_seq")
	@SequenceGenerator(
		name="millproduction_seq",
		sequenceName="millproduction_sequence",
		allocationSize=1
	)
	@JsonProperty("millproductionid")
    private long millproductionid;

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

	@JsonProperty("unittype")
	private String unittype;
	
    @JsonProperty("millproductiondate")
	private String millproductiondate;

	@JsonProperty("millproductiontime")
	private String millproductiontime;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("foremanid")
	private long foremanid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("croperatorid")
	private long croperatorid;

	@Transient
	@JsonProperty("foremanname")
	private String foremanname;

	@Transient
	@JsonProperty("croperatorname")
	private String croperatorname;

	
	@JsonProperty("rmoneproductionsize")
	private String rmoneproductionsize;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("millsrollnos")
	private long millsrollnos;

	@JsonProperty("ccmstand")
	private String ccmstand;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("ccmcummulative")
	private long ccmcummulative;

	
	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("totalproduction")
	private double totalproduction;

	@JsonProperty("milldowntimedelay")
	private String milldowntimedelay;

	@JsonProperty("milldowntimebd")
	private String milldowntimebd;

	@JsonProperty("milldowntimesequencebreak")
	private String milldowntimesequencebreak;

	@JsonProperty("ccmdowntimedelay")
	private String ccmdowntimedelay;

	@JsonProperty("ccmdowntimebd")
	private String ccmdowntimebd;

	@JsonProperty("downtimemissroll")
	private String downtimemissroll;

	@JsonProperty("powercutdowntime")
	private String powercutdowntime;
	
	@JsonProperty("itemtype")
	private String itemtype;
	
	@JsonProperty("productionid")
	private String productionid;


    @JsonProperty("rmoneproductionpcs")
	private String rmoneproductionpcs;


	@JsonProperty("rmoneproductionmt")	
	private double rmoneproductionmt;


	@JsonProperty("rmtwoproductionsize")
	private String rmtwoproductionsize;


	@JsonProperty("rmtwoproductionpcs")
	private String rmtwoproductionpcs;


	@JsonProperty("rmtwoproductionmt")
	private double rmtwoproductionmt;
}
