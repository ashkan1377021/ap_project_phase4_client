package com.example.clientgui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class signInController {
    private final usefulMethods usefulmethods = new usefulMethods();
    @FXML
    private TextField passwordField;

    @FXML
    private RadioButton remember;

    @FXML
    private TextField usernameField;

    @FXML
    private Label resultMessage;

    @FXML
    void backToSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signUp-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.show();
    }

    @FXML
    void checkSignInRequest(ActionEvent event) throws IOException, InterruptedException, NoSuchAlgorithmException {
        if (!usernameField.getText().equals("") && !passwordField.getText().equals("")) {
            Socket socket = new Socket("127.0.0.1", 7600);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            usefulmethods.send_message(out, "1");
            Thread.sleep(200);
            usefulmethods.send_message(out, "2");
            Thread.sleep(200);
            usefulmethods.send_message(out, usernameField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, usefulmethods.toHexString(usefulmethods.getSHA(passwordField.getText())));
            Thread.sleep(100);
            if (usefulmethods.read_message(in).equals("True")) {
                if (remember.isSelected()) {
                    FileWriter fileWriter = new FileWriter("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\remember me\\" + usernameField.getText() + ".txt");
                    fileWriter.write("True");
                    fileWriter.close();
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline-view.fxml"));
                Parent root = (Parent) loader.load();
                TimelineController timelineController = loader.getController();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                timelineController.setUsername(usernameField.getText());
                stage.setScene(scene);
                stage.setTitle("Timeline");
                stage.show();
            } else {
                resultMessage.setText("username or password is incorrect!");
            }
            socket.close();
            in.close();
            out.close();
        }
    }
}