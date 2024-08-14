package org.example;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс, реализующий обработку входных параметров программы (аргументов утилиты)
 * и обеспечивающий обработку исключений, связанных с парсингом команды при запуске.
 * @autor Julzz10110
 * @version 1.0
 */
public class CLIProcessor {

    /** Объект командной строки */
    private static CommandLine commandLine;

    /** Метод-геттер объекта командной строки
     * @return commandLine - объект командной строки
     */
    public static CommandLine getCommandLine() {
        return commandLine;
    }

    /** Процедура обработки ввода командной строки
     * @param args - атрибуты введенной команды
     * @throws ParseException - исключение, сигнализирующее об ошибке
     * при анализе входных параметров (атрибутов) команды.
     * */
    public static void processCommandLine(String[] args) {

        Option option_o = Option.builder("o")
                .required(false)
                .desc("Опция ручной установки пути к директории расположения выходных файлов.")
                .build();
        Option option_p = Option.builder("p")
                .required(false)
                .desc("Опция установки префикса к имени выходных файлов.")
                .build();
        Option option_a = Option.builder("a")
                .required(false)
                .desc("Опция добавления в существующие файлы.")
                .build();

        OptionGroup infoOptionGroup = new OptionGroup();
        Option option_s = Option.builder("s")
                .required(false)
                .desc("Опция вывода краткой информации о выходных файлах.")
                .build();

        Option option_f = Option.builder("f")
                .required(false)
                .desc("Опция вывода полной информации о выходных файлах.")
                .build();
        infoOptionGroup.addOption(option_s);
        infoOptionGroup.addOption(option_f);

        Option option_h = Option.builder("h")
                .required(false)
                .desc("Опция вывода справки по использованию утилиты.")
                .build();

        Options options = new Options();
        option_o.setArgs(1);
        option_p.setArgs(1);
        options.addOption(option_o);
        options.addOption(option_p);
        options.addOption(option_a);
        options.addOption(option_h);
        options.addOptionGroup(infoOptionGroup);

        CommandLineParser cmdLineParser = new DefaultParser();

        try {
            commandLine = cmdLineParser.parse(options, args);
        } catch (ParseException pe) {
            System.out.print("Ошибка парсинга команды: ");
            System.out.println(pe.getMessage());
            String[] modifiedArgs = getFilteredArgs(args.clone());

            for (String arg : args) {
                boolean inOptions = false;
                if (arg.charAt(0) != '-') continue;
                String extractedArg = arg.charAt(0) == '-'
                        ? arg.substring(1) : arg;
                for (Option opt : options.getOptions()) {
                    if (opt.getOpt().equals(extractedArg)) {
                        inOptions = true;
                        break;
                    }
                }
                if (!inOptions) modifiedArgs = getClarifiedArgs(modifiedArgs, extractedArg);
            }

            if (pe.getMessage().contains("-" + option_s.getOpt())
                    && pe.getMessage().contains("-" + option_f.getOpt())) {
                System.out.println("Автоматически будет выбрана опция вывода полной статистики по выходным файлам.");
                modifiedArgs = getClarifiedArgs(args, "s");

            }
            if (pe.getMessage().contains(" -" + option_p.getOpt())
            || pe.getMessage().contains(" " + option_p.getOpt())) {
                System.out.println("Префикс для имен выходных файлов не будет задан.");
                Main.prefix = "";
                modifiedArgs = getClarifiedArgs(modifiedArgs, "p");
                System.out.println("PATH :  " + option_o.getValue());
            }

            if (pe.getMessage().contains(" -" + option_o.getOpt())
                    || pe.getMessage().contains(" " + option_o.getOpt())) {
                System.out.println("Не заданы имена входных файлов " +
                        "и/или не задан параметр пути директории расположения выходных файлов. " +
                        "Добавьте имена входных файлов" +
                        "и/или укажите путь к папке после опции '-" + option_o.getOpt()  + "'.");
            }

            try {
                commandLine = cmdLineParser.parse(options, modifiedArgs);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        } finally {
            if (commandLine.hasOption("h")) {
                final String commandLineSyntax = "java util.jar"; // Подсказка по запуску утилиты
                final PrintWriter writer = new PrintWriter(System.out); // Поток вывода справки
                final HelpFormatter helpFormatter = new HelpFormatter(); // Создание объекта для вывода справки
                helpFormatter.printHelp(
                        writer,
                        80,
                        commandLineSyntax,
                        "Опции:",
                        options,
                        3,
                        5,
                        "-- HELP --",
                        true); // Формирование справки
                writer.flush(); // Вывод
            }
            if (commandLine.hasOption("o")) {
                String inputDirPath = commandLine.getOptionValue("o").strip();
                if (inputDirPath.contains(".")) {
                    Set<Character> filter = new HashSet<>(List.of('.'));
                    inputDirPath = inputDirPath.chars()
                            .filter(c -> !filter.contains((char) c))
                            .mapToObj(c -> "" + (char) c)
                            .collect(Collectors.joining());
                }
                while (inputDirPath.charAt(inputDirPath.length() - 1) == '\\') {
                    inputDirPath = Optional.of(inputDirPath)
                            .filter(s -> !s.isEmpty())
                            .map(str -> str.substring(0, str.length() - 1))
                            .orElse(inputDirPath);
                }

                System.out.println("Путь к директории расположения " +
                        "выходных файлов отформатирован: "
                        + (Paths.get(inputDirPath).isAbsolute()
                 ? inputDirPath : System.getProperty("user.dir")
                        + '\\' + inputDirPath));

                try {
                    Files.createDirectories(Paths.get(inputDirPath));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Main.intsFullPath = inputDirPath + '/';
                Main.floatsFullPath = inputDirPath + '/';
                Main.strsFullPath = inputDirPath + '/';
            } else {
                Main.intsFullPath = System.getProperty("user.dir") + '/';
                Main.floatsFullPath = System.getProperty("user.dir") + '/';
                Main.strsFullPath = System.getProperty("user.dir") + '/';
            }

            if (commandLine.hasOption("p")) {
                if (!commandLine.getOptionValue("p").isEmpty())
                    Main.prefix = commandLine.getOptionValue("p").replaceAll("\\.", "");
            }

            {
                String[] remainder = commandLine.getArgs();

                for (String inputFilename : remainder) {
                    Main.inputFiles.add(Paths.get(inputFilename).toAbsolutePath().toString());
                }
                System.out.println();
            }
        }
    }

    /** Метод обработки атрибутов команды, включающей разрешение конфликтов в группе опций (OptionGroup)
     * и удаление указанных атрибутов
     * @param args - атрибуты введенной команды
     * @param avoidedArgs - атрибуты, которые необходимо исключить из {@param args}
     * @return массив обработанных атрибутов;
     * @see CLIProcessor#processCommandLine(String[])
     * */
    private static String[] getClarifiedArgs(String[] args, String... avoidedArgs) {
        ArrayList<String> clarifiedArgs = new ArrayList<>();

        for (String arg : args) {
            if (arg.matches("-[fs]{2,}")) {
                clarifiedArgs.add("-f");
                continue;
            }

            for (String avoidedArg : avoidedArgs) {
                if (!arg.matches("-[" + avoidedArg + "]+")) {
                        clarifiedArgs.add(arg);
                    }
                }
        }

        Object[] temp = clarifiedArgs.toArray();
        return Arrays.copyOf(temp, temp.length, String[].class);
    }

    /** Метод фильтрации атрибутов команды в соответствии с валидными значениями
     * @param args - атрибуты введенной команды
     * @return targetArr - массив отфильтрованных атрибутов;
     * если после обработки содержимое массива не изменилось, возвращает исходный массив {@param args}
     * @see CLIProcessor#processCommandLine(String[])
     * */
    private static String[] getFilteredArgs(String[] args) {
        ArrayList<String> filteredArgsList = new ArrayList<>();
        Set<Character> filter = new HashSet<>(Arrays.asList('-', 'o', 'a', 'p', 's', 'f', 'h'));

        for (var arg : args) {
            if (arg.charAt(0) != '-'
                || (arg.charAt(0) == '-' && arg.length() > 1)) {
                filteredArgsList.add(arg);
                continue;
            }

            String filteredArg = arg.chars()
                    .filter(c -> filter.contains((char) c))
                    .mapToObj(c -> "" + (char) c)
                    .collect(Collectors.joining());
            if (!filteredArg.isEmpty()
                && !filteredArg.equals("-")) filteredArgsList.add(filteredArg);
        }

        Object[] tempArr = filteredArgsList.toArray();
        String[] targetArr = Arrays.copyOf(tempArr, tempArr.length, String[].class);

        return !filteredArgsList.isEmpty()
        ? targetArr
        : args;
    }

}