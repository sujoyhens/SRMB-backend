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
@Table(name = "rolemaster")
public class RoleMaster{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolemaster_seq")
	@SequenceGenerator(
		name="rolemaster_seq",
		sequenceName="rolemaster_sequence",
		allocationSize=1
	)
	@JsonProperty("rolemasterid")
    private long rolemasterid;

	@JsonProperty("rolename")
	private String rolename;

	@JsonProperty("usermode")
    private String usermode;

	@Column(columnDefinition = "varchar(255) default 'yes'")
	@JsonProperty("view")
	private String view;

	@Column(columnDefinition = "varchar(255) default 'yes'")
	@JsonProperty("add")
	private String add;

	@Column(columnDefinition = "varchar(255) default 'yes'")
	@JsonProperty("update")
	private String update;

	@Column(columnDefinition = "varchar(255) default 'yes'")
	@JsonProperty("delete")
	private String delete;	

	@Column(columnDefinition = "varchar(255) default 'yes'")
	@JsonProperty("download")
	private String download;

	@Column(columnDefinition = "varchar(255) default 'yes'")
	@JsonProperty("upload")
	private String imagepath;

	@JsonProperty("caller")
    private String caller;

	@Column(columnDefinition = "varchar(255) default 'active'")
	@JsonProperty("status")
	private String status;

	@Column(columnDefinition = "varchar(255) default 'no'")
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
