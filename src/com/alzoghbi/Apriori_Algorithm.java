package com.alzoghbi;

import java.io.File;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Apriori_Algorithm {
	public static void main (String[] args) throws Exception, IOException {
		Scanner sc = new Scanner(System.in);
		int numOfTransaction = 9958;
		int counter = 0;
		System.out.print("Enter Minimum Support:");
		int minSupport = sc.nextInt();
		System.out.print("Enter Minimum Confidance: ");
		int minConfidance = sc.nextInt();
		List<String> ItemsCandidate = new ArrayList<>();
		List<String> ItemsCandidate1 = new ArrayList<>();
		List<String> ItemsCandidate3 = new ArrayList<>();
		List<Integer> countItemCandidate = new ArrayList<>();
		List<Integer> countItemFrequent = new ArrayList<>();
		List<String> ItemsFrequent = new ArrayList<>();
		List<String> ItemsFrequent1 = new ArrayList<>();
		List<String> ItemsFrequent2 = new ArrayList<>();
		Map <String , Integer > candidate = new HashMap<>();
		Map <String , Integer > frequent = new HashMap<>();
		candidate.clear();     ItemsCandidate.clear();     frequent.clear();      ItemsFrequent.clear();  countItemCandidate.clear();
		File f = new File("Sheet1.xls");
		Workbook wb = Workbook.getWorkbook(f);
		Sheet s = wb.getSheet(0);
		int row = s.getRows();
		int col = s.getColumns();
		System.out.println(row +"  " + col);
		// to collect Items from the File or DB and make it unique 
		for (int i = 1 ; i < row ; i++) {
			for (int j = 3 ; j < col ; j++) {
				Cell c = s.getCell(j , i);
				if (ItemsCandidate.isEmpty()) {
					ItemsCandidate.add(c.getContents().toString());
				}
				else if (!ItemsCandidate.contains(c.getContents().toString())) {
					ItemsCandidate.add(c.getContents().toString());
				}
			}
		}
		//System.out.println(Items);
		// get number of every item that are repeated in the File or DB
		for (int k = 0 ; k <ItemsCandidate.size() ; k++ ) {
			for (int i = 1 ; i < row ; i++) {
				for (int j = 3 ; j < col ; j++) {
					Cell c = s.getCell(j , i);
					if (ItemsCandidate.get(k) == c.getContents().toString()) {
						counter++;
					}
				}
			}
			candidate.put(ItemsCandidate.get(k), counter);
			counter = 0;
		}
		System.out.println("Candidate Table After teration 1 .........");
		System.out.println(candidate);
		// to get frequent item set 
		for (int i = 0 ; i < candidate.size() ;i++) {
			if (candidate.get(ItemsCandidate.get(i)) > minSupport || candidate.get(ItemsCandidate.get(i)) == minSupport ) {
				frequent.put(ItemsCandidate.get(i), candidate.get(ItemsCandidate.get(i)));
				ItemsFrequent.add(ItemsCandidate.get(i));
			}
		}
		System.out.println("Frequent Table After Iteratio 1.......");
		System.out.println(frequent);
		//System.out.println(ItemsFrequent);
		
		// preparing for the second iteration :D 
		
		candidate.clear();      ItemsCandidate.clear();
		//System.out.println(frequent.size());
		for (int i = 0 , j =0 ;  i < frequent.size() ;i++ ) {
			j = i+1;
			for ( ; j < frequent.size() ; j++ ) {
				ItemsCandidate.add(ItemsFrequent.get(i));
				ItemsCandidate1.add(ItemsFrequent.get(j));
			}
		}
		// get number of every item that are repeated in the File or DB
		String Tuple = ""; 
		counter = 0;
		for (int k = 0 ; k <ItemsCandidate1.size() ; k++ ) {
			int i = 1 ;
			for (; i < row ; i++) {
				Cell c = s.getCell(3 , i);
				Cell c1 = s.getCell(4 , i);
				Cell c2 = s.getCell(5 , i);
				Tuple = c.getContents().toString() + c1.getContents().toString() + c2.getContents().toString();
				//System.out.println(Tuple);
				//System.out.println(ItemsCandidate.get(k) + "  " + ItemsCandidate1.get(k));
				if (Tuple.contains(ItemsCandidate.get(k)) && Tuple.contains(ItemsCandidate1.get(k))) {
					counter++;
				}
			}
			//System.out.println(counter);
			countItemCandidate.add(counter);
			counter = 0;
		}
		// print Candidate Items 2
		System.out.println("Candidate Table After Itaration 2 ..........");
		for (int i = 0 ; i < countItemCandidate.size(); i++)
			System.out.println(ItemsCandidate.get(i) + " " + ItemsCandidate1.get(i) + " " +countItemCandidate.get(i));
		
		//to get second frequent item set
		ItemsFrequent.clear();    ItemsFrequent1.clear();
		for (int i = 0 ; i < countItemCandidate.size() ;i++) {
			if (countItemCandidate.get(i) > minSupport || countItemCandidate.get(i) == minSupport ) {
				ItemsFrequent.add(ItemsCandidate.get(i));
				ItemsFrequent1.add(ItemsCandidate1.get(i));
				countItemFrequent.add(countItemCandidate.get(i));
			}
		}
		// print the second Freaquent item set :D 
		System.out.println("Frequent Table After Iteration 2 ..........");
		for (int i = 0 ; i < countItemFrequent.size(); i++)
			System.out.println(ItemsFrequent.get(i) + " " + ItemsFrequent1.get(i) + " " +countItemFrequent.get(i));
		
		// Third Iteration :D
		// now we get the unique items from iteration 2 to make iteration 3
		List <String> unique = new ArrayList<>(); 
		for (int i = 0 ; i < ItemsFrequent.size() ; i++) {
			if (unique.isEmpty()) {
				unique.add(ItemsFrequent.get(i));
				unique.add(ItemsFrequent1.get(i));
			}
			else if (!unique.contains(ItemsFrequent.get(i)))
				unique.add(ItemsFrequent.get(i));
			else if (!unique.contains(ItemsFrequent1.get(i)))
				unique.add(ItemsFrequent1.get(i));
			}
		//System.out.println(unique);
		//now we get all combination between items
		ItemsCandidate.clear();   ItemsCandidate1.clear();   ItemsCandidate3.clear();  countItemCandidate.clear();
		ItemsFrequent.clear();   ItemsFrequent1.clear();    ItemsFrequent2.clear();    countItemFrequent.clear();
		for (int i = 0 ; i < unique.size()-2 ; i++) {
			int j = i + 1 ;   int k = i + 2;
			for ( ; j < unique.size()-1 ; j++)
				for( ; k < unique.size() ;k++) {
					ItemsCandidate.add(unique.get(i));
					ItemsCandidate1.add(unique.get(j));
					ItemsCandidate3.add(unique.get(k));
				}
		}
		// preparing to get third candidate Table :D
		Tuple = ""; 
		counter = 0;
		for (int k = 0 ; k <ItemsCandidate1.size() ; k++ ) {
			int i = 1 ;
			for (; i < row ; i++) {
				Cell c = s.getCell(3 , i);
				Cell c1 = s.getCell(4 , i);
				Cell c2 = s.getCell(5 , i);
				Tuple = c.getContents().toString() + c1.getContents().toString() + c2.getContents().toString();
				//System.out.println(Tuple);
				//System.out.println(ItemsCandidate.get(k) + "  " + ItemsCandidate1.get(k));
				if (Tuple.contains(ItemsCandidate.get(k)) && Tuple.contains(ItemsCandidate1.get(k)) && Tuple.contains(ItemsCandidate3.get(k))){
					counter++;
				}
			}
			//System.out.println(counter);
			countItemCandidate.add(counter);
			counter = 0;
		}
		
		// sout Candidate Table
		System.out.println("Candidate Table After Itaration 3.......");
		for (int i = 0 ; i < ItemsCandidate.size() ; i++ )
			System.out.println(ItemsCandidate.get(i) + " " + ItemsCandidate1.get(i) + "  " + ItemsCandidate3.get(i) + "  " + countItemCandidate.get(i));
		
		//to get third frequent item set
		for (int i = 0 ; i < countItemCandidate.size() ;i++) {
			if (countItemCandidate.get(i) > minSupport || countItemCandidate.get(i) == minSupport ) {
				ItemsFrequent.add(ItemsCandidate.get(i));
				ItemsFrequent1.add(ItemsCandidate1.get(i));
				ItemsFrequent2.add(ItemsCandidate3.get(i));
				countItemFrequent.add(countItemCandidate.get(i));
			}
		}
		// print the second Freaquent item set :D 
		System.out.println("Frequent Item Set After Iteration 3 ..........");
		for (int i = 0 ; i < countItemFrequent.size(); i++)
			System.out.println(ItemsFrequent.get(i) + " " + ItemsFrequent1.get(i) + " " +ItemsFrequent2.get(i)+ " " +countItemFrequent.get(i));
		
		// Compute Strong Association 
		for (int i = 0 ; i < ItemsFrequent2.size() ;i++) {
			int count2;
			int count;
			String now = "Weak";
			double min_Conf;
			/*// 
			System.out.println("Tuple "+ i+1 +" "+ ItemsFrequent.get(i) + "  " +ItemsFrequent1.get(i) + "  " + ItemsFrequent2.get(i));
			int count = countItems(ItemsFrequent.get(i), ItemsFrequent1.get(i));
			int count2 = frequent.get(ItemsFrequent.get(i));
			double min_Conf = ((double)count / (double)count2)*100;
			String now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("First Compination " + ItemsFrequent.get(i) + "--->" + ItemsFrequent1.get(i) + "  " + min_Conf + "  "+ now);
			//
			count = countItems(ItemsFrequent.get(i), ItemsFrequent2.get(i));
			count2 = frequent.get(ItemsFrequent.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("Second Compination " + ItemsFrequent.get(i) + "--->" + ItemsFrequent2.get(i) + "  " + min_Conf + "  "+ now);
			//
			count = countItems(ItemsFrequent1.get(i), ItemsFrequent.get(i));
			count2 = frequent.get(ItemsFrequent1.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("3rd Compination " + ItemsFrequent1.get(i) + "--->" + ItemsFrequent.get(i) + "  " + min_Conf + "  "+ now);
			//
			count = countItems(ItemsFrequent1.get(i), ItemsFrequent2.get(i));
			count2 = frequent.get(ItemsFrequent1.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("4th Compination " + ItemsFrequent1.get(i) + "--->" + ItemsFrequent2.get(i) + "  " + min_Conf + "  "+ now);
			//
			count = countItems(ItemsFrequent2.get(i), ItemsFrequent.get(i));
			count2 = frequent.get(ItemsFrequent2.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("5th Compination " + ItemsFrequent2.get(i) + "--->" + ItemsFrequent.get(i) + "  " + min_Conf + "  "+ now);
			//
			count = countItems(ItemsFrequent2.get(i), ItemsFrequent1.get(i));
			count2 = frequent.get(ItemsFrequent2.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("6th Compination " + ItemsFrequent2.get(i) + "--->" + ItemsFrequent1.get(i) + "  " + min_Conf + "  "+ now);
			//*/
			count = countItemFrequent.get(i);
			count2 = countItems(ItemsFrequent.get(i), ItemsFrequent1.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("7th Compination [" + ItemsFrequent.get(i) + "--->" + ItemsFrequent1.get(i) + "]  ---->"+ ItemsFrequent2.get(i)+" " + min_Conf + "  "+ now);
			//
			count = countItemFrequent.get(i);
			count2 = countItems(ItemsFrequent1.get(i), ItemsFrequent2.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("8th Compination [" + ItemsFrequent1.get(i) + "--->" + ItemsFrequent2.get(i) + "]  ---->"+ ItemsFrequent.get(i)+" " + min_Conf + "  "+ now);
			//
			count = countItemFrequent.get(i);
			count2 = countItems(ItemsFrequent2.get(i), ItemsFrequent.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("9th Compination [" + ItemsFrequent2.get(i) + "--->" + ItemsFrequent.get(i) + "]  ---->"+ ItemsFrequent1.get(i)+" " + min_Conf + "  "+ now);
			//
			count = countItemFrequent.get(i);
			count2 = frequent.get(ItemsFrequent.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("10th Compination [" + ItemsFrequent.get(i) + "]---> [" + ItemsFrequent1.get(i) + "&&"+ ItemsFrequent2.get(i)+"  " + min_Conf + "  "+ now);
			//
			count = countItemFrequent.get(i);
			count2 = frequent.get(ItemsFrequent1.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("11th Compination [" + ItemsFrequent1.get(i) + "]---> [" + ItemsFrequent.get(i) + "&&"+ ItemsFrequent2.get(i)+"  " + min_Conf + "  "+ now);
			//
			count = countItemFrequent.get(i);
			count2 = frequent.get(ItemsFrequent2.get(i));
			min_Conf = ((double)count / (double)count2)*100;
			now = "Weak";
			if (min_Conf >minConfidance || min_Conf == minConfidance) {
				now = "Strong";
			}
			System.out.println("12th Compination [" + ItemsFrequent2.get(i) + "]---> [" + ItemsFrequent.get(i) + "&&"+ ItemsFrequent1.get(i)+"  " + min_Conf + "  "+ now);
			
			
			
		}
	}
	
	public static int countItems(String fi , String se) throws Exception, IOException {
		File f = new File("Sheet1.xls");
		Workbook wb = Workbook.getWorkbook(f);
		Sheet s = wb.getSheet(0);
		int row = s.getRows();
		int col = s.getColumns();
		int count = 0;
		String Tuple = ""; 
			int i = 1 ;
			for (; i < row ; i++) {
				Cell c = s.getCell(3 , i);
				Cell c1 = s.getCell(4 , i);
				Cell c2 = s.getCell(5 , i);
				Tuple = c.getContents().toString() + c1.getContents().toString() + c2.getContents().toString();
				//System.out.println(Tuple);
				//System.out.println(ItemsCandidate.get(k) + "  " + ItemsCandidate1.get(k));
				if (Tuple.contains(fi) && Tuple.contains(se)) {
					count++;
				}
			}
			//System.out.println(counter);
			return count;
	}
	public static void testExel() throws Exception, IOException {
		File f = new File("test.xls");
		Workbook wb = Workbook.getWorkbook(f);
		Sheet s = wb.getSheet(0);
		int row = s.getRows();
		int col = s.getColumns();
		for (int i = 0 ; i < row ; i++) {
			for (int j = 0 ; j < col ; j++) {
				Cell c = s.getCell(j , i);
				System.out.print(c.getContents().toString()+" ");
			}
			System.out.println();
		}
	}
}
