package main.java.model;

import java.util.List;

public class Celebrity {
    private String name;
    private String profession;
    private String biography;
    private String achievements;
    private List<String> images;
    private List<String> videos;

    public Celebrity(String name, String profession, String biography, String achievements, List<String> images, List<String> videos) {
        this.name = name;
        this.profession = profession;
        this.biography = biography;
        this.achievements = achievements;
        this.images = images;
        this.videos = videos;
    }

    public String getName() {
        return this.name;
    }

    public String getProfession() {
        return this.profession;
    }

    public String getBiography() {
        return this.biography;
    }

    public String getAchievements() {
        return this.achievements;
    }

    public List<String> getImages() {
        return this.images;
    }

    public List<String> getVideos() {
        return this.videos;
    }
}