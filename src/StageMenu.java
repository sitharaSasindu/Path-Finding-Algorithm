/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sithara
 * w165448
 * 2016372
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class StageMenu extends Application {
    
  private Label title = new Label("Path Finder");   
   
Button dimentionArray20 = new Button("20x");//buttons to select which 2d array
Button dimentionArray40 = new Button("40x");

 private VBox pane = new VBox();
 private HBox hbox = new HBox(20);
 private HBox hboxSpace = new HBox(20);

	@Override
	public void start(Stage primaryStage) throws Exception {//sending 20 as parameter to Main method to draw 20x20 2d array
		dimentionArray20.setOnAction((ActionEvent e) -> {
			Main main = new Main(20);
			try {
				main.start(new Stage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        });
	
		dimentionArray40.setOnAction((ActionEvent e) -> {//drawing 40x40 array
			Main main = new Main(40);
			try {
				main.start(new Stage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        });
		
		title.setPadding(new Insets(40));
		hbox.getChildren().addAll(dimentionArray20, dimentionArray40);
                hbox.setAlignment(Pos.CENTER);
                
                pane.getChildren().addAll(title, hboxSpace, hbox);
                pane.setAlignment(Pos.CENTER);
                
	     Scene scene = new Scene(pane,300, 200);//setting up the scene and stage
	     primaryStage.setScene(scene);
	     primaryStage.setMinHeight(200);
	     primaryStage.setMinWidth(300);
	     primaryStage.setMaxHeight(200);
	     primaryStage.setMaxWidth(300);
	     primaryStage.show();
	}
	
    public static void main(String[] args) {
       launch(args);
   }

}

