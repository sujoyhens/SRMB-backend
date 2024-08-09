package com.boenci.srmb.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import com.boenci.srmb.model.SrmbCommon;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "inspectionmaster")
public class InspectionMaster extends SrmbCommon{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inspectionmaster_seq")
	@SequenceGenerator(
		name="inspectionmaster_seq",
		sequenceName="inspectionmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("inspectionmasterid")
    private long inspectionmasterid;

	@JsonProperty("areaname")
	private String areaname;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("unitmasterid")
	private long unitmasterid;	

	@JsonProperty("remarks")
	private String remarks;
	
}
