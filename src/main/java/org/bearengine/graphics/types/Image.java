package main.java.org.bearengine.graphics.types;

import de.matthiasmann.twl.utils.PNGDecoder;
import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.utils.ResourceLoader;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Image {

    public String Name;

    public String ImagePath;

    public ByteBuffer ImageData;

    public int Width, Height;

    public Image(String pathToImage){
        this.ImagePath = pathToImage;

        LoadImage();
    }

    private void LoadImage(){
        Debug.log("Image -> Loading Image: " + ImagePath);
        try {
            PNGDecoder decoder = new PNGDecoder(getClass().getResourceAsStream(ImagePath));

            Name = ImagePath;
            Width = decoder.getWidth();
            Height = decoder.getHeight();

            ImageData = ByteBuffer.allocateDirect(4 * Width * Height);
            decoder.decode(ImageData, Width * 4, PNGDecoder.Format.RGBA);

            ImageData.flip();

            Debug.log("Image -> Image Loaded.");
        } catch(NullPointerException | IOException e) {
            Debug.exception("Image -> Exception Loading Image", e);
        }
    }

//    public static Image GetImage(String imagePath, ResourceLoader.FileType fileType){
//        return GetImage(imagePath, fileType, true);
//    }
//
//    public static Image GetImage(String imagePath, ResourceLoader.FileType fileType, boolean flip){
//        imagePath = imagePath.replace("\\", "/");
//        Debug.log("Image -> Loading Image: " + imagePath);
//
//        Current = new Image();
//        Current.Name = imagePath.substring(imagePath.lastIndexOf("/") + 1);
//
//        BufferedImage bufferedImage = GetBufferedImage(imagePath, fileType);
//
//        return FromBufferedImage(bufferedImage, flip);
//    }
//
//    private static BufferedImage GetBufferedImage(String path, ResourceLoader.FileType fileType){
//        try {
//            if(fileType == ResourceLoader.FileType.Internal){
//                InputStream stream = Image.class.getResourceAsStream("/main/java/resources/" + path);
//                BufferedInputStream inputStream = new BufferedInputStream(stream);
//                return ImageIO.read(inputStream);
//            }else{
//                File file = new File(path);
//                return ImageIO.read(file);
//            }
//        } catch(IOException e) {
//            Debug.exception("Image -> Could not Load Image: ");
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public static Image FromBufferedImage(BufferedImage bufferedImage, boolean flip){
//        Debug.log("Image -> Creating from BufferedImage.");
//        ByteBuffer imageBuffer = GetImageData(bufferedImage, flip);
//
//        Image image = Current;
//        if(image == null) image = new Image();
//
//        image.ImageData = imageBuffer;
//        image.PixelWidth = bufferedImage.getWidth();
//        image.PixelHeight = bufferedImage.getHeight();
//        image.Components = (bufferedImage.getColorModel().hasAlpha()) ? 4 : 3;
//
//        Debug.log("Image -> PixelWidth: " + image.PixelWidth);
//        Debug.log("Image -> PixelHeight: " + image.PixelHeight);
//        Debug.log("Image -> Components: " + image.Components);
//        Debug.log("Image -> Image Loaded.");
//
//        Current = null;
//
//        return image;
//    }
//
//
//    private static ByteBuffer GetImageData(BufferedImage bufferedImage, boolean flip){
//        int width = bufferedImage.getWidth();
//        int height = bufferedImage.getHeight();
//        int[] pixels = new int[width * height];
//        bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
//
//        int bytesPerPixel = bufferedImage.getColorModel().hasAlpha() ? 4 : 3;
//
//        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bytesPerPixel);
//
//        for(int y = 0; y < height; y++){
//            int actualY = (flip) ? height - 1 - y : y;
//            for(int x = 0; x < width; x++){
//                int pixel = pixels[actualY * width + x];
//                buffer.put((byte)((pixel >> 16) & 0xFF));//Red
//                buffer.put((byte)((pixel >> 8) & 0xFF));//Green
//                buffer.put((byte)((pixel) & 0xFF));//Blue
//                if(bytesPerPixel == 4)
//                    buffer.put((byte)((pixel >> 24) & 0xFF));//Alpha
//            }
//        }
//
//        buffer.flip();
//
//        return buffer;
//    }

}
