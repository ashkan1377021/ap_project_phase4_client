package com.example.clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * this class controls other user profile events
 *
 * @author ashkan_mogharab
 */
public class profileView2Controller implements Initializable {
    String username;
    String username1;
    usefulMethods usefulmethods = new usefulMethods();
    @FXML
    private Label followersLabel;
    @FXML
    private Label followingLabel;
    @FXML
    private ImageView imageArea;
    @FXML
    private Label nameLabel;
    @FXML
    private TextArea bioArea;
    @FXML
    private VBox tweetsAndLikesArea;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button followOrUnfollowButton;

    /**
     * this method back main user from the other user profile to Timeline
     *
     * @param event an actionEvent
     */
    @FXML
    void backToTimeline(ActionEvent event) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline-view.fxml"));
        Parent root = (Parent) loader.load();
        TimelineController timelinecontroller = loader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        timelinecontroller.setUsername(username1);
        timelinecontroller.comeToTimeline();
        stage.setScene(scene);
        stage.setTitle("Timeline");
        stage.show();
    }

    /**
     * this method handles refresh
     *
     * @param event an actionEvent
     */
    @FXML
    void refresh(ActionEvent event) throws IOException, InterruptedException {
        comeToProfileView2Controller(1);
    }

    /**
     * this method refresh profile and show liked tweets of  other user
     *
     * @param event an actionEvent
     */
    @FXML
    void clickOnLikesButton(ActionEvent event) throws IOException, InterruptedException {
        comeToProfileView2Controller(2);
    }

    /**
     * is method refresh profile and show tweets and retweets of other user
     *
     * @param event an actionEvent
     */
    @FXML
    void clickOnTweetButton(ActionEvent event) throws IOException, InterruptedException {
        comeToProfileView2Controller(1);
    }

    /**
     * this method receives image of other user profile from server
     *
     * @param in an input stream
     */
    private Image receiveImage(InputStream in) throws IOException {
        byte[] sizeAr = new byte[4];
        in.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

        byte[] imageAr = new byte[size];
        in.read(imageAr);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
        return (convertToFxImage(image));
    }

    /**
     * this method converts a buffered image to a fx image
     *
     * @param image a buffered image
     * @return an image
     */
    private Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * this method could follow or unfollow a user with connection to server
     *
     * @param event an actionEvent
     */
    @FXML
    void followOrUnfollow(ActionEvent event) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 7600);
        OutputStream out = socket.getOutputStream();
        FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + username1 + ".txt");
        Scanner sc = new Scanner(fileReader);
        String index = sc.next();
        fileReader.close();
        usefulmethods.send_message(out, "3");
        Thread.sleep(100);
        usefulmethods.send_message(out, index);
        Thread.sleep(100);
        if (followOrUnfollowButton.getText().equals("follow")) {
            usefulmethods.send_message(out, "1");
            followOrUnfollowButton.setText("unfollow");
        } else {
            usefulmethods.send_message(out, "2");
            followOrUnfollowButton.setText("follow");
        }
        Thread.sleep(100);
        usefulmethods.send_message(out, username);
        Thread.sleep(100);
        socket.close();
        out.close();
    }

    /**
     * setter
     *
     * @param username  a string which wants to be value of  username field
     * @param username1 a string which wants to be value of  username1 field
     */
    public void setUsername(String username, String username1) {
        this.username = username;
        this.username1 = username1;
    }

    /**
     * this method show profile of other user to main user
     *
     * @param sign if it is 1 means that this method should show tweets and if it is 2  shows likes
     */
    public void comeToProfileView2Controller(int sign) throws IOException, InterruptedException {
        tweetsAndLikesArea.getChildren().clear();
        Socket socket = new Socket("127.0.0.1", 7600);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + username1 + ".txt");
        Scanner sc = new Scanner(fileReader);
        String index = sc.next();
        fileReader.close();
        usefulmethods.send_message(out, "5");
        Thread.sleep(101);
        usefulmethods.send_message(out, index);
        Thread.sleep(101);
        usefulmethods.send_message(out, "2");
        Thread.sleep(101);
        usefulmethods.send_message(out, "" + sign);
        Thread.sleep(101);
        usefulmethods.send_message(out, "2");
        Thread.sleep(101);
        usefulmethods.send_message(out, username);
        Thread.sleep(101);
        imageArea.setImage(receiveImage(in));
        nameLabel.setText("name: " + usefulmethods.read_message(in));
        usernameLabel.setText("username: " + usefulmethods.read_message(in));
        bioArea.setText(usefulmethods.read_message(in));
        followingLabel.setText(usefulmethods.read_message(in) + " Following");
        followersLabel.setText(usefulmethods.read_message(in) + " Followers");
        if (usefulmethods.read_message(in).equals("true")) {
            followOrUnfollowButton.setText("unfollow");
        } else {
            followOrUnfollowButton.setText("follow");
        }
        int tweetsOrLiked = Integer.parseInt(usefulmethods.read_message(in));
        for (int i = 0; i < tweetsOrLiked; i++) {
            node node = new node();
            if (!usefulmethods.read_message(in).equals("true")) {
                node.getLabel1().setText("You Retweeted");
            }
            node.getButton1().setText(usefulmethods.read_message(in));
            node.getButton2().setText(usefulmethods.read_message(in) + " Retweets");
            node.getButton3().setText(usefulmethods.read_message(in) + " likes");
            node.getTextarea().setText(usefulmethods.read_message(in));
            node.getLabel2().setText(usefulmethods.read_message(in));
            tweetsAndLikesArea.getChildren().add(node.asParent());

        }
        socket.close();
        in.close();
        out.close();
    }
}

