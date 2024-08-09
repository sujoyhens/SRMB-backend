package com.boenci.srmb.model.production;

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
@Table(name = "sequenceparameter")
public class SequenceParameter{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceparameter_seq")
	@SequenceGenerator(
		name="sequenceparameter_seq",
		sequenceName="sequenceparameter_sequence",
		allocationSize=1
	)
	@JsonProperty("sequenceparameterid")
    private long sequenceparameterid;

	@JsonProperty("productionlogid")
    private long productionlogid;

	@JsonProperty("heatnumber")
	private String heatnumber;

	@JsonProperty("ladletemp")
	private String ladletemp;

	@JsonProperty("tundishtemp")
	private String tundishtemp;

	@JsonProperty("status")
	private String status;

	@JsonProperty("deletedflag")
	private String deletedflag;

	@JsonProperty("createdby")
    private String createdby;
	
	@JsonProperty("createdat")
    private String createdat;

    @JsonProperty("updatedat")
    private String updatedat;

	@JsonProperty("updatedby")
    private String updatedby;

	@JsonProperty("action")
    private String action;
    
}
