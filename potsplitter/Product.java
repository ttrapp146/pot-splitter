package com.ttrapp14622.potsplitter;

public class Product {
    //items used in recycler view to display pot splits

    private int id;
    private String split;

    public Product(int id, String split) {
        this.id = id;
        this.split = split;
    }

    public int getId() {
        return id;
    }

    public String getSplit() {
        return split;
    }
}
