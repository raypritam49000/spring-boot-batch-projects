package com.sc.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.sc.config.Data;

public class FileUtils {

	private final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	private String fileName;
	private CSVReader csvReader;
	private CSVWriter csvWriter;
	private FileReader fileReader;
	private FileWriter fileWriter;
	private File file;

	public FileUtils(String fileName) {
		this.fileName = fileName;
	}

	public Data readLine() {
		try {
			if (Objects.isNull(csvReader)) {
				initReader();
			}

			String[] data = csvReader.readNext();

			if (Objects.isNull(data)) {
				return null;
			}

			return new Data(data[0], LocalDate.parse(data[1], DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		} catch (Exception e) {
			logger.error("Error while reading data in file: {}", fileName);
			return null;
		}
	}

	public void writeLine(Data data) {
		try {
			if (Objects.isNull(csvWriter)) {
				initWriter();
			}

			String[] str = new String[2];
			str[0] = data.getName();
			str[1] = data.getAge().toString();
			csvWriter.writeNext(str);
		} catch (Exception e) {
			logger.error("Error while writing data in file: {}", fileName);
		}
	}

	private void initReader() throws Exception {
		ClassLoader classLoader = this.getClass().getClassLoader();

		if (file == null) {
			file = new File(classLoader.getResource(fileName).getFile());
		}

		if (fileReader == null) {
			fileReader = new FileReader(file);
		}

		if (csvReader == null) {
			csvReader = new CSVReader(fileReader);
		}
	}

	private void initWriter() throws Exception {
		if (file == null) {
			file = new File(fileName);
			file.createNewFile();
		}

		if (fileWriter == null) {
			fileWriter = new FileWriter(file, true);
		}

		if (csvWriter == null) {
			csvWriter = new CSVWriter(fileWriter);
		}
	}

	public void closeWriter() {
		try {
			csvWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			logger.error("Error while closing writer.");
		}
	}

	public void closeReader() {
		try {
			csvReader.close();
			fileReader.close();
		} catch (IOException e) {
			logger.error("Error while closing reader.");
		}
	}

}
