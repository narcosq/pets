import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class PetAdoptionSystem extends Application {

    private ObservableList<Pet> availablePets;
    private ObservableList<Pet> myPets;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pet Adoption System");

        availablePets = FXCollections.observableArrayList();
        myPets = FXCollections.observableArrayList();

        // Sample data
        availablePets.add(new Pet("Dog", "Buddy", "Male"));
        availablePets.add(new Pet("Cat", "Whiskers", "Female"));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // Available Pets ListView
        ListView<Pet> availablePetsListView = new ListView<>(availablePets);
        availablePetsListView.setPrefWidth(200);
        availablePetsListView.setPrefHeight(200);
        availablePetsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // My Pets ListView
        ListView<Pet> myPetsListView = new ListView<>(myPets);
        myPetsListView.setPrefWidth(200);
        myPetsListView.setPrefHeight(200);
        myPetsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Buttons
        Button adoptButton = new Button("Adopt");
        adoptButton.setOnAction(e -> adoptPet(availablePetsListView, myPetsListView));

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> returnPet(availablePetsListView, myPetsListView));

        // Button for Donation
        Button donateButton = new Button("Donate");
        donateButton.setOnAction(e -> showDonateDialog());

        // Button for Veterinarian Questions
        Button vetQuestionsButton = new Button("Questions for the Veterinarian");
        vetQuestionsButton.setOnAction(e -> showVetQuestionsDialog());

        // Add controls to grid
        grid.add(new Label("Available Pets"), 0, 0);
        grid.add(availablePetsListView, 0, 1);
        grid.add(new Label("My Pets"), 1, 0);
        grid.add(myPetsListView, 1, 1);
        grid.add(adoptButton, 0, 2);
        grid.add(returnButton, 1, 2);
        grid.add(donateButton, 0, 3);
        grid.add(vetQuestionsButton, 1, 3);

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
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

    private void showDonateDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Support the Project");
        alert.setHeaderText(null);
        alert.setContentText("Thank you for considering supporting our Pet Adoption System project!\n\n" +
                "To make a donation, please visit our website or contact us directly.\n\n");

        alert.showAndWait();
    }

    private void showVetQuestionsDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Questions for the Veterinarian");
        alert.setHeaderText(null);

        Text text = new Text("If you have any questions for the veterinarian, "
                + "please visit our website and use the contact form.");

        Hyperlink link = new Hyperlink("Visit our website");
        link.setOnAction(e -> {
            // Open the website in the default browser
            getHostServices().showDocument("https://vetgalaxy.ru/articles/10-voprosov-kotorye-vy-dolzhny-zadat-veterinarnomu-vrachu");
        });

        // Create a VBox to hold the text and hyperlink
        VBox vbox = new VBox(text, link);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        // Set the content of the alert to the VBox
        alert.getDialogPane().setContent(vbox);

        alert.showAndWait();
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

        @Override
        public String toString() {
            return type + ": " + name + " (" + gender + ")";
        }
    }
}
