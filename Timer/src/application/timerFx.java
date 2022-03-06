package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Deze class load de user interface en haalt de layout data van de MainPanel1.fxml via de FXMLLoader methode, 
 * zet het in een variabele root van het type Parent die verantwoordelijk is voor de scene operaties, 
 * de root Node word dan in de scene gezet.
 * 
 * @author Eric Cordero Castillo
 */
public class timerFx extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root =  FXMLLoader.load(getClass().getClassLoader().getResource("MainPanel1.fxml"));   
		Scene scene = new Scene(root);	
		stage.initStyle(StageStyle.UNDECORATED); 
		stage.setScene(scene);						
		stage.show(); 
		scene.getRoot();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
