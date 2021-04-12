package com.kaduandrade;

import com.drew.metadata.Tag;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileProcessor {

	private final static String slash = "\\";
	private final static Map<Integer, String> meses = new HashMap<>();
	static {
		meses.put(0, "01");
		meses.put(1, "02");
		meses.put(2, "03");
		meses.put(3, "04");
		meses.put(4, "05");
		meses.put(5, "06");
		meses.put(6, "07");
		meses.put(7, "08");
		meses.put(8, "09");
		meses.put(9, "10");
		meses.put(10, "11");
		meses.put(11, "12");
	}

	public static Date parseDate(String inputDate) {

		Date outputDate = null;
		String[] possibleDateFormats = {
			"yyyy.MM.dd G 'at' HH:mm:ss z",
			"yyyyMMdd",
			"EEE, MMM d, ''yy",
			"yyyy:MM:dd HH:mm:ss",
			"E MMM dd HH:mm:ss XXX yyyy",
			"MMM dd HH:mm:ss XXX yyyy",
			"dd HH:mm:ss XXX yyyy",
			"yyyy:MM:dd",
			"hh 'o''clock' a, zzzz",
			"K:mm a, z",
			"yyyyy.MMMMM.dd GGG hh:mm aaa",
			"EEE, d MMM yyyy HH:mm:ss Z",
			"yyMMddHHmmssZ",
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ",
			"yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
			"YYYY-'W'ww-u",
			"EEE, dd MMM yyyy HH:mm:ss z",
			"EEE, dd MMM yyyy HH:mm zzzz",
			"yyyy-MM-dd'T'HH:mm:ssZ",
			"yyyy-MM-dd'T'HH:mm:ss.SSSzzzz",
			"yyyy-MM-dd'T'HH:mm:sszzzz",
			"yyyy-MM-dd'T'HH:mm:ss z",
			"yyyy-MM-dd'T'HH:mm:ssz",
			"yyyy-MM-dd'T'HH:mm:ss",
			"yyyy-MM-dd'T'HHmmss.SSSz",
			"yyyy-MM-dd'T'HH:mm:ssXXX",
			"yyyy-MM-dd'T'HH:mmXXX",
			"yyyy-MM-dd'T'HH:mm:ss.SS",
			"yyyy-MM-dd",
			"yyyyMMdd",
			"dd/MM/yy",
			"dd/MM/yyyy"
		};

		try {
			outputDate = DateUtils.parseDate(inputDate, possibleDateFormats);
		} catch (ParseException e) {
			System.out.println("erro: "+inputDate);
		}

		return outputDate;

	}

	public static String formatDate(Date date, String requiredDateFormat) {
		SimpleDateFormat df = new SimpleDateFormat(requiredDateFormat);
		String outputDateFormatted = df.format(date);
		return outputDateFormatted;
	}

	public static void main(String[] args) throws ParseException {
		parseDate("sexta fev 18 14:46:10 +00:00 2005");
		parseDate("fev 18 14:46:10 +00:00 2005");
		parseDate("18 14:46:10 +00:00 2005");

	}

	public static FileTypes getFileType(File file) {
		return getFileType(file, Arrays.asList(FileTypes.values().clone()));
	}

	public static FileTypes getFileType(OptionsConfig optionsConfig) {
		return getFileType(optionsConfig.getFile(), Arrays.asList(FileTypes.values().clone()));
	}

	public static FileTypes getFileType(File file, List<FileTypes> tiposDeArquivo) {
		for (FileTypes enumTiposDeArquivo : tiposDeArquivo) {
			if (file.getName().toLowerCase().contains(enumTiposDeArquivo.getExtension().toLowerCase())) {
				return enumTiposDeArquivo;
			}
		}
		return null;
	}

	public static Calendar getCalendar(FileTime date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.format(date.toMillis());
		return sdf.getCalendar();
	}

	public static Calendar getCalendar(Tag tag) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(FileProcessor.parseDate(tag.getDescription()));
		return cal;
	}

	public static void proccess(OptionsConfig optionsConfig) {

		File file = optionsConfig.getFile();
		if (getFileType(optionsConfig) != null) {
			String extension = file.getName().toUpperCase().substring(file.getName().length()-3, file.getName().length());
			String fileName = file.getName().toUpperCase().substring(0, file.getName().length()-4);
			Calendar date = optionsConfig.getDate();
			String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
			if (date.get(Calendar.DAY_OF_MONTH) < 10) {
				day = "0" + day;
			}
			String month = meses.get(date.get(Calendar.MONTH));
			String year = String.valueOf(date.get(Calendar.YEAR));
			String hour = String.valueOf(date.get(Calendar.HOUR_OF_DAY));
			if (date.get(Calendar.HOUR_OF_DAY) < 10) {
				hour = "0" + hour;
			}
			String minute = String.valueOf(date.get(Calendar.MINUTE));
			if (date.get(Calendar.MINUTE) < 10) {
				minute = "0" + minute;
			}
			String newFileName = fileName
					.concat("-")
					.concat(year)
					.concat(month)
					.concat(day)
					.concat("-")
					.concat(hour)
					.concat(minute)
					.concat("."+extension);

			StringBuffer targetFolderName = new StringBuffer();
			targetFolderName
					.append(optionsConfig.getTargetFolder())
					.append(slash)
					.append(getFileType(optionsConfig).getDescriptionType())
					.append(slash)
					.append(extension)
					.append(slash)
					.append(year)
					.append(slash)
					.append(month);
			if (optionsConfig.isCreateFolderByDate()) targetFolderName.append(slash).append(day);
			createFolder(targetFolderName.toString());
			targetFolderName.append(slash).append(newFileName);

			Path pathSourceFile = Paths.get(file.getPath());
			Path pathTargetFile = Paths.get(targetFolderName.toString());
			try {
				if (optionsConfig.isMoveOriginal()) {
					Files.move(pathSourceFile, pathTargetFile, StandardCopyOption.COPY_ATTRIBUTES);
					System.out.println("Arquivo movido com sucesso: "+newFileName);
				} else {
					Files.copy(pathSourceFile, pathTargetFile, StandardCopyOption.COPY_ATTRIBUTES);
					System.out.println("Arquivo copiado com sucesso: "+newFileName);
				}
			} catch (FileAlreadyExistsException e) {
				//copyWithDifferentName(file, targetFolderName.toString(), file.getName());
				System.out.println("Arquivo existente não será sobrescrito:  "+e.getMessage());
			} catch (IOException e) {
				System.out.println("proccess "+e);
			}

		}

	}

	public static void copyWithDifferentName(File sourceFile, String targetFolderName, String newFileName) {
		if (sourceFile == null || newFileName == null || newFileName.isEmpty()) {
			return;
		}
		String extension = "";
		if (sourceFile.getName().split("\\.").length > 1) {
			extension = sourceFile.getName().split("\\.")[sourceFile.getName().split("\\.").length - 1];
		}
		String newPath = targetFolderName.substring(0, targetFolderName.length() - sourceFile.getName().length()) + newFileName;
		if (!extension.isEmpty()) {
			newPath += "." + extension;
		}
		try (OutputStream out = new FileOutputStream(newPath)) {
			Files.copy(sourceFile.toPath(), out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFolder(String nomeDiretorio) {
		for (int i = 0; i < 3; i++) {
			File diretorio = new File(nomeDiretorio);
			if (!diretorio.exists()) {
				if (diretorio.mkdirs()) {
					System.out.println(nomeDiretorio);
				} else {
					System.out.println("Failed to create multiple directories!");
				}
			}
		}
	}

}
