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

import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;

public class CertificateExtractorAPI_backup {

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
		
		/*
		// Validate Signature
		
		 //Get Attribute
        Attribute attribute1 =signerInfo.getSignedAttributes().get(PKCSObjectIdentifiers.pkcs_9_at_messageDigest);
        Attribute attribute2=null;
        if(signerInfo.getUnsignedAttributes()!=null)    {
            attribute2 =signerInfo.getUnsignedAttributes().get(PKCSObjectIdentifiers.id_aa_signatureTimeStampToken);
        }
        
        //Get MD in CMS
        String subFilter=signature.getSubFilter();
        String messageDigest="";
        if(subFilter.contains("ETSI.RFC3161"))  {
            TimeStampToken timeToken=new TimeStampToken(signedData);
            messageDigest=Base64.getEncoder().encodeToString(
                    timeToken.getTimeStampInfo().getMessageImprintDigest());
        }
        else    {
            messageDigest=Base64.getEncoder().encodeToString(
                    Hex.decode(attribute1.getAttributeValues()[0].toString().substring(1)));
        }
        MessageDigest digest=MessageDigest.getInstance(signerInfo.getDigestAlgOID());
        
        String signatureSID=signerInfo.getSID().getSerialNumber().toString(16);

        //Getting PublicKey
        Collection<X509CertificateHolder> matches = signedData.getCertificates().getMatches(signerInfo.getSID());
        byte[] pubByte=matches.iterator().next().getSubjectPublicKeyInfo().getEncoded();

        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(pubByte);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        
        //Check signature
        String encAlgo=null;
        if(signerInfo.getEncryptionAlgOID().trim().equals("1.2.840.113549.1.1.1")) {
            encAlgo="RSA";
        }
        
        if(encAlgo!=null)   {
            if(digest.getAlgorithm().equals("1.3.14.3.2.26")) {
                encAlgo="SHA1withRSA";                
            }
            else if(digest.getAlgorithm().equals("2.16.840.1.101.3.4.2.1"))    {
                encAlgo="SHA256withRSA";  
            }
            else if(digest.getAlgorithm().equals("2.16.840.1.101.3.4.2.2"))    {
                encAlgo="SHA384withRSA";  
            }
            else if(digest.getAlgorithm().equals("2.16.840.1.101.3.4.2.3"))    {
                encAlgo="SHA512withRSA";  
            }

        }
        else    {
            encAlgo=signerInfo.getEncryptionAlgOID();
        }
        
        
        System.out.println("ENC ALGO: " + encAlgo);
        PublicKey pubKey=kf.generatePublic(keySpec);
        
        Signature rsaSign=Signature.getInstance(encAlgo);       
        rsaSign.initVerify(pubKey);
        rsaSign.update(signerInfo.getEncodedSignedAttributes());
        boolean cmsSignatureValid=rsaSign.verify(signerInfo.getSignature());
		
        
        if(!cmsSignatureValid)   {
            //logApp.error("Signature ID {} have INVALID CMS Signature",signatureSID);
        	throw new CertificateExtractException("INVALID CMS Signature");
        }
        
        String mdPdf=Base64.getEncoder().encodeToString(digest.digest(contentToSigned));
        if(!mdPdf.equals(messageDigest)) {
        	throw new PDFSignatureVerificationFailed("Message Digest Signature ID {} is invalid, data integrity is NOT OK");
        }
        */
        
        //
        //
        //
		// Read Certificate Data
        //
        //
        //
        
		Collection<X509CertificateHolder> certCollection = signedData.getCertificates().getMatches(signerInfo.getSID());
		
		X509Certificate cert509 = getLeafCertificate(certCollection);

		String subjectDn = cert509.getSubjectDN().getName();
		//System.out.println("SubjectDN: " + cert509.getSubjectDN().getName());

		
		CertData cd = new CertData();
		LdapName ldapDN = new LdapName(subjectDn);

		for (Rdn rdn : ldapDN.getRdns()) {
			if (rdn.getType().compareTo("C") == 0)
				cd.setCountry(rdn.getValue().toString());
			else if (rdn.getType().compareTo("O") == 0)
				cd.setOrganisation(rdn.getValue().toString());
			else if (rdn.getType().compareTo("OID.2.5.4.17") == 0)
				cd.setPostalCode(rdn.getValue().toString());
			else if (rdn.getType().compareTo("PostalCode") == 0)
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
			else if (rdn.getType().compareTo("S") == 0)
				cd.setState(rdn.getValue().toString());
			else if (rdn.getType().toUpperCase().compareTo("T") == 0) // Title
				cd.setTitle(rdn.getValue().toString());
			else {
//				System.out.println("Name: " + rdn.getType().toUpperCase());
//				System.out.println("Value: " + rdn.getValue().toString());
			}
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


	public static EntityResponce extractDataFromPdf(EntityRequest reqXSD) throws Exception {
		
	    EntityResponce responseXsd = new EntityResponce();
	    
		String  path = "D://BalanceCer_AADCB2504L01.pdf";
	    CertData cd = CertificateExtractorAPI_backup.extractCertificateData(Files.readAllBytes(Paths.get(path)), null);
	    
	    if (cd == null) {
	        throw new Exception("No Data Found");
            
	} else {

	    EntityResponce response = new EntityResponce();
	    
        response.setCommomName(cd.getCn());
        response.setHouseIdentifier(cd.getHouseIdentifier());
        response.setStreet(cd.getStreet());
        response.setLocality(cd.getLocality());
        response.setState(cd.getState());
        response.setPostalcode(cd.getPostalCode());
        response.setOrganisation(cd.getOrganisation());
        response.setCountry(cd.getCountry());
        response.setTitle(cd.getTitle());
        

	    System.out.println("Common Name: " + cd.getCn());
	    System.out.println("House Identifier: " + cd.getHouseIdentifier());
	    System.out.println("Street: " + cd.getStreet());
	    System.out.println("Locality: " + cd.getLocality());
	    System.out.println("State: " + cd.getState());
	    System.out.println("Postal Code: " + cd.getPostalCode());
	    System.out.println("Organisation: " + cd.getOrganisation());
	    System.out.println("Country: " + cd.getCountry());
	    System.out.println("Title: " + cd.getTitle());
	    
        
	}
	    responseXsd.setError(false);
	    responseXsd.setErrorCode("");
	    responseXsd.setErrorDescription("Data Extracted Successfully");
        return responseXsd;
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
	
	
	
	public static void main(String[] args) throws Exception
	{
//		CertData cd =  CertificateExtractorAPI.extractCertificateData(Files.readAllBytes(Paths.get("D://BalanceCer_AADCB2504L01.pdf")),null );
		
		byte[] base64 = Files.readAllBytes(Paths.get("D://care4sign_signed.pdf"));
//		byte[] base64 = Files.readAllBytes(Paths.get("D://BalanceCer_AADCB2504L01.pdf"));

		CertData cd =  CertificateExtractorAPI_backup.extractCertificateData(base64,null );

		System.out.println("Commom Name: " + cd.getCn());
		System.out.println("House Identifier: " + cd.getHouseIdentifier());
		System.out.println("Street: " + cd.getStreet());
		System.out.println("Locality: " + cd.getLocality());
		System.out.println("State: " + cd.getState());
		System.out.println("Postal code: " + cd.getPostalCode());
		System.out.println("Organisation: " + cd.getOrganisation());
		System.out.println("Country: " + cd.getCountry());
		System.out.println("Title: " + cd.getTitle());
	}
	
//	 private  static byte[] getByteRangeData(ByteArrayInputStream bis,int[] byteRange)    {
//	        int length1=byteRange[1]+byteRange[3];
//	        byte[] contentSigned=new byte[length1];
//	        bis.skip(byteRange[0]);
//	        bis.read(contentSigned, 0, byteRange[1]);
//	        bis.skip(byteRange[2]-byteRange[1]-byteRange[0]);
//	        bis.read(contentSigned, byteRange[1], byteRange[3]);
//	        bis.reset();
//	        return contentSigned;
//
//	    }

}
