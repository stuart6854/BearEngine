package main.java.org.bearengine.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

    public static String[] ReadLines(String file){
        List<String> Lines = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
                Lines.add(line);
            }

            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return Lines.toArray(new String[Lines.size()]);
    }

    public static String[] ReadLines(InputStream inputStream){
        List<String> Lines = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null){
                Lines.add(line);
            }

            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return Lines.toArray(new String[Lines.size()]);
    }

}
