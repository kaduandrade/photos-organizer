package com.kaduandrade;

import com.drew.imaging.ImageProcessingException;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PhotosOrganizer {

	private static final String SOURCE_FOLDER = "D:\\TESTE-JAVA\\tmp";
	private static final String TARGET_FOLDER = "D:\\TESTE-JAVA\\target";

	public static void main(String[] args) {
		try {
			FileHandler fileHandler = new FileHandler(SOURCE_FOLDER);
			MetadataExtractor metadataExtractor = new MetadataExtractor();
			List<File> sourceFiles = fileHandler.getSourceFiles();
			processFiles(sourceFiles, metadataExtractor);
		} catch (ImageProcessingException | IOException e) {
			logError(e);
		}
	}

	private static void processFiles(List<File> files, MetadataExtractor metadataExtractor)
			throws ImageProcessingException, IOException {
		for (File file : files) {
			if (FileProcessor.getFileType(file) != null) {
				List<Calendar> dates = metadataExtractor.extractDates(file);
				if (!dates.isEmpty()) {
					Collections.sort(dates);
					processFile(file, dates.getFirst());
				}
			}
		}
	}

	private static void processFile(File file, Calendar date) {
		OptionsConfig copyFile = new OptionsConfig(
				date,
				file,
				TARGET_FOLDER,
				false,
				false,
				FileHandler.getAllowedFileTypes()
		);
		FileProcessor.process(copyFile);
	}

	private static void logError(Exception e) {
		System.out.println(e.getMessage());
	}
}
