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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.Scanner;

public class profileView1Controller implements Initializable {
    String username;
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
    void editPicture(ActionEvent event) throws IOException, InterruptedException {
        FileChooser of = new FileChooser();
        File f = of.showOpenDialog(null);
        if (f != null) {
            BufferedImage image = ImageIO.read(f);
            Socket socket = new Socket("127.0.0.1", 7600);
            OutputStream out = socket.getOutputStream();
            FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + username + ".txt");
            Scanner sc = new Scanner(fileReader);
            String index = sc.next();
            fileReader.close();
            usefulmethods.send_message(out, "5");
            Thread.sleep(100);
            usefulmethods.send_message(out, index);
            Thread.sleep(100);
            usefulmethods.send_message(out, "1");
            Thread.sleep(100);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            out.write(size);
            out.write(byteArrayOutputStream.toByteArray());
            out.flush();
            Thread.sleep(1000);
            socket.close();
            out.close();
            comeToProfileView1Controller(1);
        }
    }

    @FXML
    void backToTimeline(ActionEvent event) throws IOException, InterruptedException {
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
    }

    @FXML
    void refresh(ActionEvent event) throws IOException, InterruptedException {
        comeToProfileView1Controller(1);
    }

    @FXML
    void clickOnLikesButton(ActionEvent event) throws IOException, InterruptedException {
        comeToProfileView1Controller(2);
    }

    @FXML
    void clickOnTweetButton(ActionEvent event) throws IOException, InterruptedException {
        comeToProfileView1Controller(1);
    }

    private Image receiveImage(InputStream in) throws IOException {
        byte[] sizeAr = new byte[4];
        in.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

        byte[] imageAr = new byte[size];
        in.read(imageAr);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
        return (convertToFxImage(image));
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void comeToProfileView1Controller(int sign) throws IOException, InterruptedException {
        tweetsAndLikesArea.getChildren().clear();
        Socket socket = new Socket("127.0.0.1", 7600);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\clientGui\\index\\" + username + ".txt");
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
        usefulmethods.send_message(out, "1");
        Thread.sleep(101);
        usefulmethods.send_message(out, username);
        Thread.sleep(101);
        imageArea.setImage(receiveImage(in));
        nameLabel.setText("name: " + usefulmethods.read_message(in));
        usernameLabel.setText("username: " + usefulmethods.read_message(in));
        bioArea.setText(usefulmethods.read_message(in));
        followingLabel.setText(usefulmethods.read_message(in) + " Following");
        followersLabel.setText(usefulmethods.read_message(in) + " Followers");
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
