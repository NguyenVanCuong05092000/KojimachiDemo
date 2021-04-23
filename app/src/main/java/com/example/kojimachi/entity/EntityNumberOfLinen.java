package com.example.kojimachi.entity;

import java.util.ArrayList;

public class EntityNumberOfLinen {
    public ArrayList<EntityOptionOfLinen> entityOptionOfLinenArrayList;
    public int numberPieces;

    public EntityNumberOfLinen(ArrayList<EntityOptionOfLinen> entityOptionOfLinenArrayList, int numberPieces) {
        this.entityOptionOfLinenArrayList = entityOptionOfLinenArrayList;
        this.numberPieces = numberPieces;
    }
}
