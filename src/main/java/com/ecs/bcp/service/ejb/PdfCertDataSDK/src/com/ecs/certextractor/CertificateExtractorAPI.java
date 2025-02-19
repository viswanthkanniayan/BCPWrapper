package com.ecs.bcp.service.ejb.PdfCertDataSDK.src.com.ecs.certextractor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.tsp.TSPException;


public class CertificateExtractorAPI {

	public static CertData extractCertificateData(byte[]pdfData, byte[]verifyCert) throws CertificateExtractException, IOException, CMSException, InvalidNameException, CertificateException, NoSuchAlgorithmException, TSPException, InvalidKeySpecException, InvalidKeyException, SignatureException, PDFSignatureVerificationFailed
	{
		PDDocument pdfDoc = Loader.loadPDF(pdfData);

		if(pdfDoc.getSignatureDictionaries() == null || pdfDoc.getSignatureDictionaries().isEmpty())
		{
			throw new CertificateExtractException("Signature not found!");
		}
		
		if(pdfDoc.getSignatureDictionaries() == null || pdfDoc.getSignatureDictionaries().isEmpty())
		{
			throw new CertificateExtractException("Signature not found!");
		}

		PDSignature signature = pdfDoc.getSignatureDictionaries().get(0);

		byte[] contentToSigned=getByteRangeData(new ByteArrayInputStream(pdfData), signature.getByteRange());
		
		CMSSignedData signedData=new CMSSignedData(signature.getContents());
		SignerInformation signerInfo= (SignerInformation) signedData.getSignerInfos().getSigners().iterator().next();
		
		
//        System.out.println(signature.getLocation());
//        System.out.println(signature.getReason());
		Collection<X509CertificateHolder> certCollection = signedData.getCertificates().getMatches(signerInfo.getSID());
		
		X509Certificate cert509 = getLeafCertificate(certCollection);

		String subjectDn = cert509.getSubjectDN().getName();
//		System.out.println("SubjectDN: " + cert509.getSubjectDN().getName());
//		System.out.println("SerialNo: " + cert509.getSerialNumber());
		

		
		CertData cd = new CertData();
		LdapName ldapDN = new LdapName(subjectDn);

		for (Rdn rdn : ldapDN.getRdns()) {
			if (rdn.getType().compareTo("C") == 0)
				cd.setCountry(rdn.getValue().toString());
			else if (rdn.getType().compareTo("O") == 0)
				cd.setOrganisation(rdn.getValue().toString());
			else if (rdn.getType().compareTo("OID.2.5.4.17") == 0)
				cd.setPostalCode(rdn.getValue().toString());
			else if (rdn.getType().compareTo("L") == 0)
				cd.setLocality(rdn.getValue().toString());
			else if (rdn.getType().compareTo("ST") == 0)
				cd.setState(rdn.getValue().toString());
			else if (rdn.getType().compareTo("STREET") == 0)
				cd.setStreet(rdn.getValue().toString());
			else if (rdn.getType().compareTo("OID.2.5.4.51") == 0)
				cd.setHouseIdentifier(rdn.getValue().toString());
			else if (rdn.getType().compareTo("CN") == 0)
				cd.setCn(rdn.getValue().toString());
			else if (rdn.getType().toUpperCase().compareTo("T") == 0) // Title
				cd.setTitle(rdn.getValue().toString());
			else {
//				System.out.println("Name: " + rdn.getType().toUpperCase());
//				System.out.println("Value: " + rdn.getValue().toString());
			}
			cd.setReason(signature.getReason());
			cd.setLocation(signature.getLocation());
			
		//	cd.setSigningTime(String.valueOf(signature.getSignDate()));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cd.setSigningTime(sdf.format(signature.getSignDate().getTime()));

		}
		return cd;
	}
	
	private static X509Certificate getLeafCertificate(Collection c) throws CertificateException {

		Object[] cert509ListObj = c.toArray();
		X509Certificate[] cert509List = new X509Certificate[cert509ListObj.clone().length];
		
		int i = 0;
		for(Object cert509Holder : cert509ListObj )
		{
			cert509List[i++] = new JcaX509CertificateConverter().getCertificate((X509CertificateHolder) cert509Holder);
		}
//		X509Certificate[] cert509List = (X509Certificate[]) c.toArray();
		for (X509Certificate x5091 : cert509List) {
			boolean found = false;
			for (X509Certificate x5092 : cert509List) {
				if (x5091.getSubjectDN().getName().compareToIgnoreCase(x5092.getIssuerDN().getName()) == 0)
					found = true;
			}

			if (found == false)
				return x5091;
		}

		return null;
	}
	
	public static void main(String[] args) throws Exception
	{
		CertData cd =  CertificateExtractorAPI.extractCertificateData(Files.readAllBytes(Paths.get("D://BalanceCer_AADCB2504L01.pdf")),null );

		System.out.println("Commom Name: " + cd.getCn());
		System.out.println("House Identifier: " + cd.getHouseIdentifier());
		System.out.println("Street: " + cd.getStreet());
		System.out.println("Locality: " + cd.getLocality());
		System.out.println("State: " + cd.getState());
		System.out.println("Postal code: " + cd.getPostalCode());
		System.out.println("Organisation: " + cd.getOrganisation());
		System.out.println("Country: " + cd.getCountry());
		System.out.println("Title: " + cd.getTitle());
		
		System.out.println("Location: " + cd.getLocation());
		System.out.println("Reason: " + cd.getReason());
		
		System.out.println("Sign Date: " + cd.getSigningTime());
		
	}
	
	 private  static byte[] getByteRangeData(ByteArrayInputStream bis,int[] byteRange)    {
	        int length1=byteRange[1]+byteRange[3];
	        byte[] contentSigned=new byte[length1];
	        bis.skip(byteRange[0]);
	        bis.read(contentSigned, 0, byteRange[1]);
	        bis.skip(byteRange[2]-byteRange[1]-byteRange[0]);
	        bis.read(contentSigned, byteRange[1], byteRange[3]);
	        bis.reset();
	        return contentSigned;

	    }

}
