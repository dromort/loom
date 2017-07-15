package com.loom;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LogProcessor {
	
	private String input_path=null;
	private String output_path=null;
	private Map<String,ArrayList<Integer>> constantStringToLinesMap = new LinkedHashMap<String, ArrayList<Integer>>();
	
	/**
	 * Processing a log from the input file and storing the results
	 * in the output file 
	 *
	 * @param  input_path  path of the input file
	 * @param  output_path path of the output file
	 */
	public void processLog(String input_path, String output_path){
		this.input_path = input_path;
		this.output_path = output_path;
		
		//Go over each line in file and process it
		try (Scanner sc = new Scanner(new File(input_path), "UTF-8")) {
			int i=0;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				processLine(line, i++);
			}
			sc.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
		printResults();
	}
	
	private void processLine(String line, int line_number) {
		String[] parts = line.split(" ");
		if (parts.length < 4)
			System.out.println("Error parsing line: " + line);
		
		//Get the unique part of the string and store it in map as a key
		//and add a line number to array (value)
		String name = parts[2];
		String constantString = line.split(name + "\\s+")[1];
		ArrayList<Integer> val = constantStringToLinesMap.get(constantString);
		if (null == val){
			val = new ArrayList<Integer>();
			constantStringToLinesMap.put(constantString, val);
		}

		val.add(line_number);
	}
	
	void printResults() {
		
		//Init writer once for performance reasons instead of opening the file for add each time
		PrintWriter writer;
		try{
		    writer = new PrintWriter(output_path, "UTF-8");
		} catch (IOException e) {
		   return;
		}
		
		//Go over unique substrings
		writer.println("=====");
		for (Map.Entry<String, ArrayList<Integer>> entry : constantStringToLinesMap.entrySet()){
			if (entry.getValue().size()>1){
				PrintLinesFromFile(writer, entry.getValue());
			}
		}
		writer.println("=====");
	    writer.close();
	}
	
	
	private void PrintLinesFromFile(PrintWriter writer, ArrayList<Integer> line_numbers) {
		List<String> names;
		
		//Go over the input file and print the relevant lines
		int j = 0;
		try (Scanner sc = new Scanner(new File(input_path), "UTF-8")) {
			names = new ArrayList<String>();
			int i=0;
			while (sc.hasNextLine() && j<line_numbers.size()) {
				String line = sc.nextLine();

				if (i++!=line_numbers.get(j))
					continue;
				
				//Store the changing word
				String[] parts = line.split(" ");
				if (parts.length < 4)
					System.out.println("Error parsing line: " + line);
				names.add(parts[2]);

				//Print the line and increment the line_numbers array index
				writer.println(line);
				j++;
			}
			
			//Print the changing words for this substring
			StringBuilder concl = new StringBuilder("The changing word was: " + names.get(0));
			for(int idx=1; idx < names.size(); idx++)
				concl.append(", " + names.get(idx));
			writer.println(concl);
				
			writer.println("");
			
			sc.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
}
