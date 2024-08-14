package org.example.output;

import org.example.StringCasting;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс, реализующий интерфейс <b>IOutput<T><b/>.
 * @autor Julzz10110
 * @version 1.0
 */
public class Output<T> implements IOutput<T> {

    /** Количество элементов (строк в файле) */
    protected int elementNum = 0;
    /** Список элементов (содержимое исходящего файла) */
    protected List<T> contentList;
    /** Строковое представление пути файла */
    protected String filePath;

    /**
     * Конструктор - создание нового объекта
     * @param filePath - строковое представление пути файла
     */
    public Output(String filePath) {
        this.filePath = filePath;
    }

    /** Метод получения краткой информации о выходных данных
     * @return elementNum - количество элементов в исходящем файле
     */
    @Override
    public int getShortStats() {
        boolean isStraightCalled = Thread.currentThread().getStackTrace()[2].getMethodName().equals("getShortStats");
        if (isStraightCalled) System.out.println("Краткая статистика по файлу " + filePath + ":");
        System.out.println("Количество элементов:  " + elementNum);
        if (isStraightCalled) System.out.println();

        return elementNum;
    }

    /** Метод получения полной информации о выходных данных
     * @return массив выходных статистических показателей
     */
    @Override
    public BigDecimal[] getFullStats() {
        System.out.println("Полная статистика по файлу " + filePath + ":");
        getShortStats();

        return new BigDecimal[0];
    }

    /** Процедура обновления статистической информации о выходных данных
     */
    @Override
    public void actualizeStats(T element) {}

    /** Метод приведения элемента
     * @return element - приведенный элемент
     */
    public Object getTypedElement(String element) {
        element = element.replaceAll("^\\s+", "").strip();
        //if (!element.matches("-?\\d+(\\.\\d+)?")) return element;
        if (StringCasting.isReal(element)) {
             return StringCasting.toReal(element);
        }
        else if (StringCasting.isInteger(element)) {
            return StringCasting.toInteger(element);

        }
            return element;
    }

    /** Процедура предварительного заполнения содержимого выходного файла
     */
    @Override
    public void presetContentList() {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEachOrdered(line -> {elementNum++;
                actualizeStats((T) getTypedElement(line));});
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /** Процедура обновления содержимого выходного файла (добавления элемента)
     */
    @Override
    public void update(T element) {
        contentList.add(element);
        elementNum++;
    }

    /** Процедура записи/перезаписи выходных данных в файл
     */
    @Override
    public void writeFile() throws IOException {
            FileWriter fileWriter = new FileWriter(filePath);
            for (Object str : contentList) {
                fileWriter.write(str.toString() + System.lineSeparator());
            }
            fileWriter.close();
    }

    /** Процедура добавления выходных данных в существующий файл
     */
    @Override
    public void appendToFile() throws IOException {
        for (Object str : contentList) {
            Files.write(Paths.get(filePath), (str + "\n").getBytes(), StandardOpenOption.APPEND);
        }
    }
}
