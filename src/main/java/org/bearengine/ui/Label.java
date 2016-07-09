package main.java.org.bearengine.ui;

import main.java.org.bearengine.font.*;
import main.java.org.bearengine.font.Character;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Material;
import main.java.org.bearengine.graphics.types.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 10/06/2016.
 */
public class Label extends UIObject {

    public static int ASCII_SPACE = 32;
    public static int ASCII_NEWLINE = 10;

    private String Text;
    private List<Line> lines;

    private Font font;

    public Label(String text, Font font){
        super();
        this.Text = text;
        this.font = font;

        BuildMesh();
    }

    @Override
    public void BuildMesh() {
        if(this.Text == null || this.Text == "") return;

        this.lines = CreateStructure(this.Text);
        BuildMeshFromStructure(this.lines);
        super.PixelWidth = GetLongestLineWidth();
        super.PixelHeight = GetLinesHeight();

        super.CreateDebugMesh();
    }

    private List<Line> CreateStructure(String text){
        List<Line> lines = new ArrayList<>();

        float lineHeight = font.FontFile.LineHeight * font.GetRelativeFontSize();
        float spaceWidth = font.FontFile.SpaceWidth * font.GetRelativeFontSize();

        char[] chars = text.toCharArray();
        Line currentLine = new Line(lineHeight, spaceWidth);
        Word currentWord = new Word(font.GetRelativeFontSize());
        for(char chr : chars){
            int ascii = (int)chr;

            if(ascii == ASCII_SPACE){ //32 = SPACE_ASCII
                currentLine.AddWord(currentWord);
                currentWord = new Word(font.GetRelativeFontSize());
            }else if(ascii == ASCII_NEWLINE){
                currentLine.AddWord(currentWord);
                currentWord = new Word(font.GetRelativeFontSize());
                lines.add(currentLine);
                currentLine = new Line(lineHeight, spaceWidth);
            }else{
                currentWord.AddCharacter(font.FontFile.characters[ascii]);
            }
        }
        currentLine.AddWord(currentWord);
        lines.add(currentLine);

        return lines;
    }

    private void BuildMeshFromStructure(List<Line> lines){
        Mesh mesh = new Mesh();

        float paddingX = font.FontFile.PaddingWidth;

        List<Integer> indices = new ArrayList<>();
        List<Float> vertices = new ArrayList<>();
        List<Float> uvs = new ArrayList<>();

        float x = 1, y = 0;

        int i = 0;
        for(Line line : lines){
            for(Word word : line.GetWords()){
                for(Character character : word.GetCharacters()){
                    AddCharacterVertices(x, y, character, vertices);
                    AddCharacterUVs(character, uvs);
                    AddCharacterIndices(i, indices);
                    x += (character.xAdvance - paddingX) * font.GetRelativeFontSize();
                    i++;
                }

                x += line.GetSpaceWidth();
            }

            x = 0;
            y -= line.GetLineHeight();
        }

        mesh.SetVertices(vertices);
        mesh.SetUVs(uvs);
        mesh.SetIndices(indices);
        mesh.CreateRenderModel();
        mesh.material = new Material();
        mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI_SDF;
        mesh.material.SetTexture(font.FontAtlas);

        setMesh(mesh);
    }

    private void AddCharacterVertices(float cursorX, float cursorY, Character character, List<Float> vertices){
        float x = cursorX + (character.xOffset * font.GetRelativeFontSize());
        float y = cursorY - (character.yOffset * font.GetRelativeFontSize());
        float maxX = x + (character.sizeX * font.GetRelativeFontSize());
        float maxY = y - (character.sizeY * font.GetRelativeFontSize());

        float properX = (2 * x) - 1;
        float properY = (-2 * y) + 1;
        float properMaxX = (2 * maxX) - 1;
        float properMaxY = (-2 * maxY) + 1;
        AddVertices(properX, properY, properMaxX, properMaxY, vertices);
    }

    private void AddVertices(float x, float y,  float maxX, float maxY, List<Float> vertices){
        vertices.add(x);
        vertices.add(y);
        vertices.add(0f);

        //Bottom-Left
        vertices.add(x);
        vertices.add(maxY);
        vertices.add(0f);

        //Bottom-Right
        vertices.add(maxX);
        vertices.add(maxY);
        vertices.add(0f);

        //Top-Right
        vertices.add(maxX);
        vertices.add(y);
        vertices.add(0f);
    }

    private void AddCharacterUVs(Character character, List<Float> uvs){
        uvs.add(character.xUV);
        uvs.add(character.yUV);

        uvs.add(character.xUV);
        uvs.add(character.yMaxUV);

        uvs.add(character.xMaxUV);
        uvs.add(character.yMaxUV);

        uvs.add(character.xMaxUV);
        uvs.add(character.yUV);
    }

    private void AddCharacterIndices(int charNum, List<Integer> indices){
        //Tri 1
        indices.add(charNum * 4);
        indices.add(charNum * 4 + 1);
        indices.add(charNum * 4 + 2);

        //Tri 2
        indices.add(charNum * 4);
        indices.add(charNum * 4 + 2);
        indices.add(charNum * 4 + 3);
    }

    public void SetText(String text){
        this.Text = text;
        BuildMesh();
    }

    private float GetLongestLineWidth(){
        float width = 0;
        for(Line line : lines){
            if(line.GetLineWidth() > width)
                width = line.GetLineWidth();
        }
        return width + font.FontFile.PaddingWidth;
    }

    private float GetLinesHeight(){
        float height = 0;
        for(Line line : lines){
            height += line.GetLineHeight() + font.FontFile.PaddingHeight;
        }
        return height;
    }

}
