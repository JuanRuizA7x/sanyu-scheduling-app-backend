package com.segurosbolivar.sanyuschedulingapp.util;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class CsvGenerator {

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