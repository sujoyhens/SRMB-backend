package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.NotiFications;

public interface NotiFicationsService {
	
	public NotiFications findById(long id);
	
	public NotiFications validNotiFications(NotiFications notiFications);
	
	public NotiFications save(NotiFications notiFications);

	public NotiFications update(NotiFications areaMaster);

	public NotiFications delete(NotiFications areaMaster);
	
	public String updateNotiFications(NotiFications notiFications);
	
	public void deleteNotiFications(NotiFications notiFications);
	
	public List<NotiFications> findAllNotiFications();
	
	public boolean isNotiFicationsExist(NotiFications notiFications);
	
	public List<NotiFications> findNotiFicationsBySearchType(NotiFications notiFications, String searchType);
	
	public NotiFications findDetailsByMobileNo(NotiFications notiFications) throws Exception;
	
	public NotiFications findDetailsByEmail(NotiFications notiFications) throws Exception;

	public String saveNotification(String createdAt,String createdBy,Long userRegisterId,String notificationDate,String notificationTime,Long referenceId,String referenceNo,String notificationTopic,String description,String contactEmail);

	
   
}
