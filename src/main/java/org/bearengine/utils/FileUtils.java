package main.java.org.bearengine.utils;

import java.io.*;
import java.io.File;
import java.net.URISyntaxException;

public class FileUtils {
	
	/**
	 * @return Absolute path to Games Directory
	 */
	public static String getGameDir(){		
		String path = new File("").getAbsolutePath();
		return path.substring(0, path.lastIndexOf("\\"));
	}
	
	public static String getJarDir(){
        String path = "";
        try {
            path = new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath() + "\\";
            path = path.replace("\\bin", "");
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getFileExtension(String filePath){
        int lastSlashIndex = filePath.lastIndexOf("\\");
        int lastDotIndex = filePath.lastIndexOf(".");
        if(lastDotIndex > lastSlashIndex){
            return filePath.substring(lastDotIndex + 1);
        }
        return null;
    }

    public static InputStream GetResourceStream(String file){
        return FileUtils.class.getResourceAsStream("/main/java/resources/" + file);
    }

}
