package main.java.org.bearengine.utils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import java.io.File;

public class ClassLoaderDummy {

    public static void searchResource(String path) throws IOException {
        List<Root> roots = new ArrayList<>();
        for (URL url : getContext().getURLs()) {
            roots.add(new Root(url));
        }

        System.out.println();
        System.out.println(prefix() + "SEARCHING: \"" + path + "\"");

        for (int attempt = 1; attempt <= 6; attempt++)
            for (Root root : roots)
                if (root.search(path, attempt))
                    return;

        System.out.println(prefix() + "FAILED: failed to find anything like");
        System.out.println(prefix() + "               \"" + path + "\"");
        System.out.println(prefix() + "         in all classpath entries:");
        for (Root root : roots)
            if(root.entry == null)
                System.out.println(prefix() + "               SCAN FAILED FOR: \"" + root.url + "\"");
            else
                System.out.println(prefix() + "               \"" + root.entry.getAbsolutePath() + "\"");
    }

    private static class Root {
        final URL url;
        final File entry;
        final List<String> resources = new ArrayList<>();

        public Root(URL url) throws IOException {
            this.url = url;
            this.entry = visitRoot(url, resources);
        }

        public boolean search(String path, int attempt) {
            path = path.replace('\\', '/');

            // ---

            if (attempt == 1)
                for (String resource : resources) {
                    if (path.equals(resource)) {
                        System.out.println(prefix() + "SUCCESS: found resource \"" + path + "\" in root: " + entry);
                        return true;
                    }
                }

            if (attempt == 2)
                for (String resource : resources) {
                    if (path.toLowerCase().equals(resource.toLowerCase())) {
                        System.out.println(prefix() + "FOUND: similarly named resource:");
                        System.out.println(prefix() + "               \"" + resource + "\"");
                        System.out.println(prefix() + "         in classpath entry:");
                        System.out.println(prefix() + "               \"" + entry + "\"");
                        System.out.println(prefix() + "         for access use:");
                        System.out.println(prefix() + "               getResourceAsStream(\"/" + resource + "\");");
                        return true;
                    }
                }

            // ---

            if (attempt == 3)
                for (String resource : resources) {
                    String r1 = path;
                    String r2 = resource;
                    if (r1.contains("/"))
                        r1 = r1.substring(r1.lastIndexOf('/') + 1);
                    if (r2.contains("/"))
                        r2 = r2.substring(r2.lastIndexOf('/') + 1);

                    if (r1.equals(r2)) {
                        System.out.println(prefix() + "FOUND: mislocated resource:");
                        System.out.println(prefix() + "               \"" + resource + "\"");
                        System.out.println(prefix() + "         in classpath entry:");
                        System.out.println(prefix() + "               \"" + entry + "\"");
                        System.out.println(prefix() + "         for access use:");
                        System.out.println(prefix() + "               getResourceAsStream(\"/" + resource + "\");");
                        return true;
                    }
                }

            if (attempt == 4)
                for (String resource : resources) {
                    String r1 = path.toLowerCase();
                    String r2 = resource.toLowerCase();
                    if (r1.contains("/"))
                        r1 = r1.substring(r1.lastIndexOf('/') + 1);
                    if (r2.contains("/"))
                        r2 = r2.substring(r2.lastIndexOf('/') + 1);

                    if (r1.equals(r2)) {
                        System.out.println(prefix() + "FOUND: mislocated, similarly named resource:");
                        System.out.println(prefix() + "               \"" + resource + "\"");
                        System.out.println(prefix() + "         in classpath entry:");
                        System.out.println(prefix() + "               \"" + entry + "\"");
                        System.out.println(prefix() + "         for access use:");
                        System.out.println(prefix() + "               getResourceAsStream(\"/" + resource + "\");");
                        return true;
                    }
                }

            // ---

            if (attempt == 5)
                for (String resource : resources) {
                    String r1 = path;
                    String r2 = resource;
                    if (r1.contains("/"))
                        r1 = r1.substring(r1.lastIndexOf('/') + 1);
                    if (r2.contains("/"))
                        r2 = r2.substring(r2.lastIndexOf('/') + 1);
                    if (r1.contains("."))
                        r1 = r1.substring(0, r1.lastIndexOf('.'));
                    if (r2.contains("."))
                        r2 = r2.substring(0, r2.lastIndexOf('.'));

                    if (r1.equals(r2)) {
                        System.out.println(prefix() + "FOUND: resource with different extension:");
                        System.out.println(prefix() + "               \"" + resource + "\"");
                        System.out.println(prefix() + "         in classpath entry:");
                        System.out.println(prefix() + "               \"" + entry + "\"");
                        System.out.println(prefix() + "         for access use:");
                        System.out.println(prefix() + "               getResourceAsStream(\"/" + resource + "\");");
                        return true;
                    }
                }

            if (attempt == 6)
                for (String resource : resources) {
                    String r1 = path.toLowerCase();
                    String r2 = resource.toLowerCase();
                    if (r1.contains("/"))
                        r1 = r1.substring(r1.lastIndexOf('/') + 1);
                    if (r2.contains("/"))
                        r2 = r2.substring(r2.lastIndexOf('/') + 1);
                    if (r1.contains("."))
                        r1 = r1.substring(0, r1.lastIndexOf('.'));
                    if (r2.contains("."))
                        r2 = r2.substring(0, r2.lastIndexOf('.'));

                    if (r1.equals(r2)) {
                        System.out.println(prefix() + "FOUND: similarly named resource with different extension:");
                        System.out.println(prefix() + "               \"" + resource + "\"");
                        System.out.println(prefix() + "         in classpath entry:");
                        System.out.println(prefix() + "               \"" + entry + "\"");
                        System.out.println(prefix() + "         for access use:");
                        System.out.println(prefix() + "               getResourceAsStream(\"/" + resource + "\");");
                        return true;
                    }
                }

            return false;
        }
    }

    private static URLClassLoader getContext() {
        return (URLClassLoader) Thread.currentThread().getContextClassLoader();
    }

    private static File visitRoot(URL url, List<String> resources) throws IOException {
        if (!url.getProtocol().equals("file")) {
            throw new IllegalStateException();
        }
        String path = url.getPath();

        if (System.getProperty("os.name").toLowerCase().contains("win"))
            if (path.startsWith("/"))
                path = path.substring(1);

        File root = new File(path);
        if (!root.exists()) {
            System.err.println(prefix() + "failed to find classpath entry in filesystem: " + path);
            return null;
        }

        if (root.isDirectory()) {
            visitDir(root.getAbsolutePath().replace('\\', '/'), root, resources);
        } else if (root.getName().toLowerCase().endsWith(".zip")) {
            visitZip(root, resources);
        } else if (root.getName().toLowerCase().endsWith(".jar")) {
            visitZip(root, resources);
        } else {
            System.err.println(prefix() + "unknown classpath entry type: " + path);
            return null;
        }
        return root;
    }

    private static void visitDir(String root, File dir, Collection<String> out) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                visitDir(root, file, out);
            out.add(file.getAbsolutePath().replace('\\', '/').substring(root.length() + 1));
        }
    }

    private static void visitZip(File jar, Collection<String> out) throws IOException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(jar));
        while (true) {
            ZipEntry entry = zis.getNextEntry();
            if (entry == null)
                break;
            out.add(entry.getName().replace('\\', '/'));
        }
        zis.close();
    }

    private static String prefix() {
        return "[" + ClassLoaderDummy.class.getSimpleName() + "] ";
    }
}
