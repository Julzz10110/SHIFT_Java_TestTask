package org.example.output;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Обобщенный интерфейс выходных данных.
 * @autor Julzz10110
 * @version 1.0
 */
public interface IOutput<T> {

    /** Метод получения краткой информации о выходных данных
     * @return количество элементов в исходящем файле
     */
    int getShortStats();

    /** Метод получения полной информации о выходных данных
     * @return массив выходных статистических показателей
     */
    BigDecimal[] getFullStats();

    /** Процедура обновления статистической информации о выходных данных
     */
    void actualizeStats(T element);

    /** Процедура предварительного заполнения содержимого выходного файла
     */
    void presetContentList();

    /** Процедура обновления содержимого выходного файла (добавления элемента)
     */
    void update(T obj);

    /** Процедура записи/перезаписи выходных данных в файл
     */
    void writeFile() throws IOException;

    /** Процедура добавления выходных данных в существующий файл
     */
    void appendToFile() throws IOException;
}
