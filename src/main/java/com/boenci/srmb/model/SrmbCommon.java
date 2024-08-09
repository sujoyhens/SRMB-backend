package com.boenci.srmb.model;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@MappedSuperclass
@Data
public class SrmbCommon{

	@JsonProperty("enterprisemasterid")
	@Column(columnDefinition = "integer default 0")
    private long enterprisemasterid;

	@JsonProperty("sitemasterid")
	@Column(columnDefinition = "integer default 0")
    private long sitemasterid;

	@JsonProperty("plantmasterid")
	@Column(columnDefinition = "integer default 0")
	private long plantmasterid;	

	@Transient
    @JsonProperty("enterprisename")
	private String enterprisename;

	@Transient
	@JsonProperty("sitename")
	private String sitename;

	@Transient
	@JsonProperty("plantname")
	private String plantname;
		
	@JsonProperty("caller")
	private String caller;

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
