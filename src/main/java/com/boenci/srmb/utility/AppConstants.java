
package com.boenci.srmb.utility;



public class AppConstants {
	private static String appUri = "";
		
	public static final String SUCCESS_RESPONSE = "Success";
	public static final String FAILURE_RESPONSE = "Failure";
	 
    public static final String ADD_NEW_RECORD = "save";
    public static final String UPDATE_RECORD = "update";
    public static final String DELETE_RECORD = "delete";
    public static final String SAVE_ALL = "SaveALL";

    public static final String MASTER_QUERY = "querytable.enterprisemasterid,"+
    "(select ename.enterprisename from EnterpriseMaster ename where "+
    " ename.enterprisemasterid = querytable.enterprisemasterid)  enterprisename, "+
    "querytable.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
    " pm.plantmasterid = querytable.plantmasterid) as plantname, querytable.sitemasterid, "+
    " (select sm.sitename from SiteMaster sm where sm.sitemasterid = querytable.sitemasterid) "+
    " as sitename";

    public static final String UNITMASTER_QUERY = "querytable.unitmasterid,(select um.unitname from UnitMaster um "+
    " WHERE um.unitmasterid = querytable.unitmasterid) as unitname";

    public static final String AREAMASTER_QUERY = "querytable.areamasterid,"+
    " (select am.areaname from AreaMaster am WHERE  am.areamasterid = querytable.areamasterid) as areaname";

    public static final String COMMON_QUERY = "SELECT querytable.action, querytable.caller, querytable.createdat,"+
    " querytable.createdby, querytable.deletedflag,querytable.status, querytable.updatedat, querytable.updatedby";

    public static final String EQUIPMENT_QURY = " querytable.equipmentmasterid,(select em.equipmentname from EquipmentMaster em "+
    " WHERE em.equipmentmasterid = querytable.equipmentmasterid) as equipmentname";

    public static final String SUBEQUIPMENT_QURY = " querytable.subequipmentmasterid,(SELECT  sem.subequipmentname "+
    " FROM SubEquipmentMaster sem WHERE sem.subequipmentmasterid = querytable.subequipmentmasterid) as subequipmentname";
    
    public static final String STANDARDMASTER_QUERY = "querytable.standardmasterid,(SELECT sm.standardname FROM "+
    " StandardMaster sm WHERE sm.standardmasterid = querytable.standardmasterid) as standardname";

    public static final String USERMASTER_QUERY = "querytable.userregisterid,(SELECT ur.username FROM UserRegister ur "+
    " WHERE ur.userregisterid = querytable.userregisterid) as username";

    public static final String CIPHER_TEXT = "AES/ECB/PKCS5PADDING";
    public static final String KEY_GENERATOR = "AES";
    
    public static final String ACCOUNT_SID = "ACd455eed9fc27c271a65ca7f5777afd1a";
    public static final String AUTH_TOKEN = "c7f5e25031147c34b1951e9c840eb732";
    
    public static byte[] PRIVATE_KEY = {  0x45, 0x57, 0x78, 0x71, 0x23, 0x73, 0x41,
		    0x24, 0x09, 0x78, 0x41, 0x78, 0x63, 0x41,0x73, 0x12};
    
    public static final String SERVICE_PROVIDER = "serviceprovider";
    
    public static final String SERVICE_FINDER = "servicefinder";
    
    public static final String REQUESTED = "Requested";
    
	/**
     * Key representing a DB Connection error.
     */
    public static final String DB_CONNECTION_ERROR = "GEN.DB.CONN.ERROR";

    /**
     * Key representing a error when searching.
     */
    public static final String ERROR_OCCURED_WHILE_SEARCHING_THE_OBJECT = "error occured while searching the object-->";

    /**
     * Key representing a error when deleting.
     */
    public static final String ERROR_OCCURED_WHILE_DELETING_THE_OBJECT = "error occured while deleting the objects-->";

    /**
     * Key representing a error when retrieving current sectional heads.
     */
    public static final String ERROR_OCCURED_WHILE_RETRIEVING_THE_OBJECT =
            "error occured while retrieving the object-->";

    /**
     * Key representing an empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * Key representing an memberId.
     */
    public static final String MEMBER_ID = "memberId";

    /**
     * Key representing an unchecked string.
     */
    public static final String UNCHECKED = "unchecked";

    /**
     * Key representing a DB Connection error.
     */
    public static final String EMAIL_ERROR = "EMAIL.ERROR";

    /**
     * Key representing a message.
     */
    public static final String MESSAGE = "message";

    /**
     * Key representing invalid hibernate operation - add new record.
     */
    public static final String HIBERNATE_INVALID_ADD_OPERATION = "GEN.DB.INVALID.OPERATION.ADD";

    /**
     * Key representing error while converting a string into date format.
     */
    public static final String DATE_CONVERSION_ERROR = "GEN.DB.INVALID.DATE.CONVERSION";

    /** Error message is to display when the field mismatch. */
    public static final String REPORTING_IO_EXCEPTION = "REPORTING.IO.EXCEPTION";

    /** Error message is to display when an error when the file is not found. */
    public static final String FILE_NOT_FOUND = "STU.UI.FILE.NOT.FOUND";

    /** index zero. */
    public static final int PARAM_INDEX_ZERO = 0;

    /** index ONE. */
    public static final int PARAM_INDEX_ONE = 1;

    /** index TWO. */
    public static final int PARAM_INDEX_TWO = 2;

    /** index THREE. */
    public static final int PARAM_INDEX_THREE = 3;

    /** index FOUR. */
    public static final int PARAM_INDEX_FOUR = 4;

    /** index FIVE. */
    public static final int PARAM_INDEX_FIVE = 5;

    /** index SIX. */
    public static final int PARAM_INDEX_SIX = 6;

    /** index TEN. */
    public static final int PARAM_INDEX_TEN = 10;

    /** Represents the key for the log message when error has occurred. */
    public static final String ERROR_WHILE_PROCESSING = "Error while processing the data.";

    /** Represents the key for the log message when file hasn't deleted. */
    public static final String ROLBACK_FILE_ERROR = "Rollback has happened. File is not deleted --- > ";

    /** Represents the key for the log message when folder hasn't deleted. */
    public static final String FOLDER_DELETED = " folder is deleted. --- > ";

    /** Represents the key for the log message when issue in the loading data. */
    public static final String DATA_LOADED_ERROR = "Data are not successfully loaded on to the database.";

    /** Represents the key for the log message when directory doesn't exist. */
    public static final Object DIRECTORY_DOES_NOT_EXIST = "Directory doesn't exists.";

    /** Represents the key for enable add edit and delete. */
    public static final String ENABLE_ADD_EDIT_DELETE = "EnableAddEditDelete";

    /** Represents the id of role admin. */
    public static final int ROLE_ADMIN = 1;

    /** Represents the decimal pattern. */
    public static final String ROUND_PATTERN = "###.";

    /** Represents the rounded decimal. */
    public static final String ROUNDED_DECIMAL = "#";

    /** Represents the Percentage mark. */
    public static final String PERCENTAGE_MARK = " %";

    /** Represents hundred. */
    public static final int HUNDRED = 100;

    /** Represents two floating point. */
    public static final int FLOAT_POINT_TWO = 2;

    /** Represents Three. */
    public static final int THREE = 3;

    /** Represents ZERO_POINT_FIVE. */
    public static final float ZERO_POINT_FIVE = 0.5f;

    /** Represents ten. */
    public static final int TEN = 10;

    /** Represents nine. */
    public static final int NINE = 9;

    /** Key representing a Phone number error. */
    public static final String  PHONENUMBER_ERROR= "PHONENUMBER.ERROR";
    
    /** Represents EMPTY_STRING_SPACE. */
    public static final String EMPTY_STRING_SPACE= " ";
    
    /** Represents STRING_ZERO. */
    public static final String STRING_ZERO= "0";
    
    /** Represents PLUS_SIGN. */
    public static final String PLUS_SIGN= "+";
    
    /** Represents BR_TAG. */
    public static final String BR_TAG= "<br>";
    
    
    /** Represents EQUAL_SIGN. */
    public static final String EQUAL_SIGN= "=";
    
    /** Represents DASH_SIGN. */
    public static final String DASH_SIGN= "-";
    

	public static String getAppUri() {
		return appUri;
	}

	public static void setAppUri(String appUri) {
		AppConstants.appUri = appUri;
	}
	
}
