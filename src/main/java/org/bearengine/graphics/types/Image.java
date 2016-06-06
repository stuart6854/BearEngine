package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.utils.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Image {

    public String Name;

    public ByteBuffer ImageData;

    public int Width, Height;
    public int Components;

    private static Image Current;

    public static Image GetImage(String imagePath, ResourceLoader.FileType fileType){
        imagePath = imagePath.replace("\\", "/");
        Debug.log("Image -> Loading Image: " + imagePath);

        Current = new Image();
        Current.Name = imagePath.substring(imagePath.lastIndexOf("/") + 1);

        BufferedImage bufferedImage = GetBufferedImage(imagePath, fileType);

        return FromBufferedImage(bufferedImage);
    }

    public static Image FromBufferedImage(BufferedImage bufferedImage){
        ByteBuffer imageBuffer = GetImageData(bufferedImage);

        Image image = Current;
        if(image == null) image = new Image();

        image.ImageData = imageBuffer;
        image.Width = bufferedImage.getWidth();
        image.Height = bufferedImage.getHeight();
        image.Components = (bufferedImage.getColorModel().hasAlpha()) ? 4 : 3;

        Debug.log("Image -> Width: " + image.Width);
        Debug.log("Image -> Height: " + image.Height);
        Debug.log("Image -> Components: " + image.Components);
        Debug.log("Image -> Image Loaded.");

        Current = null;

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
