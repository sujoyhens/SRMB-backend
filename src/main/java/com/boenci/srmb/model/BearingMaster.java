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
@Table(name = "bearingmaster")
public class BearingMaster extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bearingmaster_seq")
	@SequenceGenerator(
		name="bearingmaster_seq",
		sequenceName="bearingmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("bearingmasterid")
    private long bearingmasterid;

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

	@JsonProperty("standname")
	private String standname;

	@JsonProperty("make")
	private String make;

	@JsonProperty("bearingtype")
	private String bearingtype;	

	@JsonProperty("bearingno")
	private String bearingno;

	@JsonProperty("bearinglife")
	private String bearinglife;

	@JsonProperty("lastchangeoverdate")
	private String lastchangeoverdate;

	@JsonProperty("installationdate")
	private String installationdate;	

	@JsonProperty("schedulechangeoverdate")
	private String schedulechangeoverdate;

	@JsonProperty("actualchangeoverdate")
	private String actualchangeoverdate;

	@JsonProperty("bearingstatus")
	private String bearingstatus;

	@JsonProperty("side")
	private String side;

	@JsonProperty("position")
	private String position;	
	
	@Column(length = 5000)
	@JsonProperty("remarks")
	private String remarks;

}
