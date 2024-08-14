package org.example.output;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Прокси-класс, предоставляющий опосредованный доступ к классу <b>Output<T></><b/>
 * и обеспечивающий создание/запись в файл по необходимости.
 * @autor Julzz10110
 * @version 1.0
 */
public class OutputProxy<T> implements IOutput<T> {

    /** Строковое представление пути файла */
    private final String filePath;
    /** Ссылка на инкапсулированный реальный объект выходных данных */
    private IOutput output;

    /**
     * Конструктор - создание нового объекта
     * @param filePath - Строковое представление пути файла
     */
    public OutputProxy(String filePath) {
        this.filePath = filePath;
    }

    /** Метод получения пути файла в формате строки
     * @return filePath - строковое представление пути файла
     */
    public String getFilePath() {
        return filePath;
    }

    /** Метод получения краткой информации о выходных данных
     * @return количество элементов в исходящем файле
     * @see Output#getShortStats()
     */
    @Override
    public int getShortStats() {
        if (output != null) {
            return output.getShortStats();
        }
        return -1;
    }

    /** Метод получения полной информации о выходных данных
     * @return массив выходных статистических показателей
     * @see Output#getFullStats()
     */
    @Override
    public BigDecimal[] getFullStats() {
        if (output != null) {
            return output.getFullStats();
        }
        return null;
    }

    /** Процедура обновления статистической информации о выходных данных
     * @see Output#actualizeStats(Object)
     */
    @Override
    public void actualizeStats(T element) {}

    /** Процедура предварительного заполнения содержимого выходного файла
     * @see Output#presetContentList()
     */
    @Override
    public void presetContentList() {
        if (output != null) {
            output.presetContentList();
        }
    }

    /** Процедура обновления содержимого выходного файла (добавления элемента)
     * @see Output#update(Object)
     */
    @Override
    public void update(T obj) {
        if (output == null) {
            if (obj instanceof String) {
                output = new StringOutput(filePath);
            } else if (obj instanceof BigInteger || obj instanceof BigDecimal) {
                output = new NumericOutput(filePath);
            } else try {
                throw new Exception("Тип элемента не является допустимым!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        output.update(obj);
    }

    /** Процедура записи/перезаписи выходных данных в файл
     * @see Output#writeFile()
     */
    @Override
    public void writeFile() throws IOException {
        if (output != null) {
            output.writeFile();
            }
        }

    /** Процедура добавления выходных данных в существующий файл
     * @see Output#appendToFile()
     */
    @Override
    public void appendToFile() throws IOException {
        if (output != null) {
            output.appendToFile();
        }
    }

}
