package com.example.clientgui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class node {
    private final BorderPane borderpane;
    private final Button button1;
    private final Button button2;
    private final Button button3;
    private final TextArea textarea;
    private final Label label1;
    private final Label label2;
    private final HBox hBox1;

    public node() {
        borderpane = new BorderPane();
        borderpane.setPrefWidth(600);
        borderpane.setPrefHeight(200);
        VBox vbox1 = new VBox();
        vbox1.setAlignment(Pos.TOP_LEFT);
        vbox1.setPrefWidth(600);
        vbox1.setPrefHeight(200);
        vbox1.setSpacing(5);
        hBox1 = new HBox();
        hBox1.setAlignment(Pos.TOP_CENTER);
        hBox1.setSpacing(5);
        hBox1.setPrefWidth(600);
        hBox1.setPrefHeight(27);
        VBox vbox2 = new VBox();
        vbox2.setPrefWidth(241);
        button1 = new Button();
        button1.setPrefWidth(120);
        button1.setPrefHeight(27);
        label1 = new Label();
        label1.setAlignment(Pos.CENTER_LEFT);
        label1.setPrefWidth(200);
        label1.setPrefHeight(24);
        vbox2.getChildren().add(label1);
        vbox2.getChildren().add(button1);
        label2 = new Label();
        label2.setAlignment(Pos.CENTER_LEFT);
        label2.setPrefWidth(150);
        label2.setPrefHeight(24);
        label2.setPadding(new Insets(20, 0, 0, 0));
        hBox1.getChildren().add(vbox2);
        hBox1.getChildren().add(label2);
        vbox1.getChildren().add(hBox1);
        textarea = new TextArea();
        textarea.setPrefWidth(600);
        textarea.setPrefHeight(50);
        vbox1.getChildren().add(textarea);
        HBox hBox2 = new HBox();
        hBox2.setAlignment(Pos.TOP_CENTER);
        hBox2.setSpacing(50);
        hBox2.setPrefWidth(600);
        hBox2.setPrefHeight(40);
        button2 = new Button();
        button2.setPrefWidth(120);
        button2.setPrefHeight(27);
        button3 = new Button();
        button3.setPrefWidth(120);
        button3.setPrefHeight(27);
        hBox2.getChildren().add(button2);
        hBox2.getChildren().add(button3);
        vbox1.getChildren().add(hBox2);
        borderpane.setCenter(vbox1);
    }

    public Parent asParent() {
        return borderpane;
    }

    public Button getButton1() {
        return button1;
    }

    public Button getButton2() {
        return button2;
    }

    public Button getButton3() {
        return button3;
    }

    public TextArea getTextarea() {
        return textarea;
    }

    public Label getLabel1() {
        return label1;
    }

    public Label getLabel2() {
        return label2;
    }

    public HBox getHBox1() {
        return hBox1;
    }

}
