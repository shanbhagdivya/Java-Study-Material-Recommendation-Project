package main.java.com.example.myloginpage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends Application {

    private Map<String, String> users = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        // Load existing users from file
        loadUsers();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Heading label
        Label headingLabel = new Label("StudyCompass");
        headingLabel.setFont(Font.font("Times New Roman", 24));
        GridPane.setConstraints(headingLabel, 0, 0, 2, 1);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 1);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 1);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 2);
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 2);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 3);
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            if (users.containsKey(username)) {
                if (users.get(username).equals(password)) {
                    showAlert("Login Successful", "Welcome back, " + username + "!");
                    // Launch home page
                    HomePage homePage = new HomePage();
                    homePage.start(new Stage());
                    // Close login page
                    primaryStage.close();
                } else {
                    showAlert("Login Failed", "Invalid password.");
                }
            } else {
                showAlert("Login Failed", "Invalid username. Please create an account.");
            }
        });

        Button createButton = new Button("Create Account");
        GridPane.setConstraints(createButton, 1, 4);
        createButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            if (!users.containsKey(username)) {
                createUser(username, password);
                showAlert("Account Created", "Your account has been created successfully.");
            } else {
                showAlert("Account Creation Failed", "Username already exists. Please choose another username.");
            }
        });

        grid.getChildren().addAll(headingLabel, usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, createButton);

        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + ":" + password);
            writer.newLine();
            writer.flush();
            users.put(username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}