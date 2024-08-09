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
@Table(name = "standardmaster")
public class StandardMaster extends SrmbCommon{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "standardmaster_seq")
	@SequenceGenerator(
		name="standardmaster_seq",
		sequenceName="standardmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("standardmasterid")
    private long standardmasterid;

	@JsonProperty("standardname")
	private String standardname;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("unitmasterid")
	@Column(columnDefinition = "integer default 0")
	private long unitmasterid;	

	@JsonProperty("areamasterid")
	@Column(columnDefinition = "integer default 0")
	private long areamasterid;	

	@JsonProperty("equipmentmasterid")
	@Column(columnDefinition = "integer default 0")
	private long equipmentmasterid;

	@JsonProperty("subequipmentmasterid")
	@Column(columnDefinition = "integer default 0")
	private long subequipmentmasterid;

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

	@JsonProperty("remarks")
	private String remarks;
	
}
