/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is EON Guideline Execution Engine.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2009.  All Rights Reserved.
 *
 * The EON Guideline Execution Engine was developed by Center for Biomedical
 * Informatics Research (http://www.bmir.stanford.edu) at the Stanford University
 * with support from the National Library of Medicine.  Current
 * information about the EON project can be obtained at 
 * http://www.smi.stanford.edu/projects/eon/
 *
 */

/* Code modified from original by Kevin Toppenberg and Eddie Hagood
 * April, 2012
 */

package edu.stanford.smi.eon.clients;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.KBHandler;

import java.io.*;
import java.text.SimpleDateFormat;

import org.apache.log4j.*;


public class ATHENA_Server_Local extends Object {
	static boolean deleteInput = true;
	static boolean mainLoopRunning = true;
	static boolean fileProcessed = false;
	static boolean loadKB = true;
	
	public ATHENA_Server_Local() {
		//Class constructor code can go here. 
	};

	private PCAServer_i PCAImp = null;
	private static ATHENA_Server_Local Athena_Server_Obj = null;
	static PCASession pca = null;

	static Logger logger = Logger.getLogger(ATHENA_HTN_Test.class);
	
	private String[] loadKBInfo(String mode, String parentPath) {
		//Results:  [0] = kbFilePath
		//			[1] = kbID
		
		String[] results = new String[2];
		results[0]="";
		results[1]="";
		String configFilePath = parentPath + "config/";
		File configFileDir = new File(configFilePath);		
		if (configFileDir.isDirectory()==false) {
			System.out.println(configFileDir.getName() + " directory is missing. Exiting");
			System.exit(0);
		}				
		
		String configFName = parentPath + "config/KB MAP.txt";
		TStringList sl = new TStringList();
		try {
			sl.ReadFromFile(configFName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0; i < sl.Count(); i++) {
			String s = sl.getString(i);
			String lbl = s.split("=")[0];
			if (lbl.equals("")) continue;
			String[] lblParts = lbl.split(":");
			if (lblParts.length < 2) continue;
			if (lblParts[0].equals(mode) == false) continue;
			if (lblParts[1].equals("KB")) {
				String kbFilePath = parentPath + "kbs/" + s.split("=")[1].trim();   
				results[0] = kbFilePath;
			} else if (lblParts[1].equals("ID")) {
				results[1] = s.split("=")[1].trim();  //kb ID string
			}
		}
		return results;
	}
	
	private void loadKB(String kbURL, String guidelineId) {
		
		System.out.println("Starting to load KB");
		System.out.println("------------------------------------------------------------------");
		try {
			PCAImp = new PCAServer_i();
			PCAImp.kbManager = new KBHandler(kbURL); //Sever loads the KB
			//java.util.Date finishedKB = new java.util.Date();
			//logger.warn("finished loading KB "+(finishedKB.getTime() - startTime.getTime())+ " milliseconds after start.");
			pca = PCAImp.open_pca_session();
			if ((guidelineId != null) && (guidelineId != "")) {
				pca.setGuideline(guidelineId); // Specifies the guideline to use
			} else logger.error("No GUIDELINEID specified");
		} catch(Exception se) {
			logger.error("Exception raised during initialization " + se.toString());
			System.exit(1);
		}
		pca.finishSession();
		System.out.println("------------------------------------------------------------------");
		System.out.println("Done loading KB");

	}
	
	private static void processFile(String pid, File child, File outFilePath, String guidelineId, String htmlFilePath) {
		//Write to output directory
		//outFilePath should be path+pid+extension
		String caseData=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date startTime = new java.util.Date();
		try {
			caseData =  readFileAsString(child.getPath());
		} catch (java.io.IOException e) {
			logger.error("Error reading data file ", e);
			System.exit(-1);
		} 
		String recommendations = null;
		try {
			System.out.println("-----------------------------------------");
			recommendations = pca.topLevelComputeAdvisory( pid, caseData,  formatter.format(startTime), guidelineId, pid );
			System.out.println("-----------------------------------------");
			//String fileName = outFilePath+pid+fileExtension
			//File outFilePath = new File(fileName);;
			logger.warn("Output recommenations to: "+ outFilePath);
			if (outFilePath.exists()) {
				outFilePath.delete();
			}
			try {
				PrintWriter out = new PrintWriter(outFilePath.getPath());
				out.print(recommendations);
				out.flush();
				//System.out.println("****READY FILE PROCESSING****");
				//create Ready file
				//String readyFileName;
				//readyFileName = outFilePath.getPath();
				//System.out.println("Ready File Name Is: " + readyFileName);  //Remove
				//File readyFile = new File(readyFileName.replace(".xml","_READY.txt"));
				//try {
				//  readyFile.createNewFile();
				//  System.out.println("****SUCCESS****"+readyFile.getPath());
				//} catch (java.io.IOException e0) {
				//   System.out.println("Error creating READY file");
				//}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (PCA_Session_Exception e1) {
			e1.printStackTrace();
		}		
	}
	
	public static void outputACK(File filePath, String mode){
		File outFileName = new File(filePath.getPath()+"/"+mode+"_ACK.TXT");
		System.out.println("Sending output to: " + outFileName.getPath());
		if (outFileName.exists()) {
			outFileName.delete();
		}
		try {
			PrintWriter out = new PrintWriter(outFileName.getPath());
			//out.print(recommendations);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	static Boolean Verbose = true;

	public static void main(String[] argv) throws InterruptedException {
		// Arguments:
		// 1st = Mode
		// 2nd = Parent Path  -- (Parent path must have children /data  and /output
		
		System.out.println("+----------------------------------+");
		System.out.println("| Running Athena Pallas Processor  |");
		System.out.println("|             9/12/12              |");
		System.out.println("+----------------------------------+");

		Athena_Server_Obj = new ATHENA_Server_Local();
		
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);
		
		//Get and verify parameters
		if (argv.length <2) {
			System.out.println("Not enough parameters.");
			System.out.println("Expected 2 parameters: 1st is MODE, 2nd is path to working directory.");
			System.out.println("Exiting.");
			System.exit(0);
		}
		
		String mode = argv[0];
		String parentPath = argv[1];
		File inFilePath  = new File(parentPath + "astronautdata/");
		File outFilePath = new File(parentPath + "output/");
		File kbFilePath  = new File(parentPath + "kbs/"); 

		if (mode.equals("")){
			System.out.println("Parameter 1 is empty.  Should be RUNMODE (e.g. 'HTN'). Exiting");
			System.exit(0);	
		}
		
		if (inFilePath.isDirectory()==false) {
			System.out.println(inFilePath.getName()+" directory is missing. Exiting");
			System.exit(0);
		}
		
		if (outFilePath.isDirectory()==false) {
			System.out.println(outFilePath.getName() + " directory is missing. Exiting");
			System.exit(0);
		}
		
		if (kbFilePath.isDirectory()==false) {
			System.out.println(kbFilePath.getName() + " directory is missing. Exiting");
			System.exit(0);
		}		
		
		String kbID = "";
        TStringList   IgnoredFileNames = new TStringList();	
		
		//Main Loop
		while (mainLoopRunning) {
			if (Verbose == true) {
				//System.out.println("New Loop");
			}
			fileProcessed = false;
			if (Verbose == true) {
				//System.out.println("Ignoring #" + IgnoredFileNames.Count() + " files.");
			}
			
			//(re)load if needed
			if (loadKB==true){
				String[] kbInfo = Athena_Server_Obj.loadKBInfo(mode, parentPath);
				String  kbFilenamePath = kbInfo[0];
				kbID = kbInfo[1];
					
				Athena_Server_Obj.loadKB(kbFilenamePath, kbID);
				loadKB = false;
			}
			
			File[] listOfFiles = inFilePath.listFiles();
			// for (File child : inFilePath.listFiles()) {
			for (int i=0; i < listOfFiles.length; i++) {
				File child = listOfFiles[i];
				if (".".equals(child.getName()) || "..".equals(child.getName())) {
					continue;
				}
				if (child.getName().toUpperCase().equals(mode+"-CMD.TXT")) {
					try {
						handleCommands(child.getPath(),outFilePath, mode);
						//fileProcessed = true;
						if (child.delete() == false) {
							System.out.println("Unable to delete: " + child.getPath());
							Thread.sleep(1000);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//continue;
				}
				if (IgnoredFileNames.IndexOf(child.getName()) > -1) {
					//if (Verbose == true) {System.out.print("~");}
					continue;
				}
				if (Verbose == true) {System.out.println("Considering file: ["+child.getName()+"]");}
				String[] namePieces = child.getName().split("_");
				//  (should only open filenames with format of <MODE>_<DFN>.xml)
				if (Verbose == true) {System.out.println("File Name Is: " +  child.getName() );}
				String fileExt=GetFileExtension(child.getName());
				if (Verbose == true) {System.out.println("File Ext Is: " + fileExt);}
				//At times, there seems to be a race condition, where this java process
				//   tries to load in the file before the mump process is done writting
				//So I have modified the mumps process to that it outputs TWO files.
				//  1st the <MODE>_<DFN>.xml file
				//  2nd, after finished with above, it outputs <MODE>_<DFN>_READY.TXT
				//So the 2nd file must exist before opening the <MODE>_<DFN>.xml
				//  The 2nd file need not be opened or processed.  Just the fact that
				//  it exists on in the directory is signal that it is OK to process.
                if (fileExt.equals("xml") == false) {
					if (Verbose == true) {System.out.println("Skipping file due to wrong extension.");}
					IgnoredFileNames.Add(child.getName());
					continue;
				}
				if (((namePieces.length>1) && (namePieces[0].equals(mode))) == false) {
					if (Verbose == true) {System.out.println("Skipping file due to wrong mode.");}
					IgnoredFileNames.Add(child.getName());
					continue;
				}
				Boolean finishFileExists=finishFileExist(child.getPath());
				if (Verbose == true) {System.out.println("Ready File Exists?: " + finishFileExists);}
				//NOTE: after processing the <MODE>_<DFN>.xml, both files should be deleted
				//NOTE: in java, one can't use '=' to compare strings -- that just compares a pointer to the string.  Must use ".equals()"
				if (finishFileExists == true) {
					System.out.println("Found File to Process: ["+child.getName()+"]");
					fileProcessed = true;
					String patientID = namePieces[1];
					patientID = patientID.split("\\.")[ 0];
					File outFileName = new File(outFilePath.getPath()+"/"+mode+"_"+patientID+".xml");  
					String htmlFile = outFilePath.getPath()+"/"+mode+"_"+patientID+".html";
					System.out.println("SENDING TO: ["+outFileName.getPath()+"]");
					String readyFileName;
				    readyFileName = outFileName.getPath();
					try { 
						processFile(patientID, child, outFileName, kbID, htmlFile);
					} catch (Throwable t) {
						try {
							FileWriter fwrite = new FileWriter(outFileName);
							fwrite.write("-1^CDSS/Clinical Decision Support Engine(Athena) Error. " + t.getMessage());
							fwrite.flush();
							fwrite.close();					   
						} catch (IOException e3) {
							System.out.println("Could not create error message");
						}
						System.out.println("Error Processing Recommendation: " + t.getMessage());
						//return;
					}
				    System.out.println("Ready.TXT File Name Is: " + readyFileName);  //Remove
				    File readyFile = new File(readyFileName.replace(".xml","_READY.TXT"));
				    try {
				    	readyFile.createNewFile();
				    	FileWriter fstream = new FileWriter(readyFile);
				    	BufferedWriter out = new BufferedWriter(fstream);
				    	out.write("DONE");
				    	out.close();
				    	System.out.println("****SUCCESS****"+readyFile.getPath());
				    } catch (java.io.IOException e0) {
				    	System.out.println("Error creating READY file");
				    }
					if (child.delete() == false) {
						System.out.println("Unable to delete: " + child.getName());
						Thread.sleep(1000);
					}
					//Delete completed file
					File f = new File(getFinishFileName(child.getPath()));
					if (f.delete() == false) {
						System.out.println("Unable to delete: " + f.getName());
						Thread.sleep(1000);
					}
					        
				}
			}
			if (fileProcessed == false) {
				Thread.sleep(1000);
				if (Verbose == true) {System.out.print(".");}
			} else {
				if (Verbose == true) {System.out.println("Found process, so won't sleep...");}
			}
			mainLoopRunning = false;
		}  // for loop
		System.out.println("Finished with run.");
		System.exit(0);
	}  //Main routine

	private static void handleCommands(String fileNamePath, File outFilePath, String mode) throws IOException {
		//Purpose: Process file that contains commands for this process (e.g. STOP, ENQ etc.)
		System.out.println("Handling Command");
		TStringList sl = new TStringList();
		sl.ReadFromFile(fileNamePath);
	    for (int j=0; j < sl.Count(); j++) {
	    	handleOneCommand(sl.getString(j), outFilePath, mode);	    	
	    }
	    //File cmdFile = new File(fileNamePath);
	    //cmdFile.delete();
	}	
	

	private static String GetFileExtension(String fileName) {
	    String ext="";
	    int mid = fileName.lastIndexOf(".");
	    return fileName.substring(mid+1,fileName.length());
	}
	
	private static boolean finishFileExist(String filePath) {
	    String finishName=getFinishFileName(filePath);
	    //System.out.println("Ready File To Check " + finishName);
		if (Verbose == true) {System.out.println("Looking for file: " + finishName);}
	    File f = new File(finishName);
	    if (f.exists()) {
	            return true;
	    } else {
	            return false;
        }
	}
	
	private static String getFinishFileName(String fileName) {
	    String finishName="";
	    int mid = fileName.lastIndexOf(".");
	    return fileName.substring(0,mid)+"_READY.TXT";
	}
	
	private static void handleOneCommand(String s,File outFilePath, String mode) {
		System.out.println("Command is: "+s);
		if (s.equals("STOP")) { 
			mainLoopRunning = false;  
		} else if (s.equals("ENQ")) {
			System.out.println("Writing to ACK");
			outputACK(outFilePath, mode);
		} else if (s.equals("REFRESH-KB")) {
			loadKB=true;
		}
        //More command here later...  
	}
	
	private static String readFileAsString(String filePath) throws java.io.IOException{
		
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    
	    return new String(buffer);
	}


}

