package com.ren1kron.client.utility;



import com.ren1kron.common.console.Console;
import com.ren1kron.common.exceptions.AskExitException;
import com.ren1kron.common.models.Worker;
import com.ren1kron.common.models.Organization;
import com.ren1kron.common.models.Position;
import com.ren1kron.common.models.Status;
import com.ren1kron.common.models.Coordinates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

/**
 * This thing will link Console and Managers
 *
 * @author ren1kron
 */
public class Asker {
    public static Worker askWorker(Console console, Integer key, int id, String login) throws AskExitException {
        Worker worker;
        while (true) {
            try {
//                Integer key = askKey(console);
                String name;
                do {
                    console.print("Enter the name of worker: ");
                    name = console.readln().trim();
                    if (name.equals("exit")) throw new AskExitException();
                } while (name.isEmpty());
                //            do {
                //                console.print("Enter the Full name of organization worker is associated with: ");
                //                organizationName = console.readln().trim();
                //                if (organizationName.equals("exit")) throw new AskExitException();
                //            } while (organizationName.isEmpty());

                var organization = askOrganization(console);
                var position = askPosition(console);
                var status = askStatus(console);
                var salary = askSalary(console);
                var coordinates = askCoordinates(console);
                var startDate = askStartDate(console);
                //            var creationDate = new Date();
                //            Integer key, int id, String name, Organization organization, Position position, Status status, float salary,
                //            Coordinates coordinates, Date creationDate, LocalDate startDate
//                worker = new Worker(key, name, organization, position, status, salary, coordinates, startDate);
                worker = new Worker(key, id, name, organization, position, status, salary, coordinates, startDate);
                worker.setUsername(login);
//                if (worker.validate()) return worker;
                if (worker.validateWithoutId()) return worker;
                else console.printError("Inserted worker has invalid values. Try again");
            } catch (NoSuchElementException | IllegalStateException e) {
                console.printError("Illegal parameter");
                return null;
            }
        }
    }

    public static Coordinates askCoordinates(Console console) throws AskExitException {
        try {
            float x;
            while (true) {
                console.print("Enter \"x\" coordinates: ");
                var line = console.readln().trim();
                line = line.replace("\\s", "");
                if (line.equals("exit")) throw new AskExitException();
                if (!line.isEmpty()) {
                    try {
                        x = Float.parseFloat(line); // Максимальное значение поля: 145
                        if (x <= 145) break;
                        else console.println("Illegal value! Try again!");
                    } catch (NumberFormatException e) {
                        console.println(" * Illegal value! Try again! * ");
                    }
                }
            }
            double y;
            while (true) {
                console.print("Enter \"y\" coordinates: ");
                var line = console.readln().trim();
                line = line.replace("\\s", "");
                if (line.equals("exit")) throw new AskExitException();
                if (!line.isEmpty()) {
                    try {
                        y = Double.parseDouble(line);
                        break;
                    } catch (NumberFormatException ignored) {
                        console.println(" * Illegal value! Try again! * ");
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("The value of Coordinates is illegal!");
            return null;
        }
    }

    public static float askSalary(Console console) throws AskExitException {
        try {
            while (true) {
                try {
                    console.print("Enter the worker's salary (in USD): $");
                    var line = console.readln().trim();
                    line = line.replace("\\s", "");
                    if (line.equals("exit")) throw new AskExitException();
                    if (!line.isEmpty()) {
                        var f = Float.parseFloat(line);
                        if (f > 0) return f;
                        else console.println("Inserted value of salary is illegal! Try again!");
                    }
                } catch (NumberFormatException e) {
                    console.println(" * Illegal value! Try again! * ");
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("The value of Salary is illegal");
            return 0;
        }
    }

    public static Position askPosition(Console console) throws AskExitException {
        try {
            while (true) {
                try {
                    console.print("Enter the position (" + Position.names().toLowerCase() + ") of Worker: ");

                    var line = console.readln().trim();
                    if (line.equals("exit")) throw new AskExitException();
                    if (!line.isEmpty()) {
                        return Position.valueOf(line.toUpperCase());
                    }
                } catch (NullPointerException | IllegalArgumentException e) {
                    console.println(" * Illegal value! Try again! * ");
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Illegal value!");
            return null;
        }
    }

    public static LocalDate askStartDate(Console console) throws AskExitException {
        try {
            while (true) {
                console.print("Enter Worker's Birthday in the following format: YYYY-MM-DD or " +
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE) + " : ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskExitException();
                if (!line.isEmpty()) {
                    try {
                        return LocalDate.parse(line, DateTimeFormatter.ISO_DATE);
                    } catch (DateTimeParseException e) {
                        console.println(" * Illegal value! Try again! * ");
                    }
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            console.println(" * Illegal value! Try again! * ");
            return null;
        }
    }

    public static Status askStatus(Console console) throws AskExitException {
        try {
            while (true) {
                console.print("Enter the Status of the Worker (" + Status.names().toLowerCase() + ") (ENTER if status is null): : ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskExitException();
                else if (line.isEmpty()) return null;
//                else if (!line.isEmpty()) {
                else {
                    try {
                        return Status.valueOf(line.toUpperCase());
                    } catch (NullPointerException | IllegalArgumentException e) {
                        console.println(" * Illegal value! Try again! * ");
                    }
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Illegal value!");
            return null;
        }
    }

    //    public static Organization askOrganization(Console console, String organizationName) throws AskExitException {
//        console.print("Enter the Full name of Organization the Worker is related to: ");
//        var line = console.readln().trim();
//        while (true) {
//            if (line.equals("exit")) throw new AskExitException();
//            if (!line.isEmpty()) {
//
//            }
//        }
//    }
    public static Organization askOrganization(Console console) throws AskExitException {
//    private String fullName; //Поле не может быть null
//    private Integer annualTurnover; //Поле МОЖЕТ быть null, Значение поля должно быть больше 0
//    private int employeesCount; //Значение поля должно быть больше 0
        try {
            String organizationName;
            console.print("Enter the Full name of Organization worker is associated with (ENTER if " +
                    "organization is unknown): ");
            organizationName = console.readln().trim();
            if (organizationName.equals("exit")) throw new AskExitException();
            if (organizationName.isEmpty()) return null;

            int employeesCount;
            while (true) {
                console.print("Enter the Employees count of the Organization worker associated with: ");
                var line = console.readln().trim();
                line = line.replace("\\s", "");
                if (line.equals("exit")) throw new AskExitException();
//            else if (line.isEmpty()) return new Organization(organizationName, null);
                if (!line.isEmpty()) {
                    try {
                        employeesCount = Integer.parseInt(line);
                        if (employeesCount > 0) break;
                        else console.println(" * Illegal value! Try again! * ");
//                    return new Organization(organizationName, orgAnnualTurnover);
                    } catch (NumberFormatException e) {
                        console.println(" * Illegal value! Try again! * ");
                    }
                }
            }

            int orgAnnualTurnover;
            while (true) {
                console.print("Enter the Annual Turnover of the Organization worker is associated with in USD (ENTER if " +
                        "annual turnover of organization is unknown): $");
                var line = console.readln().trim();
                line = line.replace("\\s", "");
                if (line.equals("exit")) throw new AskExitException();
//            else if (line.isEmpty()) return new Organization(organizationName, null);
                else if (line.isEmpty()) return new Organization(organizationName, null, employeesCount);
                else {
                    try {
                        orgAnnualTurnover = Integer.parseInt(line);
                        if (orgAnnualTurnover > 0)
                            return new Organization(organizationName, orgAnnualTurnover, employeesCount);
                        else console.println(" * Illegal value! Try again! * ");
                    } catch (NumberFormatException e) {
                        console.println(" * Illegal value! Try again! * ");
                    }
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Illegal value!");
            return null;
        }


//        else if (organizationName)
    }
//    @Deprecated
    public static Integer askKey(Console console) throws AskExitException {
//        Integer key;
        try {
            while (true) {
                console.print("Enter the Key of Worker (it must be an integer greater than 0): ");
                var line = console.readln().trim();
                line = line.replace("\\s","");
                if (line.equals("exit")) throw new AskExitException();
                else if (!line.isEmpty()) {
                    try {
                        int key = Integer.parseInt(line);
                        if (key > 0) return key;
                        else console.println("Key should be greater than 0!");
                    } catch (NumberFormatException e) {
                        console.println(" * Illegal value! Try again! * ");
                    }
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Illegal value!");
            return null;
        }
    }
}
