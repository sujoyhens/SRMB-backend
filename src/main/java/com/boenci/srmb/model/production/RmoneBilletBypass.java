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
@Table(name = "rmonebilletbypass")
public class RmoneBilletBypass {	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rmonebilletbypass_seq")
	@SequenceGenerator(
		name="rmonebilletbypass_seq",
		sequenceName="rmonebilletbypass_sequence",
		allocationSize=1
	)
	@JsonProperty("rmonebilletbypassid")
    private long rmonebilletbypassid;

	@JsonProperty("coldbilletbypassid")
    private long coldbilletbypassid;
	    
    @JsonProperty("rmonestoptime")
	private String rmonestoptime;

	@JsonProperty("rmonestarttime")
	private String rmonestarttime;

	@JsonProperty("rmonebilletbypassed")
	@Column(columnDefinition = "integer default 0")
	private long rmonebilletbypassed;

	@JsonProperty("rmonereason")
	@Column(length = 5000)
	private String rmonereason;

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
