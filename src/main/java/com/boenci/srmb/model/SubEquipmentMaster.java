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
@Table(name = "subequipmentmaster")
public class SubEquipmentMaster extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subequipmentmaster_seq")
	@SequenceGenerator(
		name="subequipmentmaster_seq",
		sequenceName="subequipmentmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("subequipmentmasterid")
    private long subequipmentmasterid;

	@JsonProperty("subequipmentname")
	private String subequipmentname;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("unitmasterid")
	private long unitmasterid;	

	@JsonProperty("areamasterid")
	private long areamasterid;	

	@JsonProperty("equipmentmasterid")
	private long equipmentmasterid;

	@Transient
	@JsonProperty("equipmentname")
	private String equipmentname;

	@Transient
	@JsonProperty("unitname")
	private String unitname;

	@Transient
	@JsonProperty("areaname")
	private String areaname;
	
	@JsonProperty("criticalstatus")
	private String criticalstatus;

	@JsonProperty("purchasedate")
	private String purchasedate;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("severity")
	private long severity;
	
	@Column(columnDefinition = "integer default 0")
	@JsonProperty("occurrence")
	private long occurrence;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("detection")
	private long detection;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("riskprioritynumber")
	private long riskprioritynumber;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("conditionbasedfrequency")
	private long conditionbasedfrequency;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("timebasedfrequency")
	private long timebasedfrequency;

	@Column(length = 5000)
	@JsonProperty("remarks")
	private String remarks;
	
}
