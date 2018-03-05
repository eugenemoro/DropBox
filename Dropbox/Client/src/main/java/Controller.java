
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
	@FXML
	public TextField userNameTextBox;
	@FXML
	public PasswordField pwdTextBox;
	@FXML
	public Label userNameLabel;
	@FXML
	public Label authInfoLabel;
	@FXML
	public TextField regUserNameTextBox;
	@FXML
	public PasswordField regPwdFirst;
	@FXML
	public PasswordField regPwdSecond;
	@FXML
	public Label regInfoLabel;
	@FXML
	public ListView<String> filesListView;

	private boolean authenticated;
	private String userName;
	private Scene scene;
	private FileChooser fileChooser = new FileChooser();
	private ObservableList<String> files;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Core core = new Core();
		authenticated = false;
	}

	/*
	Authentication scene
	 */

	public void onLoginEnter(ActionEvent actionEvent) {
		String reply = "";
		userName = "";
		if (!userNameTextBox.getText().isEmpty() && !pwdTextBox.getText().isEmpty()) {
			userName = userNameTextBox.getText();
			reply = Core.authenticate(userName, pwdTextBox.getText());
			authInfoLabel.setText(reply);
		} else {
			authInfoLabel.setText("Login/Password cannot be empty");
		}
		userNameTextBox.setText("");
		pwdTextBox.setText("");
		if (reply.equals("Successfully connected")){
			authenticated = true;
			updateFilesList();
		}
		if (!authenticated) {
			changeScene(Client.auth);
		} else {
			changeScene(Client.main);
			userNameLabel.setText(userName);
		}

	}

	public void loginBtnClicked(){
		onLoginEnter(new ActionEvent());
	}

	public void registerBtnClicked(){
		changeScene(Client.reg);
	}

	/*
	Registration scene
	 */

	public void onRegisterEnter(ActionEvent actionEvent) {
		boolean allFieldsValid = false;

		if (regUserNameTextBox.getText().isEmpty()){
			regInfoLabel.setText("User name cannot be empty!");
		} else if (!regPwdFirst.getText().equals(regPwdSecond.getText())) {
			regInfoLabel.setText("Passwords don't match!");
		} else if (regPwdFirst.getText().isEmpty()) {
			regInfoLabel.setText("Password cannot be empty");
		} else {
			allFieldsValid = true;
		}

		if (allFieldsValid) {
			String reply = Core.register(regUserNameTextBox.getText(), regPwdFirst.getText());
			regInfoLabel.setText(reply);
			if (reply.equals("Successfully registered")) {
				regUserNameTextBox.setText("");
				regPwdFirst.setText("");
				regPwdSecond.setText("");
				changeScene(Client.auth);
			}
		}
	}

	public void regRegisterBtnClicked(){
		onRegisterEnter(new ActionEvent());
	}

	public void regBackBtnClicked(){
		changeScene(Client.auth);
	}

	/*
	Main scene
	 */

	public void uploadBtnClicked(){
		fileChooser.setTitle("Upload File");
		File file = fileChooser.showOpenDialog(Client.getStage());
		if (file != null) {
			Core.uploadFile(file);
		}
		updateFilesList();
	}

	public void downloadBtnClicked(){
		String fileName = filesListView.getSelectionModel().getSelectedItem();
		fileChooser.setTitle("Save File");
		fileChooser.setInitialFileName(fileName);
		File file = fileChooser.showSaveDialog(Client.getStage());
		Core.downloadFile(fileName, file);
	}

	public void deleteBtnClicked(){
		String fileName = filesListView.getSelectionModel().getSelectedItem();
		Core.deleteFile(fileName);
		updateFilesList();
	}

	public void logOffBtnClicked(){
		Core.logOff();
		userName = "";
		filesListView.getItems().clear();
		authenticated = false;
		authInfoLabel.setText("Successfully logged off");
		changeScene(Client.auth);
	}

	/*
	Utilities
	 */

	private void changeScene(Scene scene) {
		this.scene = scene;
		Platform.runLater(() -> {
			Client.stage.setScene(scene);
			Client.stage.setResizable(false);
			Client.stage.show();
		});
	}

	private void updateFilesList() {
		if (authenticated) {
			files = FXCollections.observableArrayList(Core.getListOfFiles());
			filesListView.setItems(files);
		}
	}
}
