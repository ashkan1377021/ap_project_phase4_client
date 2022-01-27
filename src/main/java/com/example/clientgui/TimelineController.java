package com.example.clientgui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
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
 * this class controls Timeline events
 *
 * @author ashkan_mogharab
 */
public class TimelineController implements Initializable {
    String username;
    usefulMethods usefulmethods = new usefulMethods();
    @FXML
    private VBox TimelineArea;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * this method checks that if code of a keyEvent is valid or not and if it is goes to related view
     *
     * @param event a keyEvent
     */
    @FXML
    void shortCutKey(KeyEvent event) {
        try {
            if (event.getCode() == KeyCode.D) {
                if (scrollPane.getVvalue() != scrollPane.getVmax()) {
                    scrollPane.setVvalue(scrollPane.getVvalue() + 50.0);
                }
            } else if (event.getCode() == KeyCode.U) {
                if (scrollPane.getVvalue() != 0.0) {
                    scrollPane.setVvalue(scrollPane.getVvalue() - 50.0);
                }
            } else if (event.getCode() == KeyCode.R) {
                if (scrollPane.getHvalue() != scrollPane.getHmax()) {
                    scrollPane.setHvalue(scrollPane.getHvalue() + 1.0);
                }
            } else if (event.getCode() == KeyCode.L) {
                if (scrollPane.getHvalue() != 0) {
                    scrollPane.setHvalue(scrollPane.getHvalue() - 1.0);
                }
            } else if (event.getCode() == KeyCode.F2) {
                viewSearch(event);
            } else if (event.getCode() == KeyCode.F3) {
                viewProfile(event);
            } else if (event.getCode() == KeyCode.F4) {
                showTweetScene(event);
            } else if (event.getCode() == KeyCode.F5) {
                comeToTimeline();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * setter
     *
     * @param username a string which wants to be value of  username field
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * this method takes user to search view
     *
     * @param event an actionEvent
     */
    @FXML
    void viewSearch1(ActionEvent event) throws IOException {
        viewSearch(event);
    }

    /**
     * this method takes user to tweet view
     *
     * @param event an actionEvent
     */
    @FXML
    void showTweetScene1(ActionEvent event) throws IOException {
        showTweetScene(event);
    }

    /**
     * this method shows Timeline to user
     *
     * @param event an actionEvent
     */

    @FXML
    void RefreshTimeline1(ActionEvent event) throws IOException, InterruptedException {
        comeToTimeline();
    }

    /**
     * this method takes user to profile view
     *
     * @param event an actionEvent
     */

    @FXML
    void viewProfile1(ActionEvent event) throws IOException, InterruptedException {
        viewProfile(event);
    }

    /**
     * this method shows Timeline to user
     */
    public void comeToTimeline() throws IOException, InterruptedException {
        TimelineArea.getChildren().clear();
        Socket socket = new Socket("127.0.0.1", 7600);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + username + ".txt");
        Scanner sc = new Scanner(fileReader);
        String index = sc.next();
        fileReader.close();
        usefulmethods.send_message(out, "4");
        Thread.sleep(100);
        usefulmethods.send_message(out, index);
        Thread.sleep(100);

        int ownTweetsNumbers = Integer.parseInt(usefulmethods.read_message(in));
        for (int i = 0; i < ownTweetsNumbers; i++) {
            node node = new node();
            node.getButton1().setText(usefulmethods.read_message(in));
            node.getButton2().setText(usefulmethods.read_message(in) + " Retweets");
            node.getButton3().setText(usefulmethods.read_message(in) + " likes");
            node.getTextarea().setText(usefulmethods.read_message(in));
            node.getLabel2().setText(usefulmethods.read_message(in));
            Button deleteButton = new Button();
            deleteButton.setPrefWidth(150);
            deleteButton.setPrefHeight(27);
            deleteButton.setText("delete");
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        Socket so = new Socket("127.0.0.1", 7600);
                        OutputStream ou = so.getOutputStream();
                        usefulmethods.send_message(ou, "2");
                        Thread.sleep(100);
                        usefulmethods.send_message(ou, index);
                        Thread.sleep(100);
                        usefulmethods.send_message(ou, "2");
                        Thread.sleep(100);
                        usefulmethods.send_message(ou, node.getTextarea().getText());
                        Thread.sleep(100);
                        usefulmethods.send_message(ou, node.getLabel2().getText());
                        Thread.sleep(100);
                        comeToTimeline();
                    } catch (InterruptedException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            node.getHBox1().getChildren().add(deleteButton);
            TimelineArea.getChildren().add(node.asParent());
        }
        int favoriteUsersNumbers = Integer.parseInt(usefulmethods.read_message(in));
        for (int i = 0; i < favoriteUsersNumbers; i++) {
            int favoriteUserTweetNumbers = Integer.parseInt(usefulmethods.read_message(in));
            for (int j = 0; j < favoriteUserTweetNumbers; j++) {
                node node = new node();
                node.getLabel2().setText(usefulmethods.read_message(in));
                node.getButton2().setText(usefulmethods.read_message(in) + " Retweets");
                node.getButton3().setText(usefulmethods.read_message(in) + " likes");
                node.getTextarea().setText(usefulmethods.read_message(in));
                node.getButton1().setText(usefulmethods.read_message(in));
                node.getButton1().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfShowFavoriteUserProfile(actionEvent, node.getButton1());

                    }
                });
                node.getButton2().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfRetweet(node.getButton1(), node.getButton2(), index, node.getTextarea().getText(), node.getLabel2().getText());
                    }
                });
                node.getButton3().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfLike(node.getButton1(), node.getButton3(), index, node.getTextarea().getText(), node.getLabel2().getText());
                    }
                });
                TimelineArea.getChildren().add(node.asParent());
            }
            int favoriteUserReTweetNumbers = Integer.parseInt(usefulmethods.read_message(in));
            for (int k = 0; k < favoriteUserReTweetNumbers; k++) {
                node node = new node();
                node.getLabel1().setText(usefulmethods.read_message(in) + " Retweeted ");
                node.getButton1().setText(usefulmethods.read_message(in));
                node.getLabel2().setText(usefulmethods.read_message(in));
                node.getButton2().setText(usefulmethods.read_message(in) + " Retweets");
                node.getButton3().setText(usefulmethods.read_message(in) + " likes");
                node.getTextarea().setText(usefulmethods.read_message(in));
                node.getButton3().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfLike(node.getButton1(), node.getButton3(), index, node.getTextarea().getText(), node.getLabel2().getText());
                    }
                });
                node.getButton2().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfRetweet(node.getButton1(), node.getButton2(), index, node.getTextarea().getText(), node.getLabel2().getText());
                    }
                });
                node.getButton1().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfShowFavoriteUserProfile(actionEvent, node.getButton1());

                    }
                });
                TimelineArea.getChildren().add(node.asParent());
            }
            int favoriteUserLikedNumbers = Integer.parseInt(usefulmethods.read_message(in));
            for (int p = 0; p < favoriteUserLikedNumbers; p++) {
                node node = new node();
                node.getButton2().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfRetweet(node.getButton1(), node.getButton2(), index, node.getTextarea().getText(), node.getLabel2().getText());
                    }
                });
                node.getLabel1().setText(usefulmethods.read_message(in) + " liked");
                node.getButton1().setText(usefulmethods.read_message(in));
                node.getLabel2().setText(usefulmethods.read_message(in));
                node.getButton2().setText(usefulmethods.read_message(in) + " Retweets");
                node.getButton3().setText(usefulmethods.read_message(in) + "likes");
                node.getTextarea().setText(usefulmethods.read_message(in));
                node.getButton3().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfLike(node.getButton1(), node.getButton3(), index, node.getTextarea().getText(), node.getLabel2().getText());
                    }
                });
                node.getButton1().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        processOfShowFavoriteUserProfile(actionEvent, node.getButton1());

                    }
                });
                TimelineArea.getChildren().add(node.asParent());

            }
        }
        socket.close();
        in.close();
        out.close();
    }

    /**
     * this method shows profile of a favorite user which pressed to its username in Timeline Area
     *
     * @param actionEvent an actionEvent
     * @param button1     a button that its text is favorite user's username
     */
    private void processOfShowFavoriteUserProfile(ActionEvent actionEvent, Button button1) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile-view2.fxml"));
            Parent root = (Parent) loader.load();
            profileView2Controller profileview2controller = loader.getController();
            profileview2controller.setUsername(button1.getText(), username);
            profileview2controller.comeToProfileView2Controller(1);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method retweets a tweet or retweet of favorite user which is in Timeline Area
     *
     * @param button1     a button that its text is favorite user's username
     * @param button2     a button that its text is numbers of retweets
     * @param index       index of the main user
     * @param textOfTweet text of the tweet of favorite user which wants to be reTweeted
     * @param sendTime    send time of the tweet of favorite user which wants to be reTweeted
     */
    private void processOfRetweet(Button button1, Button button2, String index, String textOfTweet, String sendTime) {
        try {
            Socket so = new Socket("127.0.0.1", 7600);
            OutputStream ou = so.getOutputStream();
            InputStream inp = so.getInputStream();
            usefulmethods.send_message(ou, "2");
            Thread.sleep(100);
            usefulmethods.send_message(ou, index);
            Thread.sleep(100);
            usefulmethods.send_message(ou, "1");
            Thread.sleep(100);
            usefulmethods.send_message(ou, "2");
            Thread.sleep(100);
            usefulmethods.send_message(ou, button1.getText());
            Thread.sleep(100);
            usefulmethods.send_message(ou, textOfTweet);
            Thread.sleep(100);
            usefulmethods.send_message(ou, sendTime);
            Thread.sleep(100);
            if (usefulmethods.read_message(inp).equals("true")) {
                button2.setText(usefulmethods.read_message(inp) + " Retweets");
            }


            so.close();
            inp.close();
            ou.close();
            comeToTimeline();
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * this method likes a tweet or retweet of favorite user which is in Timeline Area
     *
     * @param button1     a button that its text is favorite user's username
     * @param button3     a button that its text is numbers of likes
     * @param index       index of the main user
     * @param textOfTweet text of the tweet of favorite user which wants to be liked
     * @param sendTime    send time of the tweet of favorite user which wants to be liked
     */
    private void processOfLike(Button button1, Button button3, String index, String textOfTweet, String sendTime) {
        try {
            Socket so = new Socket("127.0.0.1", 7600);
            OutputStream ou = so.getOutputStream();
            InputStream inp = so.getInputStream();
            usefulmethods.send_message(ou, "2");
            Thread.sleep(101);
            usefulmethods.send_message(ou, index);
            Thread.sleep(101);
            usefulmethods.send_message(ou, "3");
            Thread.sleep(101);
            usefulmethods.send_message(ou, button1.getText());
            Thread.sleep(101);
            usefulmethods.send_message(ou, textOfTweet);
            Thread.sleep(101);
            usefulmethods.send_message(ou, sendTime);
            Thread.sleep(101);
            if (usefulmethods.read_message(inp).equals("true")) {
                button3.setText(usefulmethods.read_message(inp) + "likes");
            }


            so.close();
            inp.close();
            ou.close();
            comeToTimeline();
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method takes user to tweet view
     *
     * @param event an event
     */
    private void showTweetScene(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tweet-view.fxml"));
        Parent root = (Parent) loader.load();
        TweetController tweetController = loader.getController();
        tweetController.setUsername(username);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tweet");
        stage.show();
    }

    /**
     * this method takes user to profile view
     *
     * @param event an event
     */
    private void viewProfile(Event event) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile-view1.fxml"));
        Parent root = (Parent) loader.load();
        profileView1Controller profileview1controller = loader.getController();
        profileview1controller.setUsername(username);
        profileview1controller.comeToProfileView1Controller(1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.show();
    }

    /**
     * this method takes user to search view
     *
     * @param event an event
     */
    private void viewSearch(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
        Parent root = (Parent) loader.load();
        searchViewController searchviewcontroller = loader.getController();
        searchviewcontroller.setUsername(username);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Search");
        stage.show();
    }

}
