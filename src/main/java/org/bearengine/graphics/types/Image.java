package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.bearengine.utils.FileUtils;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Image {

    public String Name;

    public ByteBuffer ImageData;

    public int Width, Height;
    public int Components;

    public static Image GetImage(String imagePath, ResourceLoader.FileType fileType){
        imagePath = imagePath.replace("\\", "/");
        Debug.log("Image -> Loading Image: " + imagePath);

        return loadImage(imagePath, fileType);
    }

    private static Image loadImage(String imagePath, ResourceLoader.FileType fileType){
        BufferedImage bufferedImage = GetBufferedImage(imagePath, fileType);
        ByteBuffer imageBuffer = GetImageData(bufferedImage);

        Image image = new Image();
        image.Name = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        image.ImageData = imageBuffer;
        image.Width = bufferedImage.getWidth();
        image.Height = bufferedImage.getHeight();
        image.Components = (bufferedImage.getColorModel().hasAlpha()) ? 4 : 3;

        Debug.log("Image -> Width: " + image.Width);
        Debug.log("Image -> Height: " + image.Height);
        Debug.log("Image -> Components: " + image.Components);
        Debug.log("Image -> PNG Image Loaded: " + imagePath);

        return image;
    }

    private static BufferedImage GetBufferedImage(String path, ResourceLoader.FileType fileType){
        try {
            if(fileType == ResourceLoader.FileType.Internal){
                InputStream stream = Image.class.getResourceAsStream("/main/java/resources/" + path);
                BufferedInputStream inputStream = new BufferedInputStream(stream);
                return ImageIO.read(inputStream);
            }else{
                File file = new File(path);
                return ImageIO.read(file);
            }
        } catch(IOException e) {
            Debug.exception("Image -> Could not Load Image: ");
            e.printStackTrace();
        }
        return null;
    }

    private static ByteBuffer GetImageData(BufferedImage bufferedImage){
        byte[] data = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.order(ByteOrder.nativeOrder());
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

}
