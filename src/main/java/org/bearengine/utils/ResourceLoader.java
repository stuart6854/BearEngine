package main.java.org.bearengine.utils;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stuart on 03/06/2016.
 */
public class ResourceLoader {

    private static final Map<Class, IResourceLoader> ResourceLoaders = new HashMap<>();

    public enum FileType{
        Internal, External, ClassPath
    }

    public static < T > T Load(String resourcePath, Class<T> type){
        return type.cast(ResourceLoaders.get(type).Load(resourcePath));
    }

    private static Object FileLoader(String path){
        return new File(path);
    }

    private static Object ImageLoader(String path){
        return new Image(path);
    }

    @FunctionalInterface
    public interface IResourceLoader{
        Object Load(String path);
    }

    static{
        ResourceLoaders.put(Image.class, ResourceLoader::ImageLoader);
        ResourceLoaders.put(File.class, ResourceLoader::FileLoader);
    }

}
