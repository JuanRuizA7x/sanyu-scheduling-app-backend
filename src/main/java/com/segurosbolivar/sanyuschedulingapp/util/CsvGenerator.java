package com.segurosbolivar.sanyuschedulingapp.util;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class CsvGenerator {

    /**
     * Generates a CSV file as a byte array from a list of objects using the provided headers and name mappings.
     *
     * @param dataList     The list of objects to convert to CSV.
     * @param headers      An array of header strings for the CSV columns.
     * @param nameMapping  An array of property name mappings for the objects.
     * @param <T>          The type of objects in the list.
     * @return A byte array containing the CSV data.
     */
    public static <T> byte[] generateCsv(List<T> dataList, String[] headers, String[] nameMapping) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            Writer writer = new OutputStreamWriter(byteArrayOutputStream);

            try (CsvBeanWriter csvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE)) {

                csvBeanWriter.writeHeader(headers);

                for (T item : dataList) {
                    csvBeanWriter.write(item, nameMapping);
                }

            }

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}