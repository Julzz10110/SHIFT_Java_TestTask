package org.example.output;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

/**
 * Класс, представляющий числовые (целочисленные и вещественные) выходные данные.
 * @autor Julzz10110
 * @version 1.0
 */
public class NumericOutput extends Output<Number> {

    /** Сумма всех элементов */
    private BigDecimal elementsSum = BigDecimal.valueOf(0.0),
    /** Среднее значение всех элементов */
            elementsAvg,
    /** Минимальное значение */
            minElement = BigDecimal.valueOf(Double.MAX_VALUE),
    /** Максимальное значение */
            maxElement = BigDecimal.valueOf(Double.MIN_VALUE);

    /**
     * Конструктор - создание нового объекта
     * @param filePath - строковое представление пути файла
     */
    public NumericOutput(String filePath) {
        super(filePath);
        contentList = new LinkedList<>();
    }

    /** Процедура обновления содержимого выходного файла (добавления числового элемента)
     * @see Output#update(Object)
     */
    public void update(Number element) {
        super.update(element);
        actualizeStats(element);
    }

    /** Процедура обновления статистической информации о выходных данных
     * @see Output#actualizeStats(Object)
     */
    @Override
    public void actualizeStats(Number element) {
        super.actualizeStats(element);
        BigDecimal bigDecElement = new BigDecimal(String.valueOf(element));
        elementsSum = elementsSum.add(bigDecElement);
        elementsAvg = elementsSum.divide(BigDecimal.valueOf(elementNum),
                2, RoundingMode.HALF_UP);
        minElement = minElement.min(bigDecElement);
        maxElement = maxElement.max(bigDecElement);
    }

    /** Метод получения полной информации о выходных данных
     * @return elementNum - количество строк в исходящем файле
     * @return minElement - минимальное значение
     * @return maxElement - максимальное значение
     * @return elementsSum - сумма всех элементов
     * @return elementsAvg - среднее значение всех элементов
     * @see Output#getFullStats()
     */
    @Override
    public BigDecimal[] getFullStats() {
        super.getFullStats();
        System.out.println("мин. значение:  " + minElement + ":");
        System.out.println("макс. значение:  " + maxElement + ":");
        System.out.println("сумма:  " + elementsSum + ":");
        System.out.println("сред. значение:  " + elementsAvg + ":");
        System.out.println();

        return new BigDecimal[] {BigDecimal.valueOf(elementNum),
                minElement, maxElement, elementsSum, elementsAvg};
    }
}
