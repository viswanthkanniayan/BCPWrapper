package com.ecs.bcp.service.ejb.PdfCertDataSDK.src.com.ecs.certextractor;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;

public class Test {

    public static class CertData {
        private String commonName;
        private String organisation;
        private String country;
        private String locality;
        private String state;
        private String street;

        private String houseIdentifier;
        private String postalCode;
        private String title;
    	
        // Getters and Setters
        public String getCommonName() { return commonName; }
        public void setCommonName(String commonName) { this.commonName = commonName; }

        public String getOrganisation() { return organisation; }
        public void setOrganisation(String organisation) { this.organisation = organisation; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getLocality() { return locality; }
        public void setLocality(String locality) { this.locality = locality; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }

        
        public String getHouseIdentifier() {
			return houseIdentifier;
		}
		public void setHouseIdentifier(String houseIdentifier) {
			this.houseIdentifier = houseIdentifier;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		@Override
        public String toString() {
            return "Common Name: " + commonName + "\n" +
                   "Organisation: " + organisation + "\n" +
                   "Country: " + country + "\n" +
                   "Locality: " + locality + "\n" +
                   "State: " + state + "\n" +
                   "houseIdentifier: " + houseIdentifier + "\n" +
                   "postalCode: " + postalCode + "\n" +
                   "title: " + title + "\n" +
                   "Street: " + street;
        }
    }

    public static CertData extractCertificateData(byte[] pdfData) throws Exception {
        try (PDDocument pdfDoc = Loader.loadPDF(pdfData)) {
            if (pdfDoc.getSignatureDictionaries() == null || pdfDoc.getSignatureDictionaries().isEmpty()) {
                throw new Exception("No signature found in the PDF!");
            }

            PDSignature signature = pdfDoc.getSignatureDictionaries().get(0);
            byte[] signatureContents = signature.getContents();
            CMSSignedData signedData = new CMSSignedData(signatureContents);

            SignerInformation signerInfo = (SignerInformation) signedData.getSignerInfos().getSigners().iterator().next();
            Collection<X509CertificateHolder> certCollection = signedData.getCertificates().getMatches(signerInfo.getSID());
            X509Certificate cert = getLeafCertificate(certCollection);

            if (cert == null) {
                throw new Exception("Unable to extract the certificate!");
            }

            return parseCertificateDetails(cert);
        }
    }

    private static CertData parseCertificateDetails(X509Certificate cert) throws InvalidNameException {
        CertData certData = new CertData();
        String subjectDn = cert.getSubjectDN().getName();
        LdapName ldapDN = new LdapName(subjectDn);

        for (Rdn rdn : ldapDN.getRdns()) {
            switch (rdn.getType()) {
                case "CN":
                    certData.setCommonName(rdn.getValue().toString());
                    break;
                case "O":
                    certData.setOrganisation(rdn.getValue().toString());
                    break;
                case "C":
                    certData.setCountry(rdn.getValue().toString());
                    break;
                case "L":
                    certData.setLocality(rdn.getValue().toString());
                    break;
                case "ST":
                    certData.setState(rdn.getValue().toString());
                    break;
                case "S":
                    certData.setState(rdn.getValue().toString());
                    break;
                case "STREET":
                    certData.setStreet(rdn.getValue().toString());
                    break;
                case "OID.2.5.4.51":
                    certData.setHouseIdentifier(rdn.getValue().toString());
                    break;
                default:
                    // Unhandled attributes can be logged if needed
                    break;
            }
        }
        return certData;
    }

    private static X509Certificate getLeafCertificate(Collection<X509CertificateHolder> certCollection) throws CertificateException {
        for (X509CertificateHolder holder : certCollection) {
            X509Certificate cert = new JcaX509CertificateConverter().getCertificate(holder);
            boolean isLeaf = certCollection.stream()
                .map(h -> {
                    try {
                        return new JcaX509CertificateConverter().getCertificate(h);
                    } catch (CertificateException e) {
                        return null;
                    }
                })
                .noneMatch(c -> c != null && c.getIssuerDN().getName().equalsIgnoreCase(cert.getSubjectDN().getName()));
            if (isLeaf) {
                return cert;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            String filePath = "D://care4sign_signed.pdf"; // Replace with your PDF file path
            byte[] pdfData = Files.readAllBytes(Paths.get(filePath));

            CertData certData = extractCertificateData(pdfData);

            System.out.println("Certificate Details Extracted:");
            System.out.println(certData);
        } catch (Exception e) {
            System.err.println("Error extracting certificate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
