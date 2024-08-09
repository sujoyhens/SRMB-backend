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
@Table(name = "equipmentmaster")
public class EquipmentMaster extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipmentmaster_seq")
	@SequenceGenerator(
		name="equipmentmaster_seq",
		sequenceName="equipmentmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("equipmentmasterid")
    private long equipmentmasterid;

	@JsonProperty("equipmentname")
	private String equipmentname;

	@Transient
	@JsonProperty("unitname")
	private String unitname;

	@Transient
	@JsonProperty("areaname")
	private String areaname;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("unitmasterid")
	@Column(columnDefinition = "integer default 0")
	private long unitmasterid;	

	@JsonProperty("areamasterid")
	@Column(columnDefinition = "integer default 0")
	private long areamasterid;	

	@JsonProperty("purchasedate")
	private String purchasedate;

	@JsonProperty("assetid")
	private String assetid;

	@JsonProperty("criticality")
	private String criticality;

	@JsonProperty("shutdownneeded")
	private String shutdownneeded;

	@JsonProperty("changeoverfrequency")
	@Column(columnDefinition = "integer default 0")
	private long changeoverfrequency;

	@JsonProperty("changeoverdate")
	private String changeoverdate;

	@JsonProperty("remarks")
	@Column(length = 5000)
	private String remarks;

	@JsonProperty("frequency")
	private String frequency;

	@JsonProperty("installationdate")
	private String installationdate;

	@JsonProperty("subequipmentrequirment")
	private String subequipmentrequirment;	
}
