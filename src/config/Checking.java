package config;

import java.util.Vector;

import javax.swing.JOptionPane;

public class Checking {
	@SuppressWarnings("deprecation")
	public static boolean IsValidName(String str) {
		if(Character.isSpace(str.charAt(0))) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean IsEmailformat(String str) {
		boolean b = false;
		int dot = str.lastIndexOf(".");
		int at = str.indexOf("@");
		if((dot<0) || (at <0) ||(str.indexOf(" ")>0)) {
			return b;
		}
		String st = str.substring(0,at);
		String st1 = str.substring(dot+1);
		if(!st.trim().equals("")&&(st1.equals("com"))) {
			b = true;
			return b;
		}else {
			return b;
		}
		
	}
	
	public static boolean isPhoneNo(String str) {
		if(str !=null && str.length()<=11) {
			return str.startsWith("09");
		}
		return false;
	}
	
	public static boolean IsAllDigit(String str) {
	    if (str == null || str.isEmpty()) return false;
	    
	    for (int i = 0; i < str.length(); i++) {
	        if (!Character.isDigit(str.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static boolean IsContain(String s,Vector str) {
		for(int i=0; i<str.size(); i++) {
			if(s.equals((String)str.elementAt(i))) {
				return true;
			}
			
		}
		return false;
	}
	public static boolean checktxtquantity(String strqp) {
		if(strqp.equals("")) {
			JOptionPane.showMessageDialog(null, "You must enter the Quantity");
			return false;
		}else if(IsAllDigit(strqp)) {
			JOptionPane.showMessageDialog(null, "You must enter NUMBER for Quantity");
			return false;
		}else if(Integer.parseInt(strqp)>10000) {
			JOptionPane.showMessageDialog(null, "The Quantity you entered is too many to purchase!");
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean checktxtprice(String strqp) {
		System.out.println(strqp);
		if(strqp.equals("")) {
			JOptionPane.showMessageDialog(null, "You must enter the Price");
			return false;
		}else if(IsAllDigit(strqp)) {
			JOptionPane.showMessageDialog(null, "You must enter NUMBER the Price");
			return false;
		}else if(Long.parseLong(strqp)>1000000000) {
			JOptionPane.showMessageDialog(null, "The Price you entered is too much(more than 1,000,000,000)");
			return false;
		}else {
			return true;
		}
	}
	
	public static String Sumamount(Vector data,int t) {
		long sum =0;
		for(int i=0;i<data.size(); i++) {
			sum += Long.parseLong((String)data.elementAt(i));
			//sum 10000
			//str 000,01
		}
		if(t==1) {
			int len = String.valueOf(sum).length(),index =0;
			StringBuffer str = new StringBuffer("");
			for(int i=0; i<len;i++) {
				if(index==3) {
					str.append(",");
					index = 0;
					i--;
				}else {
					str.append(String.valueOf(sum).charAt(len-i-1));
					index++;
				}
			}
			return str.reverse().toString();//10,000
		}else {
			return String.valueOf(sum);
		}
		
	}
	
}
