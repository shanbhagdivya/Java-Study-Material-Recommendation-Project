package main.java.com.example.myloginpage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;

public class HomePage extends Application {

    private ComboBox<String> subjectsComboBox;
    private ComboBox<String> materialComboBox;
    private Slider priceSlider;
    private Slider ratingSlider;
    private VBox rightPanel;
    private Recommender recommender;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("StudyCompass");
        rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        try {
            recommender = new Recommender();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1); // Exit the application if unable to connect to the database
        }

        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));

        Label subjectsLabel = new Label("Subjects:");
        subjectsComboBox = new ComboBox<>();
        subjectsComboBox.getItems().addAll(
                "Choose Subject" ,"Python", "C", "Java", "AIML", "Data Science",
                "DSA", "Image Processing", "Calculus", "Linear Algebra", "DCDSL"
        );
        subjectsComboBox.setValue("Choose Subject");

        Label materialLabel = new Label("Material Type:");
        materialComboBox = new ComboBox<>();
        materialComboBox.getItems().addAll("Choose Material Type", "Paperback", "E books", "Courses");
        materialComboBox.setValue("Choose Material Type");

        Label priceLabel = new Label("Maximum Price:");
        priceSlider = new Slider(0, 10000, 5000); // Min, Max, Default
        priceSlider.setShowTickLabels(true);
        priceSlider.setShowTickMarks(true);
        priceSlider.setMajorTickUnit(1000);
        priceSlider.setMinorTickCount(5);
        priceSlider.setBlockIncrement(100);

        Label ratingLabel = new Label("Minimum Rating:");
        ratingSlider = new Slider(0, 5, 4.5); // Min, Max, Default
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setMinorTickCount(0);
        ratingSlider.setBlockIncrement(0.1);

        // Add change listeners to update slider values dynamically
        priceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Price: " + newValue);
        });

        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Rating: " + newValue);
        });

        // Create and add a "Recommend" button
        Button recommendButton = new Button("Recommend");
        recommendButton.setOnAction(event -> {
            List<StudyMaterial> recommendations = getRecommendations();
            displayRecommendations(recommendations);
        });
        leftPanel.getChildren().addAll(subjectsLabel, subjectsComboBox, materialLabel, materialComboBox, priceLabel, priceSlider, ratingLabel, ratingSlider, recommendButton);
        leftPanel.getChildren().add(new Separator());

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(leftPanel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rightPanel);
        borderPane.setCenter(scrollPane);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<StudyMaterial> getRecommendations() {
        try {
            String selectedSubject = subjectsComboBox.getValue();
            String selectedMaterial = materialComboBox.getValue();
            double maxPrice = priceSlider.getValue();
            double maxRating = ratingSlider.getValue();
            int topN = 3;

            switch (selectedMaterial) {
                case "Paperback":
                    String publisher = ""; // Default publisher
                    return recommender.filterPaperbacks(selectedSubject, maxPrice, maxRating, publisher, topN);
                case "E books":
                    return recommender.filterEBooks(selectedSubject, maxPrice, maxRating, topN);
                case "Courses":
                    String offeredBy = ""; // Default offered by
                    String languages = ""; // Default languages
                    return recommender.filterCourses(selectedSubject, maxPrice, maxRating, topN, offeredBy, languages);
                default:
                    return null;
            }
        } catch (SQLException | InsufficientRecommendationsException e) {
            displayError("An error occurred: " + e.getMessage());
            return null;
        }
    }

    private void displayRecommendations(List<StudyMaterial> recommendations) {
        rightPanel.getChildren().clear();

        for (StudyMaterial recommendation : recommendations) {
            if (recommendation instanceof Paperback) {
                Paperback paperback = (Paperback) recommendation;
                GridPane bookGrid = createBookGrid(paperback, paperback.getImageUrl());
                rightPanel.getChildren().add(bookGrid);
            } else if (recommendation instanceof EBook) {
                EBook eBook = (EBook) recommendation;
                GridPane bookGrid = createBookGrid(eBook, eBook.getImageUrl());
                rightPanel.getChildren().add(bookGrid);
            } else if (recommendation instanceof Course) {
                Course course = (Course) recommendation;
                GridPane bookGrid = createBookGrid(course, ""); // Pass empty string as image URL for Course
                rightPanel.getChildren().add(bookGrid);
            }
        }
    }

    private GridPane createBookGrid(StudyMaterial recommendation, String imageUrl) {
        GridPane bookGrid = new GridPane();
        bookGrid.setHgap(10);
        bookGrid.setVgap(10);
        bookGrid.setPadding(new Insets(10));

        Label titleLabel = new Label("Title: " + recommendation.getTitle());
        Label detailsLabel = null;

        if (recommendation instanceof Paperback) {
            Paperback paperback = (Paperback) recommendation;
            detailsLabel = new Label("Author: " + paperback.getAuthor() + "\nPublisher: " + paperback.getPublisher() +
                    "\nPrice: " + paperback.getPrice() + "\nRating: " + paperback.getRating() +
                    "\nNumber of Ratings: " + paperback.getNoOfRatings() + "\nURL: " + paperback.getUrl());
        } else if (recommendation instanceof EBook) {
            EBook eBook = (EBook) recommendation;
            detailsLabel = new Label("Author: " + eBook.getAuthor() +
                    "\nPrice: " + eBook.getPrice() + "\nRating: " + eBook.getRating() +
                    "\nNumber of Ratings: " + eBook.getNoOfRatings() + "\nURL: " + eBook.getUrl());
        } else if (recommendation instanceof Course) {
            Course course = (Course) recommendation;
            detailsLabel = new Label("Offered By: " + course.getOfferedBy() +
                    "\nPrice: " + course.getPrice() + "\nRating: " + course.getRating() +
                    "\nNumber of Ratings: " + course.getNoOfRatings() +
                    "\nNumber of Languages Available: " + course.getNoOfLanguagesAvailable() +
                    "\nLanguages: " + course.getLanguages() + "\nNumber of People Enrolled: " +
                    course.getNoOfPeopleEnrolled() + "\nURL: " + course.getUrl());
        }

        bookGrid.add(titleLabel, 0, 0);
        bookGrid.add(detailsLabel, 0, 1);

        if (!imageUrl.isEmpty()) {
            displayImage(imageUrl, bookGrid);
        }

        return bookGrid;
    }

    private void displayImage(String imageUrl, GridPane bookGrid) {
        try {
            // Open a connection to the URL
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();

            // Read the image from the URL
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int readNum;
            while ((readNum = inputStream.read(buf)) != -1) {
                bos.write(buf, 0, readNum);
            }
            byte[] imageBytes = bos.toByteArray();

            // Create a JavaFX Image from the byte array
            Image image = new Image(new ByteArrayInputStream(imageBytes));

            // Create a JavaFX ImageView to display the image
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(200);

            // Add the ImageView to the bookGrid
            bookGrid.add(imageView, 1, 0, 1, 2);
        } catch (IOException e) {
            displayError("An error occurred while loading image: " + e.getMessage());
        }
    }

    private void displayError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
