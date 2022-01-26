package com.example.clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
 * this class controls tweet view events
 *
 * @author ashkan_mogharab
 */
public class TweetController implements Initializable {
    usefulMethods usefulmethods = new usefulMethods();
    String username;
    @FXML
    private TextArea TweetingArea;

    @FXML
    private Label resultMessage;

    /**
     * this method backs user from tweet view to Timeline by pressing back button
     *
     * @param event an actionEvent
     */
    @FXML
    void backToTimeline1(ActionEvent event) throws IOException, InterruptedException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline-view.fxml"));
        Parent root = (Parent) loader.load();
        TimelineController timelineController = loader.getController();
        Scene scene = new Scene(root);
        timelineController.setUsername(username);
        timelineController.comeToTimeline();
        stage.setScene(scene);
        stage.setTitle("Timeline");
        stage.show();
    }

    /**
     * by pressing Tweet button,this method connects to server and add a tweet for main user then backs user from tweet view to Timeline
     *
     * @param event an actionEvent
     */
    @FXML
    void backToTimeline2(ActionEvent event) throws IOException, InterruptedException {

        Socket socket = new Socket("127.0.0.1", 7600);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + username + ".txt");
        Scanner sc = new Scanner(fileReader);
        String index = sc.next();
        fileReader.close();
        usefulmethods.send_message(out, "2");
        Thread.sleep(100);
        usefulmethods.send_message(out, index);
        Thread.sleep(100);
        usefulmethods.send_message(out, "1");
        Thread.sleep(100);
        usefulmethods.send_message(out, "1");
        Thread.sleep(100);
        usefulmethods.send_message(out, TweetingArea.getText());
        Thread.sleep(100);
        String st = usefulmethods.read_message(in);
        if (st.equals("true")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline-view.fxml"));
            Parent root = (Parent) loader.load();
            TimelineController timelineController = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            timelineController.setUsername(username);
            timelineController.comeToTimeline();
            stage.setScene(scene);
            stage.setTitle("Timeline");
            stage.show();
        } else {
            resultMessage.setText("Tweet can be up to 256 characters!");
        }
        socket.close();
        in.close();
        out.close();
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
