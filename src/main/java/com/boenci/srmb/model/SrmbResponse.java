package com.boenci.srmb.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component(value="srmbResponse")
public class SrmbResponse {

	
	private String status;	
	private String message;
	private String mobilenumberregisterstatus;
	private String statuscode;	
	private List resultList;
	
	
	public SrmbResponse(){
		this.status = "";
		this.message = "";
		this.statuscode ="";
		this.mobilenumberregisterstatus = "";
	}
	
	public SrmbResponse(String status, String message, String statuscode, String forQuestionSet, String mobilenumberregisterstatus){
		this.status = status;
		this.message = message;
		this.statuscode = statuscode;
		this.mobilenumberregisterstatus = mobilenumberregisterstatus;
	}
	
	
	@Override
	public String toString() {
		return "EcommerceResponse [status=" + status + ", message="
				+ message +", mobilenumberregisterstatus = "+mobilenumberregisterstatus+"]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public String getMobilenumberregisterstatus() {
		return mobilenumberregisterstatus;
	}

	public void setMobilenumberregisterstatus(String mobilenumberregisterstatus) {
		this.mobilenumberregisterstatus = mobilenumberregisterstatus;
	}


}
