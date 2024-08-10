import java.io.*;
import java.util.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<String> terms = new ArrayList<>(); 
    private static final ArrayList<String> defs = new ArrayList<>(); // defs -> definition
    private static final ArrayList<Integer> errors = new ArrayList<>();
    private static final StringBuilder log = new StringBuilder(); // command history store

    public static void main(String[]args) {
        ArrayList<String> arguments = new ArrayList<>(List.of(args)); 
        
        int index = arguments.indexOf("-import"); // pass (-import data.txt -export saveData.txt)

        if (index != -1) {
            importFromFile(arguments.get(index + 1));
        }

        while (true) {
            printLog("\n Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats)");

            String command = scanLog();

            if (command.equalsIgnoreCase("add")) {
                addCommand();
            } else if (command.equalsIgnoreCase("remove")) {
                removeCommand();
            } else if (command.equalsIgnoreCase("import")) {
                importCommand();
            } else if (command.equalsIgnoreCase("export")) {
                exportCommand();
            } else if (command.equalsIgnoreCase("ask")) {
                askCommand();
            }else if (command.equalsIgnoreCase("log")) {
                logCommand();
            } else if (command.equalsIgnoreCase("hardest card")) {
                hardestCardCommand();
            } else if (command.equalsIgnoreCase("reset stats")) {
                printLog("Card statistics have been reset.");
                errors.clear();
                
            } else if (command.equalsIgnoreCase("exit")) {
                printLog("Done!");
                break;
            } else {
                printLog("Unkown command");
            }
        }

        index = arguments.indexOf("-export");
        if (index != -1) {
            exportToFile(arguments.get(index + 1));
        }

    }

    private static void hardestCardCommand() {
        int max = 0;

        if (!errors.isEmpty()) {
            max = Collections.max(errors);
        }
        ArrayList<String> hardest = new ArrayList<>();

        for (int i = 0; i < errors.size(); i++) {
            if (max == errors.get(i)) {
                hardest.add(terms.get(i));
            }
        }

        if (hardest.isEmpty() || max == 0) {
            printLog("There are no hardest cards.");
        } else if (hardest.size() == 1) {
            printLog("The hardest card is \"" + hardest.get(0) + "\". You have " + max + "errors answers it.");
        } else {
            StringBuilder hard = new StringBuilder();

            for (int i = 0; i < hardest.size(); i++) {
                if (i != hardest.size() - 1) {
                    hard.append("\"").append(hardest.get(i)).append("\",");
                } else {
                    hard.append("\"").append(hardest.get(i));
                }
            }

            printLog(hard.toString() + "You have " + max + " errors answering them.");
        }
    }

    private static void logCommand() {
        printLog("File name :");
        String file = scanLog();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(log.toString());
            printLog("The log has been saved.");
        } catch (IOException e) {
            printLog("Error when saving.");
        }
    }

    private static void askCommand() {
        printLog("How many times to ask ? ");
        int size = Integer.parseInt(scanLog());

        if (terms.size() == 0) {
            printLog("No cards!");
            return;
        }

        for (int i = 0; i < size; i++) {
            printLog("Print the definition of \"" + terms.get(i) + "\" :");
            String input = scanLog();

            if (defs.get(i).equals(input)) {
                printLog("Correct!");
            } else {
                printLog("Wrong. The right answer is \"" + defs.get(i) + 
                    (defs.contains(input) 
                    ? "\", " + "but your definition is corect for \"" + terms.get(defs.indexOf(input)) + "\"."
                    : "\"."));
                errors.set(i, errors.get(i) + 1);
            }
        }
    }

    private static void removeCommand() {
        printLog("Which card ?");

        String term  = scanLog();
        int index = terms.indexOf(term);

        if (index == -1) {
            printLog("can't remove \"" + term + "\": there is no such card.");
        } else {
            terms.remove(index);
            defs.remove(index);
            errors.remove(index);
            printLog("The card has been removed.");
        }
    }

    private static void addCommand() {
        printLog("The card :");

        String term = scanLog();
        if (terms.contains(term)) {
            printLog("The card \"" + term + " \" already exists.");
            return;
        }
        printLog("The definition of the card:");

        String def = scanLog();
        if (defs.contains(def)) {
            printLog("The definition \"" + term + " \" already exists.");
            return;
        }
        terms.add(term);
        defs.add(def);
        errors.add(0);
        printLog("The pair (\"" + term  + "\" : \"" + def + " \") has been added.");
    }

    private static void printLog(String string) {
        System.out.println(string);
        log.append(string).append("\n");
    }

    private static String scanLog() {
        String string = sc.nextLine();
        log.append(string).append("\n");

        return string;
    } 

    private static void importCommand() {
        printLog("File name :");
        String file = scanLog();
        importFromFile(file);
    }

    private static void importFromFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int counter = 0;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                int termIndex = terms.indexOf(data[0]);
                counter++;

                if (termIndex == -1) {
                    terms.add(data[0]);
                    defs.add(data[1]);
                    errors.add(Integer.parseInt(data[2]));
                } else {
                    defs.set(termIndex, data[1]);
                    errors.set(termIndex, Integer.parseInt(data[2]));
                }
            }
            printLog(counter + " cards have been loaded.");
        } catch (IOException e) {
            printLog("File Not Found");
        }
    }

    private static void exportCommand() {
        printLog("File name :");
        String file = scanLog();
        exportToFile(file);
    }

    private static void exportToFile(String file) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < terms.size(); i++) {
                StringBuilder termDef = new StringBuilder();
                termDef.append(terms.get(i)).
                    append(";").
                    append(defs.get(i)).
                    append(";").
                    append(String.valueOf(errors.get(i))); // combine all
                
                writer.write(termDef.toString());
                writer.newLine();
            }
            printLog(terms.size() + " cards have been saved!");
        } catch (IOException e) {
            printLog("Error when saving!!!");
        }
    }
}