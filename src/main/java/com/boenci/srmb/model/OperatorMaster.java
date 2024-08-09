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
@Table(name = "operatormaster")
public class OperatorMaster extends SrmbCommon{	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operatormaster_seq")
	@SequenceGenerator(
		name="operatormaster_seq",
		sequenceName="operatormaster_sequence",
		allocationSize=1
	)
	@JsonProperty("operatormasterid")
    private long operatormasterid;

	@Transient
	@JsonProperty("unitname")
	private String unitname;

	@Transient
	@JsonProperty("areaname")
	private String areaname;

	@JsonProperty("unittype")
	private String unittype;

	@JsonProperty("unitmasterid")
	@Column(columnDefinition = "integer default 0")
	private long unitmasterid;	

	@JsonProperty("areamasterid")
	@Column(columnDefinition = "integer default 0")
	private long areamasterid;

	@JsonProperty("designation")
	private String designation;

	@JsonProperty("employeeid")
	private String employeeid;

	@JsonProperty("employeename")
	private String employeename;

	@JsonProperty("phonenumber")
	private String phonenumber;

	@Column(length = 5000)
	@JsonProperty("remarks")
	private String remarks;
	
}
