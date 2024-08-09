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
@Table(name = "mechanicalmaintenance")
public class MechanicalMaintenance extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mechanicalmaintenance_seq")
	@SequenceGenerator(
		name="mechanicalmaintenance_seq",
		sequenceName="mechanicalmaintenance_sequence",
		allocationSize=1
	)
	@JsonProperty("mechanicalmaintenanceid")
    private long mechanicalmaintenanceid;

	@JsonProperty("unitmasterid")
	@Column(columnDefinition = "integer default 0")
	private long unitmasterid;	

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("areamasterid")
	@Column(columnDefinition = "integer default 0")
	private long areamasterid;	

	@JsonProperty("equipmentmasterid")
	@Column(columnDefinition = "integer default 0")
	private long equipmentmasterid;

	@JsonProperty("subequipmentmasterid")
	@Column(columnDefinition = "integer default 0")
	private long subequipmentmasterid;	

	@JsonProperty("standardmasterid")
	@Column(columnDefinition = "integer default 0")
    private long standardmasterid;

	@Transient
	@JsonProperty("unitname")
	private String unitname;

	@Transient
	@JsonProperty("areaname")
	private String areaname;

	@Transient
	@JsonProperty("equipmentname")
	private String equipmentname;

	@Transient
	@JsonProperty("subequipmentname")
	private String subequipmentname;

	@Transient
	@JsonProperty("standardname")
	private String standardname;

	@JsonProperty("llf")
	private String llf;

	@JsonProperty("frequency")
	private String frequency;	

	@JsonProperty("activity")
	private String activity;

	@JsonProperty("standardvalue")
	private String standardvalue;

	@JsonProperty("sparesrequired")
	private String sparesrequired;	

	@JsonProperty("cranerequired")
	private String cranerequired;

	@JsonProperty("shutdownrequired")
	private String shutdownrequired;

	@Column(length = 5000)
	@JsonProperty("remarks")
	private String remarks;

	
}
