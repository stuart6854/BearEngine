package org.bearengine.graphics.types;

import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.bearengine.debug.Debug;
import org.bearengine.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.CGL;

import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.stb.STBImage.*;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Image {

    public ByteBuffer ImageData;

    public int Width, Height;
    public int Components;

    public static Image loadImage(String imagePath){
        Image image = null;
        String imageExtension = FileUtils.getFileExtension(imagePath);

        switch(imageExtension){
            case ".png":
                image = loadPNGImage(imagePath);
                break;
            case ".jpg":
                image = loadJPGImage(imagePath);
                break;
        }

        return image;
    }

    private static Image loadPNGImage(String imagePath){
        Image image = new Image();
        try {
            PNGDecoder decoder = new PNGDecoder(new FileInputStream(imagePath));
            int width = decoder.getWidth();
            int height = decoder.getHeight();
            int components = (decoder.hasAlpha()) ? 4 : 3;

            ByteBuffer imageData = ByteBuffer.allocateDirect(components * width * height);
            decoder.decodeFlipped(imageData, decoder.getWidth() * components, (components == 4) ? PNGDecoder.Format.RGBA : PNGDecoder.Format.RGB);
            imageData.flip();

            image.ImageData = imageData;
            image.Width = width;
            image.Height = height;
            image.Components = components;

        } catch(IOException e) {
            e.printStackTrace();
        }
        Debug.log("Image -> PNG Image Loaded.");
        Debug.log("Image -> Width: " + image.Width);
        Debug.log("Image -> Height: " + image.Height);
        Debug.log("Image -> Components: " + image.Components);

        return image;
    }

    private static Image loadJPGImage(String imagePath){
        //ByteBuffer imageBuffer = readJPG(imagePath);

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(1);

        ByteBuffer imageData = stbi_load(imagePath, w, h, c, 0);
        if(imageData == null)
            Debug.exception("Image -> loadJPG() -> Failed to load image: " + imagePath);

        Image image = new Image();
        image.ImageData = imageData;
        image.Width = w.get();
        image.Height = h.get();
        image.Components = 3;

        Debug.log("Image -> JPG Image Loaded.");
        Debug.log("Image -> Width: " + image.Width);
        Debug.log("Image -> Height: " + image.Height);
        Debug.log("Image -> Components: " + c.get());

        return image;
    }

    private static ByteBuffer readJPG(String imagePath){
        ByteBuffer buffer = null;
        try {
            FileInputStream inputStream = new FileInputStream(imagePath);
            SeekableByteChannel channel = Files.newByteChannel(Paths.get(imagePath));

            buffer = BufferUtils.createByteBuffer((int)channel.size() + 1);

            while(channel.read(buffer) != -1);

            inputStream.close();
            channel.close();
            buffer.flip();
        } catch(IOException e) {
            e.printStackTrace();
        }

        Debug.log("TEST: " + buffer.remaining());

        return buffer;
    }

}
