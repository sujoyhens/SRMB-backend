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
@Table(name = "productmaster")
public class ProductMaster extends SrmbCommon{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productmaster_seq")
	@SequenceGenerator(
		name="productmaster_seq",
		sequenceName="productmaster_sequence",
		allocationSize=1
	)
	@JsonProperty("productmasterid")
    private long productmasterid;

	@JsonProperty("productdescription")
	private String productdescription;

	@JsonProperty("linenumber")
	private String linenumber;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("linespeed")
	private double linespeed;

	@Column(columnDefinition = "decimal default 0.0")
	@JsonProperty("sectionweight")
	private double sectionweight;

	@JsonProperty("size")
	private String size;

	@JsonProperty("itemtype")
	private String itemtype;

}
