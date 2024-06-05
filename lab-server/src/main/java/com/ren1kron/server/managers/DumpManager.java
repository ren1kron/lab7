
package com.ren1kron.server.managers;


import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import com.ren1kron.common.console.Console;
import com.ren1kron.common.models.*;
//import com.opecsv.exceptions.CsvValidationException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;



/**
 * Convert collection to CSV and CSV to collection
 * @author ren1kron
 */
public class DumpManager {
    // TODO right now outputting here realized using Console, which is in client part. Later I will make all on loggers,
    //  but, if i will need to put server on helios before adding logger, I'll have to add console in server.

    //    Чтение данных из файла необходимо реализовать с помощью класса java.io.FileReader
//    Запись данных в файл необходимо реализовать с помощью класса java.io.OutputStreamWriter
    private final static String[] strings = {"key", "id", "name", "organization", "position", "status", "salary", "coordinates", "date of appointment", "birthday"};
    private final String fileName;
    private final Console console;

    // console.printError("The entered file could not be found");

    CSVParser parser = new CSVParserBuilder()
            .withSeparator(';')
            .build();

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    /**
     * Gets the collection from CSV
     * @param map gotten collection(map)
     */
    // java.io.FileReader should be used here
    public void readCsv(Map<Integer, Worker> map) {
        try {
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(fileName))
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                Worker worker = new Worker();
                worker.setKey(Integer.parseInt(line[0])); // key;
                worker.setId(Integer.parseInt(line[1])); // id
                worker.setName(line[2]); // name
                worker.setOrganization(line[3].equals("null") ? null : new Organization(line[3])); // org
                worker.setPosition(Position.valueOf(line[4])); // pos
//                worker.setStatus(Status.valueOf(line[5])); // status
                worker.setStatus(line[5].equals("null") ? null : Status.valueOf(line[5])); // status
                worker.setSalary(Float.parseFloat(line[6])); // salary
                worker.setCoordinates(new Coordinates(line[7])); // coords
                Date creationDate;
                SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                // ACHTUNG!!! WARNING!!! It is very important to add 'locale' in the line above. I hadn't done it and my program hadn't worked on Helios until I did. It was hell of a hard problem to fix.
                try {
                    creationDate = formatter.parse(line[8]);
                } catch (ParseException pe) {
                    creationDate = null;
                }
                worker.setCreationDate(creationDate); // date of appointment
                LocalDate startDate;
                try {
                    startDate = LocalDate.parse(line[9], DateTimeFormatter.ISO_DATE);
                } catch (DateTimeParseException dtpe) {
                    startDate = null;
                }
                worker.setStartDate(startDate); // birthday
                map.put(worker.getKey(), worker);
//                if (worker.validate()) map.put(worker.getKey(), worker);
//                else console.printError("Worker with key " + worker.getKey() + " has invalid values!!");
//                else throw new RuntimeException();
            }
            if (map != null) console.println("Collection was successfully downloaded!");
//            if (map != null) System.out.println("Collection was successfully downloaded!");
        } catch (IOException ioe) {
            console.printError("The entered file could not be found");
        } catch (CsvValidationException cve) {
            console.printError("The entered csv-file is not valid");
        } catch (NullPointerException e) {
            console.printError("Looks like you don't have permission to open selected file");
        }
    }


    /**
     * Saves the collection to CSV
     * @param map saved collection(map)
     */
    // java.io.OutputStreamWriter should be used here
    public void writeCsv(Map<Integer, Worker> map) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName));
            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                    .withParser(parser)
                    .build();
            csvWriter.writeNext(strings);
            for (var worker: map.values()) {
//                var worker = map.get(key);
                csvWriter.writeNext(Worker.toArray(worker));
            }
            try {
                csvWriter.flush();
                csvWriter.close();
                console.println("Collection successfully saved in file!");
            } catch (IOException e) {
                console.printError("Unexpected error while saving file");
            }
        } catch (FileNotFoundException e) {
            console.printError("File with inserted name was never founded");
        }
    }
}
