package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private VBox vBox;
    private void setVBox(IMapElement mapElement, String fileName) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(fileName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        Label label = new Label();
        label.setFont(new Font("Arial", 10));
        if (mapElement instanceof Animal)
            label.setText(mapElement.getPosition().toString());
        else
            label.setText(mapElement.toString());
        vBox = new VBox(imageView, label);
        vBox.setAlignment(Pos.CENTER);
    }

    public VBox getVBox(){
        return vBox;
    }

    GuiElementBox(IMapElement mapElement) throws FileNotFoundException {
        setVBox(mapElement, mapElement.getMapElementLookFile());
    }

}
