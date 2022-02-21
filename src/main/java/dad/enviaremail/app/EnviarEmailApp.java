package dad.enviaremail.app;

import dad.enviaremail.controller.EnviarEmailController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EnviarEmailApp extends Application {

	EnviarEmailController eec;

	@Override
	public void start(Stage primaryStage) throws Exception {

		eec = new EnviarEmailController();
		Scene scene = new Scene(eec.getRoot());

		primaryStage.setTitle("Enviar email");
		primaryStage.getIcons().add(new Image("/img/email-send-icon-32x32.png"));
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
