package com.boenci.srmb.model.production;

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
@Table(name = "rmtwobilletbypass")
public class RmtwoBilletBypass {	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rmtwobilletbypass_seq")
	@SequenceGenerator(
		name="rmtwobilletbypass_seq",
		sequenceName="rmtwobilletbypass_sequence",
		allocationSize=1
	)
	@JsonProperty("rmtwobilletbypassid")
    private long rmtwobilletbypassid;

	@JsonProperty("coldbilletbypassid")
    private long coldbilletbypassid;
    
	@JsonProperty("rmtwostoptime")
	private String rmtwostoptime;

	@JsonProperty("rmtwostarttime")
	private String rmtwostarttime;

	@JsonProperty("rmtwobilletbypassed")
	@Column(columnDefinition = "integer default 0")
	private long rmtwobilletbypassed;

	@JsonProperty("rmtworeason")
	@Column(length = 5000)
	private String rmtworeason;

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
