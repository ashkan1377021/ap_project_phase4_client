package com.example.clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * this class controls main user sign up events
 *
 * @author ashkan_mogharab
 */
public class signUpController {
    private final usefulMethods usefulmethods = new usefulMethods();
    @FXML
    private TextField BioField;

    @FXML
    private TextField dayField;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField monthField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField yearField;
    @FXML
    private Label resultMessage;

    /**
     * this method backs user from sign up view to sign in view
     *
     * @param event an actionEvent
     */
    @FXML
    void backToSignIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signIn-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sign In");
        stage.show();
    }

    /**
     * this method sends information of user to the server and checks that it could sign up or not
     *
     * @param event an actionEvent
     */
    @FXML
    void checkSignUpRequest(ActionEvent event) throws IOException, InterruptedException {
        if (!firstnameField.getText().equals("") && !lastnameField.getText().equals("") && !usernameField.getText().equals("")
                && !passwordField.getText().equals("") && !BioField.getText().equals("")
                && !yearField.getText().equals("") && !monthField.getText().equals("") && !dayField.getText().equals("")) {
            Socket socket = new Socket("127.0.0.1", 7600);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            usefulmethods.send_message(out, "1");
            Thread.sleep(200);
            usefulmethods.send_message(out, "1");
            Thread.sleep(200);
            usefulmethods.send_message(out, firstnameField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, lastnameField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, usernameField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, passwordField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, yearField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, monthField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, dayField.getText());
            Thread.sleep(100);
            usefulmethods.send_message(out, BioField.getText());
            Thread.sleep(100);
            String st = usefulmethods.read_message(in);
            if (st.equals("true")) {
                String index = usefulmethods.read_message(in);
                FileWriter fileWriter = new FileWriter("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + usernameField.getText() + ".txt");
                fileWriter.write(index);
                fileWriter.close();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signIn-view.fxml")));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Sign In");
                stage.show();
            } else {
                resultMessage.setText("username or Bio or birthdate is invalid!");

            }
            socket.close();
            in.close();
            out.close();
        }

    }

}

