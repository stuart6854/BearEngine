package main.java.org.bearengine.utils;

import main.java.org.bearengine.graphics.types.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stuart on 03/06/2016.
 */
public class ResourceLoader {

    //TODO: The object... args parameter is a little messy, somehow tidy this up

    private static final Map<Class, IResourceLoader> ResourceLoaders = new HashMap<>();

    public enum FileType{
        Internal, External, ClassPath
    }

    public static <T> T Load(String resourcePath, Class<T> type, Object... args){
        return type.cast(ResourceLoaders.get(type).Load(resourcePath, args));
    }

    private static Object FileLoader(String path, Object... args){
        return new File(path);
    }

    /**
    @param args - Boolean as the whether or not to flip image
     **/
    private static Object ImageLoader(String path, Object... args){
        if(args.length > 0)
            return new Image(path, (boolean)args[0]);
        else
            return new Image(path);
    }

    @FunctionalInterface
    public interface IResourceLoader{
        Object Load(String path, Object... args);
    }

    static{
        ResourceLoaders.put(Image.class, ResourceLoader::ImageLoader);
        ResourceLoaders.put(File.class, ResourceLoader::FileLoader);
    }

}
