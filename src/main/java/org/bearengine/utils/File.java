package main.java.org.bearengine.utils;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Line;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 03/07/2016.
 */
public class File {

    public String FileName;
    public String FileType;
    public String FilePath;
    public String[] FileContents;

    public File(String path){
        this.FilePath = path;
        this.FileName = FilePath.substring(FilePath.lastIndexOf("/") + 1, FilePath.lastIndexOf("."));
        this.FileType = FilePath.substring(FilePath.lastIndexOf(".") + 1);

        LoadFileContents();
    }

    private void LoadFileContents(){
        List<String> lines = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(FilePath)));
            String line;
            while((line = reader.readLine()) != null){
                lines.add(line);
            }

            reader.close();

            FileContents = lines.toArray(new String[lines.size()]);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
