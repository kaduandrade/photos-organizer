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
	
	private final static String sourceFolder = "C:\\temp\\tmp";
	private final static String targetFolder = "C:\\Mídia";
	private final static Path pathSourceFile = Paths.get(sourceFolder);
	private final static List<FileTypes> filesAllowed = Arrays.asList(FileTypes.JPG, FileTypes.NEF, FileTypes.MOV, FileTypes.AVI);

	/**
	 * @param args
	 * @throws ImageProcessingException
	 */
	public static void main(String[] args) {

		try {
			
			List<File> sourceFileList = Files.walk(pathSourceFile)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

			for (File file : sourceFileList) {
				List<Calendar> datesList = new ArrayList<>();
				if (FileProcessor.getFileType(file) != null) {
					addDatesToMetaData(file, datesList);
					Collections.sort(datesList);
					OptionsConfig copyFile = new OptionsConfig(
							datesList.get(0),
							file,
							targetFolder,
							false,
							false,
							filesAllowed);
					FileProcessor.proccess(copyFile);
                }
			}
		} catch (ImageProcessingException e) {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addDatesToMetaData(File file, List<Calendar> dates) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().toLowerCase().contains("Date/Time Original".toLowerCase())) {
                    dates.add(FileProcessor.getCalendar(tag));
                }
            }
		}
		if (dates.isEmpty()) {
			addDatesToFile(file, dates);
		}
	}

	private static void addDatesToFile(File file, List<Calendar> dates) throws IOException {
		Path filepath = Paths.get(file.getCanonicalPath());
		BasicFileAttributes attr = Files.readAttributes(filepath, BasicFileAttributes.class);
		dates.add(FileProcessor.getCalendar(attr.creationTime()));
		dates.add(FileProcessor.getCalendar(attr.lastModifiedTime()));
	}

}
