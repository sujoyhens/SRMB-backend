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
@Table(name = "notifications")
public class NotiFications{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_seq")
	@SequenceGenerator(
		name="notifications_seq",
		sequenceName="notifications_sequence",
		allocationSize=1
	)
	@JsonProperty("notificationsid")
    private long notificationsid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("userregisterid")
    private long userregisterid;

	@Column(columnDefinition = "integer default 0")
	@JsonProperty("notificationsreferenceid")
    private long notificationsreferenceid;
	
	@JsonProperty("referenceno")
	private String referenceno;

	@JsonProperty("notificationtopic")
	private String notificationtopic;

	@JsonProperty("notificationdate")
	private String notificationdate;

	@JsonProperty("notificationtime")
	private String notificationtime;

	@Column(length = 5000)
	@JsonProperty("notificationdetails")
	private String notificationdetails;

	@JsonProperty("contactemail")
	private String contactemail;

	@Column(length = 5000)
	@JsonProperty("remarks")
	private String remarks;

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
