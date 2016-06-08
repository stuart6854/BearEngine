package main.java.org.bearengine.font;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.*;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Stuart on 04/06/2016.
 */
public class Font {

    private static FontChar[] chars;

    private static Texture[] fontTexture;
    private static java.awt.Font font;
    private static FontMetrics fontMetrics;

    public static void CreateFont(){
        chars = new FontChar[256];
        try {
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, Font.class.getResourceAsStream("/main/java/resources/fonts/Open-Sans/OpenSans-Regular.ttf"));

            Debug.log("Font -> Font Name: " + font.getFontName());

            font = font.deriveFont(java.awt.Font.PLAIN, 20);

            CreateSet();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private static void CreateSet(){
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = tmp.createGraphics();

        g.setFont(font);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        fontMetrics = g.getFontMetrics();

        int posX = 0, posY = 0;
        int page = 0;

        final int padding = fontMetrics.getMaxAdvance();
        final int maxTexWidth = 1024;
        final int maxTexHeight = 1024;

        ArrayList<Texture> pages = new ArrayList<>();

        for(int i = 0; i < 256; i++){
            char chr = (char)i;
            FontChar fontChar = new FontChar();
    
            if(posX + 2 * padding > maxTexWidth){
                posX = 0;
                posY += fontMetrics.getHeight() + padding;
            }
            if(posY + 2 * padding > maxTexHeight){
                posX = posY = 0;
                page++;
            }
    
            fontChar.advance = fontMetrics.stringWidth("" + chr);
            fontChar.padding = padding;
            fontChar.page = page;
    
            fontChar.x = posX;
            fontChar.y = posY;
            fontChar.w = fontChar.advance + (padding);
            fontChar.h = fontMetrics.getHeight();
    
            chars[i] = fontChar;

            posX += fontChar.w;
        }

        g.dispose();

        BufferedImage pageImage = new BufferedImage(maxTexWidth, maxTexHeight, BufferedImage.TYPE_INT_ARGB);
        g = pageImage.createGraphics();

        g.setFont(font);
        g.setColor(java.awt.Color.BLACK);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        page = 0;

        for(int i = 0; i < 256; i++){
            FontChar fontChar = chars[i];

            if(page != fontChar.page){
                g.dispose();

                pages.add(new Texture().UploadTexture(Image.FromBufferedImage(pageImage)));

                pageImage = new BufferedImage(maxTexWidth, maxTexHeight, BufferedImage.TYPE_INT_ARGB);
                g = pageImage.createGraphics();

                g.setFont(font);
                g.setColor(java.awt.Color.BLACK);

                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                page = fontChar.page;
            }

            g.drawString(String.valueOf((char)i), chars[i].x + padding, chars[i].y + fontMetrics.getAscent());
        }

        g.dispose();

        //Dump image to a byte buffer
        InputStream is = null;
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(pageImage, "png", out);
            //ImageIO.write(pageImage, "png", new File("page.png"));//Snippets Creates image in Projects Root called page.png
            out.flush();
            is = new ByteArrayInputStream(out.toByteArray());

            BufferedImage image = ImageIO.read(is);

            Image img = Image.FromBufferedImage(image);
            img.Name = font.getFontName().replace(" ", "") + "_Texture";

            pages.add(new Texture().UploadTexture(img));

            fontTexture = new Texture[pages.size()];
            fontTexture = pages.toArray(fontTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Mesh CreateMesh(String text, float x, float y, Color color){
        Mesh mesh = new Mesh();

        List<Integer> indices = new ArrayList<>();
        List<Float> vertices = new ArrayList<>();
        List<Float> uvs = new ArrayList<>();

        float startX = x;

        Texture page = null;
        int i = 0;
        for(char chr : text.toCharArray()){
            FontChar c = chars[(int)chr];

            if(chr == '\n'){
                y += fontMetrics.getHeight();
                x = startX;
                continue;
            }

            Texture charPage = fontTexture[c.page];

            if(page == null || page != charPage){
                page = charPage;
            }

            float minU = (float)c.x / (float)page.Width;
            float maxU = (float)(c.x + c.w) / (float)page.Width;
            float minV = 1 - (float)c.y / (float)page.Height;
            float maxV = 1 - (float)(c.y + c.h) / (float)page.Height;

            //Top-Left
            vertices.add(x - (c.padding * 2));
            vertices.add(y + c.h);
            vertices.add(0f);

            uvs.add(minU);
            uvs.add(maxV);

            //Bottom-Left
            vertices.add(x - (c.padding * 2));
            vertices.add(y);
            vertices.add(0f);

            uvs.add(minU);
            uvs.add(minV);

            //Bottom-Right
            vertices.add(x + c.w - (c.padding * 2));
            vertices.add(y);
            vertices.add(0f);

            uvs.add(maxU);
            uvs.add(minV);

            //Top-Right
            vertices.add(x + c.w - (c.padding * 2));
            vertices.add(y + c.h);
            vertices.add(0f);

            uvs.add(maxU);
            uvs.add(maxV);

            //Indices
            //Tri 1
            indices.add(i * 4);
            indices.add(i * 4 + 1);
            indices.add(i * 4 + 2);

            //Tri 2
            indices.add(i * 4);
            indices.add(i * 4 + 2);
            indices.add(i * 4 + 3);

            x += c.w - c.padding;
            i++;
        }

        mesh.Mesh_Name = "TextMesh_" + text.replace(" ", "-").replace("\n", "/n");
        mesh.SetIndices(indices);
        mesh.SetVertices(vertices);
        mesh.SetUVs(uvs);
        mesh.CreateRenderModel();
        mesh.material = new Material();
        mesh.material.SetTexture(page);

        return mesh;
    }

}
