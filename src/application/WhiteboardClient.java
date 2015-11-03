package application;
	
import java.rmi.RemoteException;

import controller.ClientController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

/**
 * Main client application, load and set UI.
 * @author Yang Zhang (Lucas)
 *
 */

public class WhiteboardClient extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/UserInterface.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("/layout/ClientInterface.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setTitle ("Whiteboard");
			primaryStage.setResizable(false);
			primaryStage.show();
			
			ClientController controller = loader.<ClientController>getController();
			controller.setTitle(primaryStage);
			
			scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
		        public void handle(WindowEvent ev) {
		        	try {
						controller.chat.unregisterClientCallback(controller.clientCallback);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	primaryStage.close();
	                System.exit(0);		        	
		        }
		    });
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
