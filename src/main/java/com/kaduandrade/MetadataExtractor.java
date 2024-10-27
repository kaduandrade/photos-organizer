package com.kaduandrade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class MetadataExtractor {

    private static final String DATE_TAG_NAME = "Date/Time Original".toLowerCase();

    public List<Calendar> extractDates(File file) throws ImageProcessingException, IOException {
        List<Calendar> dates = new ArrayList<>();
        addDatesFromMetadata(file, dates);
        if (dates.isEmpty()) {
            addDatesFromFileAttributes(file, dates);
        }
        return dates;
    }

    private void addDatesFromMetadata(File file, List<Calendar> dates) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().toLowerCase().contains(DATE_TAG_NAME)) {
                    dates.add(FileProcessor.getCalendar(tag));
                }
            }
        }
    }

    private void addDatesFromFileAttributes(File file, List<Calendar> dates) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        dates.add(FileProcessor.getCalendar(attributes.creationTime()));
        dates.add(FileProcessor.getCalendar(attributes.lastModifiedTime()));
    }
}
