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
@Table(name = "menumaster")
public class MenuMaster{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menumaster_seq")
	@SequenceGenerator(
		name="menumaster_seq",
		sequenceName="menumaster_sequence",
		allocationSize=1
	)
	@JsonProperty("menumasterid")
    private long menumasterid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("parentid")
    private long parentid;

	@JsonProperty("title")
	private String title;

	@JsonProperty("link")
	private String link;

	@JsonProperty("arrangementid")
	private long arrangementid;

	@JsonProperty("menuid")
	private String menuid;

	@JsonProperty("icon")
	private String icon;

	@JsonProperty("completed")
	private String completed;

	@JsonProperty("fsize")
    private String fsize;

	@JsonProperty("color")
	private String color;

	@Column(columnDefinition = "varchar(255) default 'yes'")
	@JsonProperty("visibility")
	private String visibility;

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
