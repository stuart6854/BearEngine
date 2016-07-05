package main.java.org.bearengine.font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 26/06/2016.
 */
public class Line {

    private float LineHeight;
    private float SpaceWidth;

    private List<Word> words;
    private int LineWidth;

    public Line(float lineHeight, float spaceWidth){
        this.LineHeight = lineHeight;
        this.SpaceWidth = spaceWidth;
        this.words = new ArrayList<>();
    }

    public void AddWord(Word word){
        this.words.add(word);
        this.LineWidth += word.GetWidth();
    }

    public List<Word> GetWords(){
        return words;
    }

    public void SetLineHeight(float height){
        this.LineHeight = height;
    }

    public float GetLineHeight(){
        return LineHeight;
    }

    public void SetSpaceWidth(float width){
        this.SpaceWidth = width;
    }

    public float GetSpaceWidth(){
        return SpaceWidth;
    }

    public float GetLineWidth(){
        return LineWidth + ((words.size() - 1) * SpaceWidth);
    }

}
