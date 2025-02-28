package model;

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

    public String getName() { return name; }
    public String getProfession() { return profession; }
    public String getBiography() { return biography; }
    public String getAchievements() { return achievements; }
    public List<String> getImages() { return images; }
    public List<String> getVideos() { return videos; }
}