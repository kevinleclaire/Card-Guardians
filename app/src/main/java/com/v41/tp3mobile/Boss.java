package com.v41.tp3mobile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Boss implements Serializable{
    private final Integer id;
    private final String name;
    private  Integer defense;
    private  Integer attack;
    private final String imgPath;
    private final String roundImgPath;
    private final String backgroundImgPath;
    private final String sliderImgPath;
    private final String color;
    private final String musicPath;
    private final String hitSoundPath;

    @JsonCreator
    public Boss(@JsonProperty("id")Integer id,
                @JsonProperty("name")String name,
                @JsonProperty("defense")Integer defense,
                @JsonProperty("attack")Integer attack,
                @JsonProperty("imgPath")String imgPath,
                @JsonProperty("roundImgPath")String roundImgPath,
                @JsonProperty("backgroundImgPath")String backgroundImgPath,
                @JsonProperty("sliderImgPath")String sliderImgPath,
                @JsonProperty("color")String color,
                @JsonProperty("musicPath")String musicPath,
                @JsonProperty("musicPath")String hitSoundPath){
        this.id = id;
        this.name = name;
        this.defense = defense;
        this.attack = attack;
        this.imgPath = imgPath;
        this.roundImgPath = roundImgPath;
        this.backgroundImgPath = backgroundImgPath;
        this.sliderImgPath = sliderImgPath;
        this.color = color;
        this.musicPath = musicPath;
        this.hitSoundPath = hitSoundPath;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("defense")
    public Integer getDefense() {
        return defense;
    }

    @JsonProperty("attack")
    public Integer getAttack() {
        return attack;
    }

    @JsonProperty("imgPath")
    public String getImgPath() {
        return imgPath;
    }

    @JsonProperty("roundImgPath")
    public String getRoundImgPath() {
        return roundImgPath;
    }

    @JsonProperty("backgroundImgPath")
    public String getBackgroundImgPath() {
        return backgroundImgPath;
    }

    @JsonProperty("sliderImgPath")
    public String getSliderImgPath() { return sliderImgPath; }

    @JsonProperty("color")
    public String getcolor() { return color; }

    @JsonProperty("musicPath")
    public String getMusicPath() { return musicPath; }

    @JsonProperty("hitSoundPath")
    public String getHitSoundPath() {
        return hitSoundPath;
    }
}
