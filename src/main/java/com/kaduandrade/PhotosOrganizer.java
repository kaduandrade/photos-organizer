package com.kaduandrade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class PhotosOrganizer {
	
	private final static String source = "C:\\Users\\Kadu\\Pictures";
	private final static String target = "C:\\fotos";
	private final static Path path = Paths.get(source);

	/**
	 * @param args
	 * @throws ImageProcessingException
	 */
	public static void main(String[] args) {

		try {
			
			List<File> filesInFolder = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

			for (File file : filesInFolder) {
				List<Calendar> datas = new ArrayList<Calendar>();
				if (Utils.checkTypeFile(file)) {
					addDatesMetadata(file, datas);
					Collections.sort(datas);
					Utils.trataFolder(datas.get(0), file, target);
                }
			}
		} catch (ImageProcessingException e) {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addDatesMetadata(File file, List<Calendar> datas) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().toLowerCase().contains("Date/Time Original".toLowerCase())) {
                    datas.add(Utils.getCalendar(tag));
                }
            }
		}
		if (datas.isEmpty()) {
			addDatesFile(file, datas);
		}
	}

	private static void addDatesFile(File file, List<Calendar> datas) throws IOException {
		Path filepath = Paths.get(file.getCanonicalPath());
		BasicFileAttributes attr = Files.readAttributes(filepath, BasicFileAttributes.class);
		datas.add(Utils.getCalendar(attr.creationTime()));
		datas.add(Utils.getCalendar(attr.lastModifiedTime()));
	}

}
