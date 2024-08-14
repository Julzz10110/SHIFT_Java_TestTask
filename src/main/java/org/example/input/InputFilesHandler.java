package org.example.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Singletone-класс обработчика входных файлов.
 * @autor Julzz10110
 * @version 1.0
 */
public class InputFilesHandler {

    /** Список строк из всех указанных входных файлов (пул строк) */
    private final List<LinkedList<String>> dumpArr = new LinkedList<>();
    /** Количество строк в пуле */
    private final int dumpNum;
    /** Порядковый номер строки и остаток строк в пуле соответственно */
    private int dumpId, dumpResidue;

    /** Реализация паттерна Singletone в форме "Initialization on Demand Holder" */
    private static class InstanceHolder {
        private static InputFilesHandler HOLDER_INSTANCE =
                new InputFilesHandler(new InputFilesHandlerConfig());
    }

    public static InputFilesHandler getInstance(InputFilesHandlerConfig config) {
        if (config != null) {
            InstanceHolder.HOLDER_INSTANCE = new InputFilesHandler(config);
        }
        return InstanceHolder.HOLDER_INSTANCE;
    }

    /**
     * Конструктор - создание нового объекта и заполнения строкового пула
     * @param config - объект конфигурации обработчика входных файлов
     */
    private InputFilesHandler(InputFilesHandlerConfig config) {
        String[] filesPaths = config.getPaths();
        dumpNum = filesPaths.length;
        if (dumpNum > 0) dumpId = 0;

        for (String path : filesPaths) {
            var dump = new LinkedList<String>();
            Path filePath = Paths.get(path);
            Charset charset = StandardCharsets.UTF_8;

            try (BufferedReader bufferedReader = Files.newBufferedReader(filePath, charset)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    dump.add(line);
                    dumpResidue++;
                }
            } catch (IOException ex) {
                System.out.format("Ошибка ввода/вывода: %s%n", ex);
            }
            dumpArr.add(dump);
        }
    }

    /** Метод получения текущей строки из пула;
     * строки выбираются по модулю в соответствии с количеством входных файлов,
     * что обеспечивает очередность выбора строк по порядку следования имен файлов в строке команды
     * @return текущая строка пула
     * @throws Exception - исключение, сообщающее об исчерпании пула строк из входных файлов
     */
    public String getLine() throws Exception {
        if (dumpResidue <= 0) throw new Exception("Пул строк из входных файлов пуст.");
        while (dumpArr.get(dumpId).isEmpty()) dumpId = ++dumpId % dumpNum;
        String line = dumpArr.get(dumpId).poll();
        dumpId = ++dumpId % dumpNum;
        dumpResidue--;
        assert line != null;
        return line.trim();
    }

    /** Метод-геттер остатка строк в пуле
     * @return dumpResidue - текущий остаток строк в пуле
     */
    public int getDumpResidue() {
        return dumpResidue;
    }
}
