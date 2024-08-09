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
@Table(name = "unitmaster")
public class UnitMaster extends SrmbCommon{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unitmaster_seq")
	@SequenceGenerator(
		name="unitmaster_seq",
		sequenceName="unitmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("unitmasterid")
    private long unitmasterid;

	@JsonProperty("unitname")
	private String unitname;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("remarks")
	private String remarks;

}
