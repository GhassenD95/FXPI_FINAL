package mains.fxpi_final;

import controllers.SignInController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        loadSignInScene();
    }

    public void loadSignInScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Signin.fxml"));
            Parent root = loader.load();
            SignInController controller = loader.getController();
            controller.setMainApp(this);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSuccessfulSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/landing.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSignUpScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Signup.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setUserData(this); // Set Main instance as user data
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}