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
@Table(name = "rolldetails")
public class RollDetails extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolldetails_seq")
	@SequenceGenerator(
		name="rolldetails_seq",
		sequenceName="rolldetails_sequence",
		allocationSize=1
	)
	@JsonProperty("rolldetailsid")
    private long rolldetailsid;

	@Transient
	@JsonProperty("unitname")
	private String unitname;

	@Transient
	@JsonProperty("areaname")
	private String areaname;

	@JsonProperty("unitmasterid")
	@Column(columnDefinition = "integer default 0")
	private long unitmasterid;

	@JsonProperty("areamasterid")
	@Column(columnDefinition = "integer default 0")
	private long areamasterid;

	@JsonProperty("standname")
	private String standname;

	@JsonProperty("rollmax")
	@Column(columnDefinition = "integer default 0")
	private long rollmax;

	@JsonProperty("rollmin")
	@Column(columnDefinition = "integer default 0")
	private long rollmin;

	@JsonProperty("bd")
	@Column(columnDefinition = "integer default 0")
	private long bd;

	@JsonProperty("bl")
	@Column(columnDefinition = "integer default 0")
	private long bl;

	@JsonProperty("nopass")
	@Column(columnDefinition = "integer default 0")
	private long nopass;

	@JsonProperty("rollerno")
	private String rollerno;		

	@JsonProperty("passtype")
	private String passtype;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("rollerdia")
	private String rollerdia;		

	@JsonProperty("rollerlife")
	private String rollerlife;

	@JsonProperty("schedulechangeoverdate")
	private String schedulechangeoverdate;

	@JsonProperty("changeoverdia")
	private String changeoverdia;

	@JsonProperty("actualchangeoverdate")
	private String actualchangeoverdate;		

	@JsonProperty("rollerstatus")
	private String rollerstatus;

	@Column(length = 5000)
	@JsonProperty("remarks")
	private String remarks;
	
}
