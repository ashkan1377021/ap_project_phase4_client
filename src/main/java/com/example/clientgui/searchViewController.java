package com.example.clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * this class controls search view events
 *
 * @author ashkan_mogharab
 */
public class searchViewController implements Initializable {
    String username;
    usefulMethods usefulmethods = new usefulMethods();
    @FXML
    private TextField usernameTextField;

    @FXML
    private Label resultMessage;

    /**
     * this method searches that a username is in database of the server or not and if it is shows profile of it .otherwise tells Not Found
     *
     * @param event an actionEvent
     */
    @FXML
    void doSearch(ActionEvent event) throws IOException, InterruptedException {
        if (!(usernameTextField.getText().equals("") || usernameTextField == null)) {
            Socket socket = new Socket("127.0.0.1", 7600);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + username + ".txt");
            Scanner sc = new Scanner(fileReader);
            String index = sc.next();
            fileReader.close();
            usefulmethods.send_message(out, "5");
            Thread.sleep(100);
            usefulmethods.send_message(out, index);
            Thread.sleep(100);
            usefulmethods.send_message(out, "3");
            Thread.sleep(100);
            usefulmethods.send_message(out, usernameTextField.getText());
            Thread.sleep(100);
            if (usefulmethods.read_message(in).equals("true")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile-view2.fxml"));
                Parent root = (Parent) loader.load();
                profileView2Controller profileview2controller = loader.getController();
                profileview2controller.setUsername(usernameTextField.getText(), username);
                profileview2controller.comeToProfileView2Controller(1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Profile");
                stage.show();
            } else {
                resultMessage.setText("Not Found");
            }
        }
    }

    /**
     * this method back user from search view to Timeline
     *
     * @param event an actionEvent
     */
    @FXML
    void backToTimeline(ActionEvent event) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline-view.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        TimelineController timelineController = loader.getController();
        Scene scene = new Scene(root);
        timelineController.setUsername(username);
        timelineController.comeToTimeline();
        stage.setScene(scene);
        stage.setTitle("Timeline");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * setter
     *
     * @param username a string which wants to be value of  username field
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
