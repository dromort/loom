package com.loom;

public class main {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: [input file path] [output file path]");
			return;
		}
		
		LogProcessor proc = new LogProcessor();
		proc.processLog(args[0], args[1]);
		System.out.println("Done!");
	}
}
