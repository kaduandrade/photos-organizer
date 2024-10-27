package com.kaduandrade;

public enum FileTypes {

    NEF("Nikon Electronic Format", "Imagens", ".nef"),
    JPG("Joint Photographic Experts Group", "Imagens", ".jpg"),
    JPEG("Joint Photographic Experts Group", "Imagens", ".jpeg"),
    PNG("Portable Network Graphics", "Imagens", ".png"),
    MOV("Portable Network Graphics", "Videos", ".mov"),
    AVI("Portable Network Graphics", "Videos", ".avi"),
    PSD("Photoshop", "Imagens", ".psd");


    private final String description;
    private final String descriptionType;
    private final String extension;

    private FileTypes(String description, String descriptionType, String extension) {
        this.description = description;
        this.descriptionType = descriptionType;
        this.extension = extension;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public String getExtension() {
        return extension;
    }

}
