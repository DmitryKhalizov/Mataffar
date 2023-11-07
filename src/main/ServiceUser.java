package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ServiceUser {

    private static final String FILE_PATH =  "Resources\\users.csv";
    public List<User> getUserDatabase() {
        List<User> userDataBase = new ArrayList<>();
        User user1 = new User("Kund1", "11111", "employee");
        User user2 = new User("Kund2", "22222", "employee");
        User user3 = new User("Kund3", "33333", "employee");
        User user4 = new User("Admin", "password", "admin");
        userDataBase.add(user1);
        userDataBase.add(user2);
        userDataBase.add(user3);
        userDataBase.add(user4);
        return userDataBase;
    }

    public String getAuthorisation(String username, String userPassword, List<User> userDB) {
        for (User a : userDB) {
            if (a.getPassword().equals(userPassword) && a.getUsername().equalsIgnoreCase(username)) {
                return a.getStatus();
            }
        }
        return "main.User not found";
    }

    public void createUsersFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            System.out.println("File exists.");
        } else {
            System.out.println("File does not exist.");
            try (FileWriter csvWriter = new FileWriter(FILE_PATH)) {
                csvWriter.append("Name, Password, Status");
                csvWriter.append("\n");
                csvWriter.append("Kund1, 11111, employee");
                csvWriter.append("\n");
                csvWriter.append("Kund2, 22222, employee");
                csvWriter.append("\n");
                csvWriter.append("Kund3, 33333, employee");
                csvWriter.append("\n");
                csvWriter.append("Admin, password, admin");
                csvWriter.append("\n");
                System.out.println("CSV file " + FILE_PATH + " created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addNewUserToDatabase() {
        String status;
        Scanner s = new Scanner(System.in);
        System.out.println("Add username: ");
        String username = s.nextLine();
        System.out.println("Choose password: ");
        String password = s.nextLine();
        System.out.println("Insert status: admin (a) or employee (e)");
        String writtenStatus = s.nextLine();
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Empty user or password, cannot create employee");
            return false;
        }
        if (!writtenStatus.equalsIgnoreCase("e") &&
                !writtenStatus.equalsIgnoreCase("employee") &&
                !writtenStatus.equalsIgnoreCase("a") &&
                !writtenStatus.equalsIgnoreCase("admin")) {
            System.out.println("Cannot create user, unknown status-");
            return false;
        } else {
            if (writtenStatus.equalsIgnoreCase("e") ||
                    writtenStatus.equalsIgnoreCase("employee")) {
                status = "employee";
            } else {
                status = "admin";
            }
        }

        List<User> usersFile = getAllUsersFromFile();
        for (User u : usersFile) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                System.out.println("This username already in use. Take another username.");
                return false;
            }
        }
        File file = new File(FILE_PATH);
        try (FileWriter csvWriter = new FileWriter(file, true)) {
            csvWriter.write(username + "," + password + "," + status);
            csvWriter.write("\n");
            System.out.println("Added new user: " + username);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something wrong, cannot add user " + username + " to file");
        }
        return true;
    }

    public List<User> getAllUsersFromFile() {
        List<User> usersFromFile = new ArrayList<>();
        File file = new File(FILE_PATH);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String username = data[0].trim();
                    String password = data[1].trim();
                    String status = data[2].trim();
                    User user = new User(username, password, status);
                    usersFromFile.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find the user in file: " + FILE_PATH);
        }
        return usersFromFile;
    }
}
