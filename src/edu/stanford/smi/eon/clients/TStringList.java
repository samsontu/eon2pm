package edu.stanford.smi.eon.clients;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

//This is attempt to simulate TStringList from Delphi
//by K. Toppenberg

public class TStringList {
	
	public LinkedList<String> Data = new LinkedList<String>();
	
	public TStringList() {
		Data = new LinkedList<String>();		
	}
	
	public int Count() {
		return Data.size();
	}
	
	public void Add(String s) {
		Data.add(s);
	}
	
	public int IndexOf(String s) {
		return Data.indexOf(s);
	}
	
	public void Clear() {
		Data.clear();
	}
	
	public void Delete(int i) {
		Data.remove(i);
	}
	
	public String getText() {
	    String result = "";		
	    String ls = System.getProperty("line.separator");
	    StringBuilder stringBuilder = new StringBuilder();
	    for (int i=0; i < Data.size(); i++) {
	        stringBuilder.append( Data.get(i) );
	        stringBuilder.append( ls );	    	
	    }
	    result = stringBuilder.toString();		
		return result;
	}
	
	public void setText(String Text) {
	    this.Clear();
	    String ls = System.getProperty("line.separator");
	    String[] lines = Text.split(ls);
	    for (int i=0; i < lines.length; i++) {
	    	this.Add(lines[i]);
	    }
	} 
	
	public void WriteToFile(String fileNamePath) throws IOException {
		
	    //Create file
	    FileWriter fstream = new FileWriter(fileNamePath);
	    BufferedWriter out = new BufferedWriter(fstream);
	    for (int i=0; i < Data.size(); i++) {
	    	out.write( Data.get(i) );
	    	if (i< Data.size()) {
	    		out.newLine();
	    	}
	    }
	    //Close stream
	    out.close();
	}
	
	public void ReadFromFile(String fileNamePath) throws IOException {
	    //TODO finish
	    this.Clear();
	    BufferedReader reader = new BufferedReader( new FileReader (fileNamePath));
	    String line  = null;
	    while( (line = reader.readLine()) != null ) {
	    	this.Add(line);
	    }
	}
	
	public String getString(int Index) {
		String result = "";
		if ((Index >= 0) && (Index < Data.size())) { 
		  result = Data.get(Index);
		}  
		return result;
	}

	public void setString(int Index, String s) {
		if ((Index >= 0) && (Index < Data.size())) { 
		  Data.set(Index,  s);
		}  
	}
	
	public String getValue(String Label) {
         //Search for first instance of <Label>=<Value>
         //Return Value
          String result = "";	        
          for (int i=0; i < Count(); i++) {
            String s = getString(i);
            String[] lineParts = s.split("=");
            if (lineParts.length < 2) continue;
            String lbl = lineParts[0];
            if (!lbl.equals(Label)) continue;
            result = lineParts[1];
            break;
          }
          return result;
	}
	

}
