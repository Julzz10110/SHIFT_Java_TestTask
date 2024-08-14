package org.example;

import org.example.input.InputFilesHandler;
import org.example.output.OutputProxy;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Класс-посредник, реализующий взаимодействие между <b>InputFilesHandler</b> и <b>OutputProxy</b>.
 * @autor Julzz10110
 * @version 1.0
 */
public class TypeFilterMediator {

    /** Обработчик входных файлов */
    private final InputFilesHandler inputFilesHandler;
    /** Массив прокси-файлов выходных данных для целочисленных, вещественных и строковых данных соответственно. */
    OutputProxy[] outputProxies = new OutputProxy[3];

    /**
     * Конструктор - создание нового объекта
     * @param inputFilesHandler - обработчик входных файлов
     * @param intP - прокси-файл для целочисленных данных
     * @param floatP - прокси-файл для вещественных данных
     * @param strP - прокси-файл для строковых данных
     * @param outputs - дополнительные прокси-файлы (может быть полезно в будущем)
     */
    public TypeFilterMediator(InputFilesHandler inputFilesHandler, OutputProxy<Integer> intP, OutputProxy<Float> floatP, OutputProxy<String> strP, OutputProxy... outputs) {
        this.inputFilesHandler = inputFilesHandler;
        outputProxies[0] = intP;
        outputProxies[1] = floatP;
        outputProxies[2] = strP;
    }

    /**
     * Процедура обработки (фильтрации) данных из входных файлов,
     * содержащихся в {@link TypeFilterMediator#inputFilesHandler}
     */
    public void processInput() {
        String line = "";
        while (inputFilesHandler.getDumpResidue() > 0)  {
            try {
                line = inputFilesHandler.getLine();
                if(line != null){
                    line = line.replaceAll("^\\s+", "").strip();
                    //if (!line.matches("-?\\d+(\\.\\d+)?")) {
                    //    outputProxies[2].update(line);
                    //}
                    if (StringCasting.isReal(line)) {
                        outputProxies[1].update(StringCasting.toReal(line));
                    }
                    else if (StringCasting.isInteger(line)) {
                        outputProxies[0].update(StringCasting.toInteger(line));

                    } else outputProxies[2].update(line);
                }
            /* } catch (NumberFormatException nfe) {
                if (nfe.getMessage().contains("neither a decimal digit number, " +
                        "decimal point, nor \"e\" notation exponential mark")) {
                    outputProxies[2].update(line.replaceAll("^\\s+", ""));
                }
                */
                
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (var outputProxy : outputProxies) {
            try {
                if (CLIProcessor.getCommandLine().hasOption("a")) {
                    try {
                        outputProxy.presetContentList();
                        outputProxy.appendToFile();
                    } catch (RuntimeException re) {
                        System.out.println("Целевой выходной файл " + outputProxy.getFilePath() + " не найден. Данный файл будет создан.");
                        outputProxy.writeFile();
                    }
                }
                else outputProxy.writeFile();
                if (CLIProcessor.getCommandLine().hasOption("f")) outputProxy.getFullStats();
                else if (CLIProcessor.getCommandLine().hasOption("s")) outputProxy.getShortStats();
            } catch (NullPointerException npe) {
                System.out.println(npe.getMessage());
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }
}
