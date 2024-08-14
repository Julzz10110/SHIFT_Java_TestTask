package org.example.output;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Класс, представляющий строковые выходные данные.
 * @autor Julzz10110
 * @version 1.0
 */
public class StringOutput extends Output<String> {

    /** Минимальная и максимальная длины строк соответственно */
    private int minLength = Integer.MAX_VALUE, maxLength = 0;

    /**
     * Конструктор - создание нового объекта
     * @param filePath - строковое представление пути файла
     */
    public StringOutput(String filePath) {
        super(filePath);
        contentList = new LinkedList<>();
    }

    /** Процедура обновления содержимого выходного файла (добавления строки)
     * @see Output#update(Object)
     */
    @Override
    public void update(String element) {
        super.update(element);
        actualizeStats(element);
    }

    /** Процедура обновления статистической информации о выходных данных
     * @see Output#actualizeStats(Object)
     */
    @Override
    public void actualizeStats(String element) {
        super.actualizeStats(element);
        maxLength = Math.max(element.length(), maxLength);
        minLength = Math.min(element.length(), minLength);
    }

    /** Метод получения полной информации о выходных данных
     * @return elementNum - количество строк в исходящем файле
     * @return minLength - минимальная длина строки
     * @return maxLength - максимальная длина строки
     * @see Output#getFullStats()
     */
    @Override
    public BigDecimal[] getFullStats() {
        super.getFullStats();
        System.out.println("размер самой короткой строки:  " + minLength + ":");
        System.out.println("размер самой длинной строки:  " + maxLength + ":");
        System.out.println();

        return new BigDecimal[] {BigDecimal.valueOf(elementNum),
                BigDecimal.valueOf(minLength), BigDecimal.valueOf(maxLength)};
    }
}
