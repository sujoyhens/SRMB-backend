package com.boenci.srmb.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "equipmentimages")
public class EquipmentImages extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipmentimages_seq")
	@SequenceGenerator(
		name="equipmentimages_seq",
		sequenceName="equipmentimages_sequence",
		allocationSize=1
	)
	@JsonProperty("equipmentimagesid")
    private long equipmentimagesid;

	@JsonProperty("imagepath")
	private String imagepath;

	@JsonProperty("unitmasterid")
	private long unitmasterid;	

	@JsonProperty("areamasterid")
	private long areamasterid;
	
	@JsonProperty("equipmentmasterid")
    private long equipmentmasterid;

	@JsonProperty("remarks")
	private String remarks;

	
}
