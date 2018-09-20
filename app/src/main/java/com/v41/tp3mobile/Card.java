package com.v41.tp3mobile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Card implements Serializable {

    private final Integer id;
    private  String special;
    private  Integer mana;
    private final String name;
    private  Integer defense;
    private  Integer attack;
    private final String rarity;
    private final String type;
    private final String imgPath;

    @JsonCreator
    public Card(@JsonProperty("id")Integer id,
                @JsonProperty("name")String name,
                @JsonProperty("mana")Integer mana,
                @JsonProperty("attack")Integer attack,
                @JsonProperty("defense")Integer defense,
                @JsonProperty("special")String special,
                @JsonProperty("rarity")String rarity,
                @JsonProperty("type")String type,
                @JsonProperty("imgPath")String imgPath) {

        this.id = id;
        this.attack = attack;
        this.defense = defense;
        this.imgPath = imgPath;
        this.mana = mana;
        this.special = special;
        this.name = name;
        this.rarity = rarity;
        this.type = type;
    }

    @JsonProperty("special")
    public String getSpecial() {
        return special;
    }

    @JsonProperty("mana")
    public Integer getMana() {
        return mana;
    }

    @JsonProperty("defense")
    public Integer getDefense() {
        return defense;
    }

    @JsonProperty("attack")
    public Integer getAttack() {
        return attack;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("imgPath")
    public String getImgPath() {
        return imgPath;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("rarity")
    public String getRarity() {
        return rarity;
    }

    @JsonIgnore
    public String getRarityPathFromRarity() {
        Integer rarity = Integer.parseInt(getRarity());
        switch (rarity) {
            case 1:
                return "img_rarity_1";
            case 2:
                return "img_rarity_2";
            case 3:
                return "img_rarity_3";
            case 4:
                return "img_rarity_4";
            case 5:
                return "img_rarity_5";
        }
        return null;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }
}
