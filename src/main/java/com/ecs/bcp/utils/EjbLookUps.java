package com.ecs.bcp.utils;

import java.util.Hashtable;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ecs.bcp.remote.AuditLogsRemote;
import com.ecs.bcp.remote.AuditorAssignRemote;
import com.ecs.bcp.remote.BalanceConfirmRemote;
import com.ecs.bcp.remote.BankApiLogRemote;
import com.ecs.bcp.remote.BankApiMasterRemote;
import com.ecs.bcp.remote.BankLinkageRemote;
import com.ecs.bcp.remote.BankMasterRemote;
import com.ecs.bcp.remote.EmailTemplateRemote;
import com.ecs.bcp.remote.EntityDetailsRemote;
import com.ecs.bcp.remote.FetchIdPaymentRemote;
import com.ecs.bcp.remote.FetchOrderPaymentRemote;
import com.ecs.bcp.remote.GstMasterRemote;
import com.ecs.bcp.remote.ICALLogMasterRemote;
import com.ecs.bcp.remote.PaymentDetailsRemote;
import com.ecs.bcp.remote.PaymentReportDetailsRemote;
import com.ecs.bcp.remote.SettingsRemote;
import com.ecs.bcp.remote.SignCheckRemote;
import com.ecs.bcp.remote.SmsEmailRemote;
import com.ecs.bcp.remote.StateMasterRemote;
import com.ecs.bcp.remote.TdsMasterRemote;
import com.ecs.bcp.remote.UserDetailsRemote;
public class EjbLookUps {

	

	private static String REMOTE_URL_EntityDetailsRemote = "ejb:BcpEAR/BcpEJB//EntityDetailsEjb!com.ecs.bcp.remote.EntityDetailsRemote";
	private static String REMOTE_URL_UserDetailsRemote = "ejb:BcpEAR/BcpEJB//UserDetailsEjb!com.ecs.bcp.remote.UserDetailsRemote";
	private static String REMOTE_URL_SmsEmailRemote = "ejb:BcpEAR/BcpEJB//SmsEmailEjb!com.ecs.bcp.remote.SmsEmailRemote";
	private static String REMOTE_URL_BankMasterRemote = "ejb:BcpEAR/BcpEJB//BankMasterEjb!com.ecs.bcp.remote.BankMasterRemote";
	private static String REMOTE_URL_BankLinkageRemote = "ejb:BcpEAR/BcpEJB//BankLinkageEjb!com.ecs.bcp.remote.BankLinkageRemote";
	private static String REMOTE_URL_PaymentDetailsRemote = "ejb:BcpEAR/BcpEJB//PaymentDetailsEjb!com.ecs.bcp.remote.PaymentDetailsRemote";
	private static String REMOTE_URL_AuditorAssignRemote = "ejb:BcpEAR/BcpEJB//AuditorAssignEjb!com.ecs.bcp.remote.AuditorAssignRemote";
	private static String REMOTE_URL_BalanceConfirmRemote = "ejb:BcpEAR/BcpEJB//BalanceConfirmEjb!com.ecs.bcp.remote.BalanceConfirmRemote";
	private static String REMOTE_URL_SettingsRemote = "ejb:BcpEAR/BcpEJB//SettingsEjb!com.ecs.bcp.remote.SettingsRemote";
	private static String REMOTE_URL_SignCheckRemote = "ejb:BcpEAR/BcpEJB//SignCheckEjb!com.ecs.bcp.remote.SignCheckRemote";
	private static String REMOTE_URL_StateMasterRemote = "ejb:BcpEAR/BcpEJB//StateMasterEjb!com.ecs.bcp.remote.StateMasterRemote";
	private static String REMOTE_URL_ICALLogMasterRemote = "ejb:BcpEAR/BcpEJB//ICAILLogMasterEjb!com.ecs.bcp.remote.ICALLogMasterRemote";
	private static String REMOTE_URL_GstMasterRemote = "ejb:BcpEAR/BcpEJB//GstMasterEjb!com.ecs.bcp.remote.GstMasterRemote";
	private static String REMOTE_URL_TdsMasterRemote = "ejb:BcpEAR/BcpEJB//TdsmasterEjb!com.ecs.bcp.remote.TdsMasterRemote";
	private static String REMOTE_URL_BankAPILogRemote = "ejb:BcpEAR/BcpEJB//BankApiLogEjb!com.ecs.bcp.remote.BankApiLogRemote";
	private static String REMOTE_URL_BankApiMasterRemote = "ejb:BcpEAR/BcpEJB//BankApiMasterEjb!com.ecs.bcp.remote.BankApiMasterRemote";
	private static String REMOTE_URL_FetchIdPaymentRemote = "ejb:BcpEAR/BcpEJB//FetchIdPaymentEjb!com.ecs.bcp.remote.FetchIdPaymentRemote";
	private static String REMOTE_URL_FetchOrderPaymentRemote = "ejb:BcpEAR/BcpEJB//FetchOrderPaymentEjb!com.ecs.bcp.remote.FetchOrderPaymentRemote";
	private static String REMOTE_URL_AuditLogsRemote = "ejb:BcpEAR/BcpEJB//AuditLogsEjb!com.ecs.bcp.remote.AuditLogsRemote";
	private static String REMOTE_URL_PaymentReportDetailsRemote = "ejb:BcpEAR/BcpEJB//PaymentReportDetailsEjb!com.ecs.bcp.remote.PaymentReportDetailsRemote";
	private static String REMOTE_URL_EmailTemplateRemote = "ejb:BcpEAR/BcpEJB//EmailTemplateEjb!com.ecs.bcp.remote.EmailTemplateRemote";
	
	
	
	public static Object getEJB(String ctx) {
		// LOG.info("Url: " + url);
		try{ 
			final Hashtable<String,String> jndiProperties = new Hashtable<>();
			jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			final Context context = new InitialContext(jndiProperties);
			return context.lookup(ctx);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static EntityDetailsRemote getEntityDetailsRemote() {
		return (EntityDetailsRemote) getEJB(REMOTE_URL_EntityDetailsRemote);
	}
	public static UserDetailsRemote getUserDetailsRemote() {
		return (UserDetailsRemote) getEJB(REMOTE_URL_UserDetailsRemote);
	}
	public static SmsEmailRemote getSmsEmailRemote() {
		return (SmsEmailRemote) getEJB(REMOTE_URL_SmsEmailRemote);
	}	
	
	public static BankMasterRemote getBankMasterRemote() {
		return (BankMasterRemote) getEJB(REMOTE_URL_BankMasterRemote);
	}	
	public static BankLinkageRemote getBankLinkageRemote() {
		return (BankLinkageRemote) getEJB(REMOTE_URL_BankLinkageRemote);
	}
	public static PaymentDetailsRemote getPaymentDetailsRemote() {
		return (PaymentDetailsRemote) getEJB(REMOTE_URL_PaymentDetailsRemote);
	}

  public static AuditorAssignRemote getAuditorAssignRemote() {
		return (AuditorAssignRemote) getEJB(REMOTE_URL_AuditorAssignRemote);
	  }
  
  public static BalanceConfirmRemote getBalanceConfirmRemote() {
		return (BalanceConfirmRemote) getEJB(REMOTE_URL_BalanceConfirmRemote);
	  }
  
  public static SettingsRemote getSettingsRemote() {
		return (SettingsRemote) getEJB(REMOTE_URL_SettingsRemote);
	  }
  
  public static SignCheckRemote   getSignCheckRemote() {
		return (SignCheckRemote) getEJB(REMOTE_URL_SignCheckRemote);
	  }
  
  public static StateMasterRemote   getStateMasterRemote() {
		return (StateMasterRemote) getEJB(REMOTE_URL_StateMasterRemote);
	  }

  
  public static ICALLogMasterRemote   getICALLogMasterRemote() {
		return (ICALLogMasterRemote) getEJB(REMOTE_URL_ICALLogMasterRemote);
	  }

  public static TdsMasterRemote   getTdsMasterRemote() {
		return (TdsMasterRemote) getEJB(REMOTE_URL_TdsMasterRemote);
	  }

  public static GstMasterRemote   getGstMasterRemote() {
		return (GstMasterRemote) getEJB(REMOTE_URL_GstMasterRemote);
	  }
  
  public static BankApiLogRemote   getBankApiLogRemote() {
		return (BankApiLogRemote) getEJB(REMOTE_URL_BankAPILogRemote);
	  }

  public static BankApiMasterRemote   getBankApiMasterRemote() {
		return (BankApiMasterRemote) getEJB(REMOTE_URL_BankApiMasterRemote);
	  }

  public static FetchIdPaymentRemote   getFetchIdPaymentRemote() {
		return (FetchIdPaymentRemote) getEJB(REMOTE_URL_FetchIdPaymentRemote);
	  }

  public static FetchOrderPaymentRemote   getFetchOrderPaymentRemote() {
		return (FetchOrderPaymentRemote) getEJB(REMOTE_URL_FetchOrderPaymentRemote);
	  }

  public static AuditLogsRemote   getAuditLogsRemote() {
		return (AuditLogsRemote) getEJB(REMOTE_URL_AuditLogsRemote);
	  }

  public static PaymentReportDetailsRemote   getPaymentReportDetailsRemote() {
		return (PaymentReportDetailsRemote) getEJB(REMOTE_URL_PaymentReportDetailsRemote);
	  }
  public static EmailTemplateRemote   getEmailTemplateRemote() {
		return (EmailTemplateRemote) getEJB(REMOTE_URL_EmailTemplateRemote);
	  }

}