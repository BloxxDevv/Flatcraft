package com.bloxxdev.flatcraft.blocks;

import java.io.Serializable;

public class Blockdata implements Serializable {

    private static final long serialVersionUID = 4786291024L;

    public char axis = ' ';

    public Blockdata(){}

    public void setAxis(char axis) {
        this.axis = axis;
    }

    public char getAxis() {
        return axis;
    }
}
