package org.example;
import org.example.input.InputFilesHandler;
import org.example.input.InputFilesHandlerConfig;
import org.example.output.OutputProxy;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Основной класс программы.
 * @autor Julzz10110
 * @version 1.0
 */
public class Main {

    /** Базовые значения имен выходных файлов */
    static final String INTEGERS_FILENAME = "integers.txt";
    static final String FLOATS_FILENAME = "floats.txt";
    static final String STRINGS_FILENAME = "strings.txt";

    static String prefix = "", intsFullPath, floatsFullPath, strsFullPath;

    static ArrayList<String> inputFiles = new ArrayList<>();

    /**
     * Метод main (точка входа в программу)
     */
    public static void main(String[] args) {
        try {
            CLIProcessor.processCommandLine(args);
        } catch (NullPointerException npe) {
            System.out.println("Введенная команда не может быть обработана. " +
                            "Чтобы посмотреть справочную информацию по использованию программы, выполните ее с флагом 'h'.");
            System.exit(0);
        }

        intsFullPath += prefix + INTEGERS_FILENAME;
        floatsFullPath += prefix + FLOATS_FILENAME;
        strsFullPath += prefix + STRINGS_FILENAME;

        String outIntsPath = Paths.get(intsFullPath).toAbsolutePath().toString();
        String outFloatsPath = Paths.get(floatsFullPath).toAbsolutePath().toString();
        String outStrsPath = Paths.get(strsFullPath).toAbsolutePath().toString();

        String[] inputFilesArray = Arrays.copyOf(inputFiles.toArray(),
                inputFiles.size(), String[].class);
        var config = new InputFilesHandlerConfig(inputFilesArray);
        var fl = InputFilesHandler.getInstance(config);

        OutputProxy<Integer> integersOutput = new OutputProxy<>(outIntsPath);
        OutputProxy<Float> floatsOutput = new OutputProxy<>(outFloatsPath);
        OutputProxy<String> stringsOutput = new OutputProxy<>(outStrsPath);

        TypeFilterMediator typeFilter = new TypeFilterMediator(fl,
                integersOutput, floatsOutput, stringsOutput);
        typeFilter.processInput();
    }
}