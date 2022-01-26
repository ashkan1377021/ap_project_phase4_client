package com.example.clientgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class manages the login of each user to the application
 */
public class client extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            String username;
            System.out.println("username:");
            Scanner input = new Scanner(System.in);
            username = input.nextLine();
            Parent root;
            File file = new File("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\remember me\\" + username + ".txt");
            if (!file.exists()) {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signIn-view.fxml")));
                Scene scene = new Scene(root);
                stage.setTitle("Sign In");
                stage.setScene(scene);
            } else {
                FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\remember me\\" + username + ".txt");
                Scanner sc = new Scanner(fileReader);
                if (sc.hasNext()) {
                    String st = sc.next();
                    if (st.equals("True")) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline-view.fxml"));
                        root = (Parent) loader.load();
                        TimelineController timelineController = loader.getController();
                        Scene scene = new Scene(root);
                        timelineController.setUsername(username);
                        stage.setTitle("Timeline");
                        stage.setScene(scene);
                    } else {
                        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signIn-view.fxml")));
                        Scene scene = new Scene(root);
                        stage.setTitle("Sign In");
                        stage.setScene(scene);
                    }
                } else {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signIn-view.fxml")));
                    Scene scene = new Scene(root);
                    stage.setTitle("Sign In");
                    stage.setScene(scene);
                }
                fileReader.close();
            }
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}