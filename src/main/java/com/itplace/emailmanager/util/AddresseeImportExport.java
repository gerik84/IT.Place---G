package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.service.AddresseeService;
import com.itplace.emailmanager.service.DepartmentService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class AddresseeImportExport {

    public List<Addressee> importFromReader(Reader reader) throws IOException {
        List<Addressee> addresseeList = new ArrayList<>();
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> linesList = csvReader.readAll();
        csvReader.close();
        linesList.forEach(line -> {
            if (EmailValidator.getInstance().isValid(line[0])) {
                Addressee addressee = Addressee.builder()
                        .email(line[0]).name(line[1]).build();
                addresseeList.add(addressee);
            }
        });
        return addresseeList;
    }

    public boolean exportToFile(List<Addressee> addresseeList, String stringPath) {
        try {
            File outputFile = new File(stringPath);
            if (outputFile.createNewFile()) {
                List<String[]> linesToWrite = new ArrayList<>();
                CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile));
                addresseeList.forEach(addressee -> {
                    List<String> line = new ArrayList<>();
                    line.add(addressee.getEmail());
                    line.add(addressee.getName());
                    line.add(addressee.getDepartment().getName());
                    linesToWrite.add(line.toArray(new String[0]));
                });
                csvWriter.writeAll(linesToWrite);
                csvWriter.close();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}