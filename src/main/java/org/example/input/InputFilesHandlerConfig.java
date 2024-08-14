package org.example.input;

/**
 * Класс конфигурации обработчика входных файлов.
 * @autor Julzz10110
 * @version 1.0
 */
public class InputFilesHandlerConfig {

    /** Массив путей входных файлов */
    private final String[] filePaths;

    /**
     * Конструктор - создание нового объекта и инкапсуляция путей входных файлов
     * @param filePaths - строки путей входных файлов
     */
    public InputFilesHandlerConfig(String... filePaths) {
        this.filePaths = filePaths;
    }

    /** Метод-геттер массива путей входных файлов
     * @return filePaths - массив путей входных файлов
     */
    public String[] getPaths() {
        return filePaths;
    }
}
