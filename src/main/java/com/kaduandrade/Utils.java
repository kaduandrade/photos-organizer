package com.kaduandrade;

import com.drew.metadata.Tag;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {

	private final static String slash = "\\";
	private final static Map<Integer, String> meses = new HashMap<>();

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

	public static boolean checkTypeFile(File file) {
		return file.getName().toLowerCase().contains(".nef".toLowerCase())
				|| file.getName().toLowerCase().contains(".jpg".toLowerCase())
				|| file.getName().toLowerCase().contains(".jpeg".toLowerCase())
				|| file.getName().toLowerCase().contains(".png".toLowerCase());
	}

	public static Calendar getCalendar(FileTime date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.format(date.toMillis());
		return sdf.getCalendar();
	}

	public static Calendar getCalendar(Tag tag) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(Utils.parseDate(tag.getDescription()));
		return cal;
	}

	public static void trataFolder(Calendar cal, File file, String destin) {

		meses.put(0, "01 - Janeiro");
		meses.put(1, "02 - Fevereiro");
		meses.put(2, "03 - Março");
		meses.put(3, "04 - Abril");
		meses.put(4, "05 - Maio");
		meses.put(5, "06 - Junho");
		meses.put(6, "07 - Julho");
		meses.put(7, "08 - Agosto");
		meses.put(8, "09 - Setembro");
		meses.put(9, "10 - Outubro");
		meses.put(10, "11 - Novembro");
		meses.put(11, "12 - Dezembro");

		String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String mes = String.valueOf(cal.get(Calendar.MONTH));
		String ano = String.valueOf(cal.get(Calendar.YEAR));
		if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
			dia = "0" + dia;
		}
		String nomeFolder = destin + slash + ano + slash + meses.get(cal.get(Calendar.MONTH)) + slash + dia;
		createDir(nomeFolder);
		Path source = Paths.get(file.getPath());
		Path target = Paths.get(nomeFolder + slash + file.getName());
		try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("trataFolder "+e);
		}

	}

	public static void createDir(String nomeFolder) {
		for (int i = 0; i < 3; i++) {
			File folder = new File(nomeFolder);
			if (!folder.exists()) {
				if (folder.mkdirs()) {
					System.out.println(nomeFolder);
				} else {
					System.out.println("Failed to create multiple directories!");
				}
			}
		}
	}

}
