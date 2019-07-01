package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Addressee;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class AddresseeImportExport {

    // TODO все ниженаписанное требует проверки!

    public List<Addressee> importFromFile(String stringPath) {
        Path path = Paths.get(stringPath);

        if (path.toFile().canRead()) {
            ColumnPositionMappingStrategy columnPositionMappingStrategy = new ColumnPositionMappingStrategy();
            columnPositionMappingStrategy.setType(Addressee.class);
            CsvToBean csvToBean = null;
            try (Reader reader = Files.newBufferedReader(path)) {
                 csvToBean = new CsvToBeanBuilder(reader).withType(Addressee.class)
                        .withMappingStrategy(columnPositionMappingStrategy).build();
            } catch (IOException e) { // игнорируем, т.к. возвращаем пустой список в случае любой неудачи
            }
            if (csvToBean != null) return csvToBean.parse();
        }
        return new ArrayList<>();
    }

    public boolean exportToFile(List<Addressee> addresseeList, String stringPath) {
        File outputFile = new File(stringPath);

        if (outputFile.isFile() && outputFile.canWrite()) {
            try (Writer writer = new FileWriter(outputFile)) {
                StatefulBeanToCsv statefulBeanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
                statefulBeanToCsv.write(addresseeList);
            } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                return false;
            }
        }
        return false;
    }
}