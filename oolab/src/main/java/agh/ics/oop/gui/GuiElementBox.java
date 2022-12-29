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

/**
 * Class of a single object to draw, sets and gets a view of certain map element
 */
public class GuiElementBox {
    private final VBox vBox;
    public VBox getVBox(){
        return vBox;
    }
    GuiElementBox(IMapElement mapElement) throws FileNotFoundException {
//        gets image from file and crop it to 20x20
        Image image = new Image(new FileInputStream(mapElement.getMapElementLookFile()),
                20 , 20, false, false);
//        creates imageView and sets its size also to 20x20
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
//        sets label to position if it's animal and to "GRASS" if it's grass
        Label label = new Label();
        label.setFont(new Font("Arial", 10));
        if (mapElement instanceof Animal)
            label.setText(mapElement.getPosition().toString());
        else
            label.setText(mapElement.toString());
//        creates and sets vBox to one with image and label, then centers it
        vBox = new VBox(imageView, label);
        vBox.setAlignment(Pos.CENTER);
    }
}
