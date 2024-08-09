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
@Table(name = "coldbilletbypass")
public class ColdBilletBypass extends SrmbCommon{	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coldbilletbypass_seq")
	@SequenceGenerator(
		name="coldbilletbypass_seq",
		sequenceName="coldbilletbypass_sequence",
		allocationSize=1
	)
	@JsonProperty("coldbilletbypassid")
    private long coldbilletbypassid;

	@Column(columnDefinition = "integer default 0")
    @JsonProperty("unitmasterid")
	private long unitmasterid;	

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("areamasterid")
	private long areamasterid;	
	
	@Column(columnDefinition = "integer default 0")
	@JsonProperty("userregisterid")
	private long userregisterid;
    
    @JsonProperty("coldbilletbypassdate")
	private String coldbilletbypassdate;

	@JsonProperty("coldbilletbypasstime")
	private String coldbilletbypasstime;

	@JsonProperty("jobshift")
	private String jobshift;

	@JsonProperty("sequencebreakstarttime")
	private String sequencebreakstarttime;

	@JsonProperty("sequencebreakendtime")
	private String sequencebreakendtime;

	@JsonProperty("strand1starttime")
	private String strand1starttime;

	@JsonProperty("strand1billetbypassed")
	private String strand1billetbypassed;

	@JsonProperty("strand1temptime")
	private String strand1temptime;

	@JsonProperty("strand1bypassamount")
	private String strand1bypassamount;

	@JsonProperty("strand1remarks")
	@Column(length = 5000)
	private String strand1remarks;

	@JsonProperty("strand2starttime")
	private String strand2starttime;

	@JsonProperty("strand2billetbypassed")
	private String strand2billetbypassed;

	@JsonProperty("strand2temptime")
	private String strand2temptime;

	@JsonProperty("strand2bypassamount")
	private String strand2bypassamount;

	@JsonProperty("strand2remarks")
	@Column(length = 5000)
	private String strand2remarks;

	@JsonProperty("strand3starttime")
	private String strand3starttime;

	@JsonProperty("strand3billetbypassed")
	private String strand3billetbypassed;

	@JsonProperty("strand3temptime")
	private String strand3temptime;

	@JsonProperty("strand3bypassamount")
	private String strand3bypassamount;

	@JsonProperty("strand3remarks")
	@Column(length = 5000)
	private String strand3remarks;

	    
}
