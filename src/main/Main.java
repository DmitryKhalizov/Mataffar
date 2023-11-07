package main;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.round;

public class Main {

    private static ServiceUser serviceUser = new ServiceUser();
    private static ServiceProduct serviceProduct = new ServiceProduct();
    private static BasketService basketService = new BasketService();
    private static ServiceCustomer serviceCustomer = new ServiceCustomer();

    private static String[] menyList = new String[]{"Welcome to the fruit & vegetables shop",
            "Please select the number in the menu below",
            "1. Show the inventory.",
            "2. Add a new product.",
            "3. Search in the inventory and add to customer basket",
            "4. Exit.",
            "5. Add new employee.",
            "6. Manage sale campaign."
    };

    public static void main(String[] args) {

        serviceUser.createUsersFile();
        serviceProduct.createProductFile();

        Scanner input = new Scanner(System.in);
        List<Product> allProducts = serviceProduct.getAllProductsFromFile();
        boolean process = true;
        String userStatus = logIn();
        boolean isAdmin = isAdmin(userStatus);
        boolean isEmployee = isEmployee(userStatus);
        while (process) {
            String choice = getMainMeny(userStatus);
            switch (choice) {
                case "1":
                    for (Product p : allProducts) {
                        System.out.println("_______________________________________________________________");
                        System.out.println(p.getName() + " type: ");
                        for (String s : p.getTypes()) {
                            System.out.println(s);
                        }
                        System.out.println(" price: " + p.getPrice());
                        System.out.println("Discount: " + p.getSale() + " % Price with discount: " + p.getPriceWithDiscount());
                        System.out.println("_______________________________________________________________");
                    }
                    process = isContinue();
                    break;
                case "2":
                    if (isAdmin || isEmployee) {
                        boolean addProduct = false;
                        while (!addProduct) {
                            addProduct = serviceProduct.addNewProduct();
                            System.out.println("Would you like to add more products, Y/N?");
                            String answer = input.nextLine();
                            if (answer.equalsIgnoreCase("Y")) {
                                addProduct = false;
                            }
                        }
                    } else {
                        System.out.println("You are not an admin or employee and cannot add a new product.");
                        break;
                    }
                    break;
                case "3":
                    boolean doShopping = true;
                    List<Basket> shoppingList = new ArrayList<>();
                    while (doShopping) {
                        Product p = serviceProduct.searchProductFromDB();
                        if (p != null) {
                            System.out.println("Would you like to add product to shopping basket, Y or N?");
                            String answer = input.nextLine();
                            if (answer.equalsIgnoreCase("Y")) {
                                System.out.println("How many " + p.getTypeOfPrice() + " would you like to add?");
                                double productAmount = input.nextDouble();
                                if (productAmount < 0) {
                                    System.out.println("Amount should be more than zero");
                                    break;
                                }
                                if (p.getTypeOfPrice().equalsIgnoreCase("piece")) {
                                    productAmount = round(productAmount);
                                }
                                Basket b = new Basket(productAmount,p);
                                shoppingList = basketService.addToBasket(b, shoppingList);
                            }
                        }
                        System.out.println("Would you like to continue shopping, Y or N?");
                        String answer = input.nextLine();
                        String answer1 = input.nextLine();
                        if (answer1.equalsIgnoreCase("N")) {
                            doShopping = false;
                            serviceCustomer.printCheque(shoppingList);
                        }
                    }
                    break;
                case "4":
                    System.out.println("Exiting the program");
                    input.close();
                    System.exit(0);
                case "5":
                    if (isAdmin) {
                        boolean addUser = false;
                        while (!addUser) {
                            addUser = serviceUser.addNewUserToDatabase();
                            System.out.println("Would you like to add more users, Y/N?");
                            String answer = input.nextLine();
                            if (answer.equalsIgnoreCase("Y")) {
                                addUser = false;
                            }
                        }
                    } else {
                        System.out.println("You are not an admin and cannot add a new user.");
                        break;
                    }
                case "6":
                    if (isAdmin || isEmployee ) {
                        boolean manage = true;
                        while (manage) {
                            System.out.println("Search for product you would like to change");
                            Product p = serviceProduct.searchProductFromDB();
                            String pLine = serviceProduct.getLineInFile(p);
                            Product updatedP = serviceProduct.makeChangeForProduct(p);

                            String updatePline = serviceProduct.getLineInFile(updatedP);
                            boolean success = serviceProduct.changeLineInFile(pLine, updatePline);
                            if (success) {
                                System.out.println("Product " + p.getName() + " updated with new campaign.");
                            } else {
                                System.out.println("Cannot update.");
                            }
                            System.out.println("Would you like to change campaign of the product, Y/N?");
                            String answer = "";
                            answer = input.nextLine();
                            if (answer.equalsIgnoreCase("N")) {
                                manage = false;
                                break;
                            }
                        }
                    } else {
                        System.out.println("You are not an admin or employee and cannot manage sales campaigns.");
                        break;
                    }
                default:
                    System.out.println("Choice not valid. Please try again");
            }
        }
    }

    private static boolean isContinue() {
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to continue (Y or N)?");
        String answer = input.nextLine();
        if (answer.equalsIgnoreCase("N")) {
            return false;
        } else {
            return true;
        }
    }

    private static List<Product> checkByTypes(List<String> typesForSearch, List<Product> productDataBase) {
        List<Product> resultList = new ArrayList<>();
        if (typesForSearch.get(0).isEmpty()) {
            return productDataBase;
        }
        for (Product p : productDataBase) {
            List<String> typesArray = p.getTypes();
            boolean check = true;
            for (String searchElement : typesForSearch) {
                if (!typesArray.contains(searchElement.trim())) {
                    check = false;
                    break;
                }
            }
            if (check) {
                resultList.add(p);
            }
        }
        return resultList;
    }
    private static String getMainMeny(String status) {
        String redColor = "\u001B[31m";
        String resetColor = "\u001B[0m";
        String yellowColor = "\u001B[33m";
        String greenColor = "\u001B[32m";
        String grayColor = "\u001B[90m";
        String pinkColor = "\u001B[38;5;206m";

        Scanner input = new Scanner(System.in);
        if (isAdmin(status)) {
            for (String m : menyList) {
                System.out.println(redColor + m);
            }
        }
        if (isEmployee(status)) {
            for (String m : menyList) {
                if (m.startsWith("5.")) {
                    System.out.println(grayColor + m);
                } else {
                    System.out.println(yellowColor + m);
                }
            }
        }
        if (!isEmployee(status) && !isAdmin(status)) {
            for (String m : menyList) {
                if (m.startsWith("5.") || m.startsWith("6.") || m.startsWith("2.")) {
                    System.out.println(grayColor + m);
                } else if (m.startsWith("Welcome") || m.startsWith("Please")) {
                    System.out.println(pinkColor + m);
                } else {
                    System.out.println(greenColor + m);
                }
            }
        }
        String choice = input.nextLine();
        return choice;
    }

    private static String logIn() {
        List<User> userDatabase = serviceUser.getAllUsersFromFile();
        Scanner login = new Scanner(System.in);
        System.out.println("Enter your name");
        String inputName = login.nextLine();
        System.out.println("Enter your password");
        Console cnsl = System.console();
        String inputPassword;
        if (cnsl == null) {
            inputPassword = login.nextLine();
        } else {
            char[] ch = cnsl.readPassword();
            inputPassword = new String(ch);
        }
        String status = serviceUser.getAuthorisation(inputName, inputPassword, userDatabase);
        if (status.equals("admin")) {
            System.out.println("You are inlogged as an admin");
            return status;
        }
        if (status.equals("employee")) {
            System.out.println("You are logged in as an employee");
            return status;
        } else {
            System.out.println("Welcome as a customer!");
        }
        return status;
    }

    private static boolean isAdmin(String userStatus) {
        if (userStatus.equals("admin"))
            return true;
        else return false;
    }

    private static boolean isEmployee(String userStatus) {
        if (userStatus.equals("employee"))
            return true;
        else return false;
    }

}
