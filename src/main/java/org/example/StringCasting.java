package org.example;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Класс, предоставляющий статические методы для приведения строк к числовым значениям.
 * @autor Julzz10110
 * @version 1.0
 */
public class StringCasting {

    /** Метод проверки возможности приведения строки к вещественному значению
     * @return вещественное число
     */
    public static boolean isReal(String s) {
        return s.matches("-?\\d+\\.\\d*[FfDd]?")
                || s.matches("-?\\d*\\.\\d+[FfDd]?")
                || s.matches("-?\\d+.?\\d*[Ee]-?\\d+");
    }

    /** Метод проверки возможности приведения строки к целочисленному значению
     * @return целочисленное число
     */
    public static boolean isInteger(String s) {
        return s.matches("-?\\d+")
                || ((s.startsWith("0b")
                    || s.startsWith("0B"))
                    && s.substring(2).matches("[01]+"))
                || ((s.startsWith("0x")
                    || s.startsWith("0X"))
                    && s.substring(2).matches("[0-9A-Fa-f]+"));
    }

    /** Метод приведения строки к вещественному значению
     * поддерживает форму записи с 'F'/'f' и 'D'/'d' в конце
     * и "научную форму записи"
     * @return вещественное число
     */
    public static BigDecimal toReal(String s) {
        if (!isReal(s)) return null;
        if (s.charAt(s.length()-1) == 'f'
            || s.charAt(s.length()-1) == 'F'
                || s.charAt(s.length()-1) == 'd'
                || s.charAt(s.length()-1) == 'D') return new BigDecimal(s.substring(0, s.length()-1));
        return new BigDecimal(s);
    }

    /** Метод приведения строки к целочисленному значению;
     * поддерживает двоичные и шестнадцатеричные литералы
     * @return целочисленное число
     */
    public static BigInteger toInteger(String s) {
        if (!isInteger(s)) return null;
        if ((s.startsWith("0b")
             || s.startsWith("0B"))
            && s.substring(2).matches("[0-1]+"))
            return new BigInteger(s.substring(2), 2);
        else if ((s.startsWith("0x")
                || s.startsWith("0X"))
                && s.substring(2).matches("[0-9A-Fa-f]+"))
            return new BigInteger(s.substring(2), 16);
        return new BigInteger(s);
    }
}
