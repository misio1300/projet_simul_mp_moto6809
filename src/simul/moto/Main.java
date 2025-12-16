package simul.moto;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MOTOController.fxml"));
			Parent root = fxmlLoader.load();
			MOTOController Controller = fxmlLoader.getController();
			Logique logic = new Logique(Controller);
			Controller.setLogic(logic);
			Logique.Inii(logic);
			//AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MOTOController.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
