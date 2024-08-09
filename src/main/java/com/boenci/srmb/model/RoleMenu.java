package com.boenci.srmb.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "rolemenu")
public class RoleMenu{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolemenu_seq")
	@SequenceGenerator(
		name="rolemenu_seq",
		sequenceName="rolemenu_sequence",
		allocationSize=1
	)
	@JsonProperty("rolemenuid")
    private long rolemenuid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("rolemasterid")
    private long rolemasterid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("menumasterid")
    private long menumasterid;

	@Transient
	@JsonProperty("parentid")
    private long parentid;

	@Transient
	@JsonProperty("title")
	private String title;

	@Transient
	@JsonProperty("link")
	private String link;

	@Transient
	@JsonProperty("menuid")
	private String menuid;

	@Transient
	@JsonProperty("icon")
	private String icon;

	@Transient
	@JsonProperty("completed")
	private String completed;

	@Transient
	@JsonProperty("fsize")
    private String fsize;

	@Transient
	@JsonProperty("color")
	private String color;

	@Transient
	@JsonProperty("arrangementid")
	private long arrangementid;


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
