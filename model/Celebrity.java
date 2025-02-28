package model;

import java.util.List;

public class Celebrity {
    private String name;
    private String profession;
    private String biography;
    private List<String> achievements;
    private List<String> images;
    private List<String> videos;

    // Constructor
    public Celebrity(String name, String profession, String biography, List<String> achievements, List<String> images, List<String> videos) {
        this.name = name;
        this.profession = profession;
        this.biography = biography;
        this.achievements = achievements;
        this.images = images;
        this.videos = videos;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = achievements;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "Celebrity{" +
                "name='" + name + '\'' +
                ", profession='" + profession + '\'' +
                ", biography='" + biography + '\'' +
                ", achievements=" + achievements +
                ", images=" + images +
                ", videos=" + videos +
                '}';
    }
}
