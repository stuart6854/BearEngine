package main.java.org.bearengine.font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 26/06/2016.
 */
public class Word {

    private float FontSize;

    private List<Character> characters;

    private int WordWidth;

    public Word(float fontSize){
        this.FontSize = fontSize;
        this.characters = new ArrayList<>();
    }

    public void AddCharacter(Character character){
        this.characters.add(character);
        WordWidth += character.xAdvance * FontSize;
    }

    public List<Character> GetCharacters(){
        return characters;
    }

    public int GetWidth(){
        return WordWidth;
    }

}
