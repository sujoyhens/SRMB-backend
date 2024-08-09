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
@Table(name = "areamaster")
public class AreaMaster extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "areamaster_seq")
	@SequenceGenerator(
		name="areamaster_seq",
		sequenceName="areamaster_sequence",
		allocationSize=1
	)
	@JsonProperty("areamasterid")
    private long areamasterid;

	@JsonProperty("areaname")
	private String areaname;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("unitmasterid")
	private long unitmasterid;	

	@JsonProperty("remarks")
	private String remarks;

	@Transient
	@JsonProperty("unitname")
	private String unitname;
	
	
}
