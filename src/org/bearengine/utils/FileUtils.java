package org.bearengine.utils;

import java.io.File;

public class FileUtils {
	
	/**
	 * @return Absolute path to Games Directory
	 */
	public static String getGameDir(){		
		String path = new File("").getAbsolutePath();
		return path.substring(0, path.lastIndexOf("\\"));
	}
	
	public static String getJarDir(){
		return new File("").getAbsolutePath();
	}

    public static String getFileExtension(String filePath){
        int lastSlashIndex = filePath.lastIndexOf("\\");
        int lastDotIndex = filePath.lastIndexOf(".");
        if(lastDotIndex > lastSlashIndex){
            return filePath.substring(lastDotIndex);
        }
        return null;
    }

}
