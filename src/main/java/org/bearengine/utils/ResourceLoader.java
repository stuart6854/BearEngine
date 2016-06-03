package main.java.org.bearengine.utils;

import main.java.org.bearengine.debug.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Stuart on 03/06/2016.
 */
public class ResourceLoader {

    public enum FileType{
        Internal, External
    }

    public static BufferedImage LoadImage(String path, FileType fileType){
        try{
            if(fileType == FileType.Internal){
                InputStream stream = ResourceLoader.class.getResourceAsStream("main/java/resources/textures/" + path);
                //InputStream stream = Image.class.getResourceAsStream("/" + path);
                BufferedInputStream inputStream = new BufferedInputStream(stream);
                return ImageIO.read(inputStream);
            }else if(fileType == FileType.External){
                File file = new File(path);
                return ImageIO.read(file);
            }
        } catch(IOException e) {
            Debug.exception("Image -> Could not Load Image: ");
            e.printStackTrace();
        }
        return null;
    }
    
}
