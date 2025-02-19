package com.ecs.bcp.bank.api;

public class NumberToWordsConverter {
	
	
	
	    private static final String[] BELOW_TWENTY = {
	        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
	        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
	    };

	    private static final String[] TENS = {
	        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
	    };

	    private static final String[] THOUSANDS = {
	        "", "Thousand", "Lakh", "Crore"
	    };

	    public static String numberToWords(double num) {
	        if (num == 0) return "Zero Rupees";

	        // Split into rupees and paise parts
	        int rupees = (int) num;
	        int paise = (int) Math.round((num - rupees) * 100);

	        String words = numberToWords(rupees) + " Rupees";
	        if (paise > 0) {
	            words += " and " + numberToWords(paise) + " Paise";
	        }

	        return words.trim();
	    }

	    private static String numberToWords(int num) {
	        if (num == 0) return "Zero";

	        int i = 0;
	        String words = "";

	        while (num > 0) {
	            if (num % 1000 != 0) {
	                words = helper(num % 1000) + THOUSANDS[i] + " " + words;
	            }
	            num /= 1000;
	            i++;
	        }

	        return words.trim();
	    }

	    private static String helper(int num) {
	        if (num == 0)
	            return "";
	        else if (num < 20)
	            return BELOW_TWENTY[num] + " ";
	        else if (num < 100)
	            return TENS[num / 10] + " " + helper(num % 10);
	        else
	            return BELOW_TWENTY[num / 100] + " Hundred " + helper(num % 100);
	    }

	    public static void main(String[] args) {
	        double amount = 819.98;
	        String words = numberToWords(amount);
	        System.out.println("Amount: " + amount + " in words is: " + words);
	    }
	}

	    


