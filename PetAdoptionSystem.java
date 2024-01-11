import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PetAdoptionSystem extends Application {

    private ObservableList<Pet> availablePets;
    private ObservableList<Pet> myPets;

    private static final String DB_URL = "jdbc:sqlite:PetAdoptionDB.db";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pet Adoption System");

        availablePets = FXCollections.observableArrayList();
        myPets = FXCollections.observableArrayList();
        loadDataFromDatabase();

        availablePets.add(new Pet("Dog", "Buddy", "Male"));
        availablePets.add(new Pet("Cat", "Whiskers", "Female"));

        AnchorPane root = new AnchorPane();


        // Available Pets
        ListView<Pet> availablePetsListView = new ListView<>(availablePets);
        availablePetsListView.setPrefWidth(200);
        availablePetsListView.setPrefHeight(200);
        availablePetsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // My Pets
        ListView<Pet> myPetsListView = new ListView<>(myPets);
        myPetsListView.setPrefWidth(200);
        myPetsListView.setPrefHeight(200);
        myPetsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Buttons
        Button adoptButton = new Button("Adopt");
        adoptButton.setOnAction(e -> adoptPet(availablePetsListView, myPetsListView));

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> returnPet(availablePetsListView, myPetsListView));

        // AnchorPane
        root.getChildren().addAll(
                new Label("Available Pets"),
                availablePetsListView,
                new Label("My Pets"),
                myPetsListView,
                adoptButton,
                returnButton
        );

// Anchor
        AnchorPane.setTopAnchor(new Label("Available Pets"), 10.0);
        AnchorPane.setLeftAnchor(new Label("Available Pets"), 10.0);

        AnchorPane.setTopAnchor(availablePetsListView, 30.0);
        AnchorPane.setLeftAnchor(availablePetsListView, 10.0);

        AnchorPane.setTopAnchor(new Label("My Pets"), 10.0);
        AnchorPane.setRightAnchor(new Label("My Pets"), 10.0);

        AnchorPane.setTopAnchor(myPetsListView, 30.0);
        AnchorPane.setRightAnchor(myPetsListView, 10.0);

        AnchorPane.setBottomAnchor(adoptButton, 10.0);
        AnchorPane.setLeftAnchor(adoptButton, 10.0);

        AnchorPane.setBottomAnchor(returnButton, 10.0);
        AnchorPane.setRightAnchor(returnButton, 10.0);


        Scene scene = new Scene(root, 400, 400);

        // window resize
        primaryStage.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
            resizeControls(newWidth.doubleValue(), primaryStage.getHeight(),
                    availablePetsListView, myPetsListView, adoptButton, returnButton);
        });

        primaryStage.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
            resizeControls(primaryStage.getWidth(), newHeight.doubleValue(),
                    availablePetsListView, myPetsListView, adoptButton, returnButton);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void resizeControls(double width, double height,
                                ListView<Pet> availablePetsListView, ListView<Pet> myPetsListView,
                                Button adoptButton, Button returnButton) {
        double listViewWidth = (width - 40) / 2;
        double listViewHeight = height - 80;
        double buttonWidth = (width - 40) / 2;
        double buttonHeight = 30;

        availablePetsListView.setPrefWidth(listViewWidth);
        availablePetsListView.setPrefHeight(listViewHeight);

        myPetsListView.setPrefWidth(listViewWidth);
        myPetsListView.setPrefHeight(listViewHeight);

        adoptButton.setPrefWidth(buttonWidth);
        adoptButton.setPrefHeight(buttonHeight);

        returnButton.setPrefWidth(buttonWidth);
        returnButton.setPrefHeight(buttonHeight);

        AnchorPane.setTopAnchor(availablePetsListView, 30.0);
        AnchorPane.setLeftAnchor(availablePetsListView, 10.0);

        AnchorPane.setTopAnchor(myPetsListView, 30.0);
        AnchorPane.setRightAnchor(myPetsListView, 10.0);

        AnchorPane.setBottomAnchor(adoptButton, 10.0);
        AnchorPane.setLeftAnchor(adoptButton, 10.0);

        AnchorPane.setBottomAnchor(returnButton, 10.0);
        AnchorPane.setRightAnchor(returnButton, 10.0);
    }

    private void loadDataFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            availablePets.clear();

            String query = "SELECT * FROM Pets";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String type = resultSet.getString("Type");
                    String name = resultSet.getString("Name");
                    String gender = resultSet.getString("Gender");
                    availablePets.add(new Pet(type, name, gender));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void adoptPet(ListView<Pet> availablePetsListView, ListView<Pet> myPetsListView) {
        Pet selectedPet = availablePetsListView.getSelectionModel().getSelectedItem();
        if (selectedPet != null) {
            availablePets.remove(selectedPet);
            myPets.add(selectedPet);
        }
    }

    private void returnPet(ListView<Pet> availablePetsListView, ListView<Pet> myPetsListView) {
        Pet selectedPet = myPetsListView.getSelectionModel().getSelectedItem();
        if (selectedPet != null) {
            myPets.remove(selectedPet);
            availablePets.add(selectedPet);
        }
    }

    public static class Pet {
        private String type;
        private String name;
        private String gender;

        public Pet(String type, String name, String gender) {
            this.type = type;
            this.name = name;
            this.gender = gender;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        @Override
        public String toString() {
            return type + ": " + name + " (" + gender + ")";
        }
    }
}
