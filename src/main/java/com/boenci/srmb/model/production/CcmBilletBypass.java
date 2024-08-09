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
@Table(name = "ccmbilletbypass")
public class CcmBilletBypass {	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ccmbilletbypass_seq")
	@SequenceGenerator(
		name="ccmbilletbypass_seq",
		sequenceName="ccmbilletbypass_sequence",
		allocationSize=1
	)
	@JsonProperty("ccmbilletbypassid")
    private long ccmbilletbypassid;

	@JsonProperty("coldbilletbypassid")
    private long coldbilletbypassid;
	
    @JsonProperty("ccmstoptime")
	private String ccmstoptime;

	@JsonProperty("ccmstarttime")
	private String ccmstarttime;

	@JsonProperty("ccmbilletbypassed")
	@Column(columnDefinition = "integer default 0")
	private long ccmbilletbypassed;

	@JsonProperty("ccmreason")
	@Column(length = 5000)
	private String ccmreason;

	@JsonProperty("sequenceendtime")
	private String sequenceendtime;

	@JsonProperty("totalbilletbypassed")
	@Column(columnDefinition = "integer default 0")
	private long totalbilletbypassed;

	@JsonProperty("bypassamount")
	@Column(columnDefinition = "integer default 0")
	private long bypassamount;

	@JsonProperty("remarks")
	@Column(length = 5000)
	private String remarks;

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
