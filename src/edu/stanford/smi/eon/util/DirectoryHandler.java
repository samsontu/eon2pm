package edu.stanford.smi.eon.util;

import java.io.File;

public class DirectoryHandler {
	
	public static String combinePath(String path1, String path2 ) {
		File file1 = new File(path1);
	    File file2 = new File(file1, path2);
	    return file2.getPath();
	}

	public static boolean isAbsolutePath(String path) {
		File file = new File(path);
		return (file.isAbsolute());
	}


}
