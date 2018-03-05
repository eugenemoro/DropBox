
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class Client extends Application {

	public static Scene reg;
	public static Scene auth;
	public static Scene main;
	public static Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Controller controller = new Controller();

		try {
			FXMLLoader authLoader = new FXMLLoader(getClass().getResource("authDropBox.fxml"));
			authLoader.setController(controller);
			Parent authPanel = authLoader.load();

			FXMLLoader regLoader = new FXMLLoader(getClass().getResource("registerDropBox.fxml"));
			regLoader.setController(controller);
			Parent regPanel = regLoader.load();

			FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainDropBox.fxml"));
			mainLoader.setController(controller);
			Parent mainPanel = mainLoader.load();

			stage = primaryStage;

			auth = new Scene(authPanel, 600, 400);
			reg = new Scene(regPanel, 600, 400);
			main = new Scene(mainPanel, 600, 400);


			TextField userNameTextBox = (TextField) authPanel.lookup("#userNameTextBox");
			userNameTextBox.requestFocus();

			stage.setScene(auth);

			stage.show();
			stage.setResizable(false);
			stage.setTitle("DropBox");

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e){
					super.windowClosing(e);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Stage getStage() {
		return stage;
	}
}
