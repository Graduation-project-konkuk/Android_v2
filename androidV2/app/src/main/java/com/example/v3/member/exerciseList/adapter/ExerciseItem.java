package com.example.v3.member.exerciseList.adapter;

public class ExerciseItem {
    String name;
    int imageName;

    public ExerciseItem(String name, int imageName) {
        this.name = name;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }
    public int getImage(){
        return imageName;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setImageName(int imageName){
        this.imageName = imageName;}

}
