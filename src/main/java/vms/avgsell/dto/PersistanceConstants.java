package vms.avgsell.dto;

public interface PersistanceConstants {

  //---SQL---//
  
  public static final String ARCHIVE_MAINTANANCE_RECORDS_TABLE = "archive_maintenance_records";
  public static final String CURRENT_MALFUNCTION_RECORDS_TABLE = "current_mf_records";
  public static final String CURRENT_MAINTANANCE_RECORDS_TABLE = "current_mt_records";
  public static final String MACHINES_TABLE = "machines";

  //---MONGO---//
  
  public static final String CURRENT_MACHINES_STATE_COLLECTION = "current_state_machines";
}