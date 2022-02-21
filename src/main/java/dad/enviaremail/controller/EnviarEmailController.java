package dad.enviaremail.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import dad.enviaremail.model.EnviarEmailModel;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

public class EnviarEmailController implements Initializable {

	private EnviarEmailModel model = new EnviarEmailModel();

	@FXML
	private TextField asuntoTextField;

	@FXML
	private Button cerrarButton;

	@FXML
	private TextField destinatarioTextField;

	@FXML
	private Button enviarButton;

	@FXML
	private TextArea mensajeAreaText;

	@FXML
	private PasswordField passTextField;

	@FXML
	private TextField puertoTextField;

	@FXML
	private TextField remitenteTextField;

	@FXML
	private GridPane root;

	@FXML
	private TextField servidorTextField;

	@FXML
	private CheckBox sslCheck;

	@FXML
	private Button vaciarButton;

	public EnviarEmailController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}

	public GridPane getRoot() {
		return root;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		asuntoTextField.textProperty().bindBidirectional(model.asuntoProperty());
		destinatarioTextField.textProperty().bindBidirectional(model.destinatarioProperty());
		mensajeAreaText.textProperty().bindBidirectional(model.mensajeProperty());
		puertoTextField.textProperty().bindBidirectional(model.puertoProperty(), new NumberStringConverter());
		remitenteTextField.textProperty().bindBidirectional(model.remitenteProperty());
		servidorTextField.textProperty().bindBidirectional(model.servidorProperty());
		sslCheck.selectedProperty().bindBidirectional(model.sslProperty());

		mensajeAreaText.setWrapText(true);
	}

	@FXML
	void onCerrarButton(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void onEnviarButton(ActionEvent event) {

		Task<Void> t = new Task<Void>() {

			protected Void call() throws Exception {

				Email email = new SimpleEmail();

				email.setHostName(model.getServidor());
				email.setSmtpPort(model.getPuerto());
				email.setAuthenticator(new DefaultAuthenticator(model.getRemitente(), model.getPass()));
				email.setSSLOnConnect(model.isSsl());
				email.setFrom(model.getRemitente());
				email.setSubject(model.getAsunto());
				email.setMsg(model.getMensaje());
				email.addTo(model.getDestinatario());
				email.send();

				return null;
			}
		};

		t.setOnSucceeded(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Mensaje enviado");
			alert.setHeaderText("Mensaje Enviado con exito a '" + model.getDestinatario() + "'.");
			alert.showAndWait();

			destinatarioTextField.clear();
			asuntoTextField.clear();
			mensajeAreaText.clear();

		});

		t.setOnFailed(e -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se pudo enviar el email.");
			alert.setContentText(e.toString());
			alert.showAndWait();
		});

		new Thread(t).start();

	}

	@FXML
	void onVaciarButton(ActionEvent event) {
		asuntoTextField.clear();
		passTextField.clear();
		destinatarioTextField.clear();
		mensajeAreaText.clear();
		puertoTextField.clear();
		remitenteTextField.clear();
		servidorTextField.clear();
		sslCheck.setSelected(false);

	}

}
