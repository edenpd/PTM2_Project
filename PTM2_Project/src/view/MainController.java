package view;

import test.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import command.ConnectCommand;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import server_side.*;

public class MainController implements Initializable {

	ViewModel viewModel;

	@FXML
	MapDisplayer mapDis;
	@FXML
	TextArea txtArea;
	@FXML
	RadioButton autopilotButton;
	@FXML
	RadioButton manualButton;
	@FXML
	ScrollBar rudderBar;
	@FXML
	ScrollBar throtlleBar;
	@FXML
	Circle bigCircle;
	@FXML
	Circle smallCircle;

	@FXML
	Text pleaseLoadText;

	ToggleGroup group = new ToggleGroup();

	public MainController() {
		this.viewModel = new ViewModel(mapDis);

	}

	public void setViewModel(ViewModel vm) {
		this.viewModel = vm;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rudderBar.setMin(-1);
		rudderBar.setMax(1);
		throtlleBar.setMin(0);
		throtlleBar.setMax(1);
		throtlleBar.setValue(0);
		autopilotButton.setToggleGroup(group);
		manualButton.setToggleGroup(group);
//		String colorValue = "radial-gradient(focus-angle 45deg, focus-distance 50%, " +
//		        "center 50% 50%, radius 50%, white 0%, black 100%)";
//		// Create the Radial Gradient       
//		RadialGradient gradient = RadialGradient.valueOf(colorValue);
//		smallCircle.setFill(gradient); 
	}

	public void LoadCSV() throws FileNotFoundException {
		FileChooser fc = new FileChooser();
		fc.setTitle("Open map file");
//		fc.setInitialDirectory(new File("./resources"));
		FileChooser.ExtensionFilter extFilter1 = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fc.getExtensionFilters().add(extFilter1);
		File chosen = fc.showOpenDialog(null);
		if (chosen != null) {
			pleaseLoadText.toBack();
			pleaseLoadText.setOpacity(0);
			mapDis.intiallizesetMapDisplayer();
			mapDis.setMapDisplayer(chosen);
			mapDis.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> mapDis.requestFocus());
			mapDis.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					mapDis.markDestByMouse(event.getX(), event.getY());
				}
			});
		}
	}

	public void autoPilot() {
		if (autopilotButton.isSelected()) {
			viewModel.runScripVM();
		}
	}

	public void throttleBarController() {
		double moveMe = throtlleBar.getValue();
		viewModel.throttleBar(moveMe);
	}

	public void rudderBarController() {
		double moveMe = rudderBar.getValue();
		viewModel.rudderBar(moveMe);
	}

	public void dragable(MouseEvent event) {
		Double maxBorder = bigCircle.getRadius();

		if (manualButton.isSelected()) {
			if ((event.getX() <= maxBorder && event.getY() <= maxBorder)
					&& (event.getX() >= -maxBorder && event.getY() >= -maxBorder)) {
				smallCircle.setCenterX(event.getX());
				smallCircle.setCenterY(event.getY());
				double aileronVal = 0.01 * (double) event.getX() % 1;
				double elevatorVal = 0.01 * (double) event.getY() % 1;
				viewModel.moveJoystic(aileronVal, elevatorVal);

				smallCircle.setOnMouseReleased(e -> {
					smallCircle.setCenterX(0);
					smallCircle.setCenterY(0);
					viewModel.moveJoystic(0, 0);
				});
			}

			else {
				smallCircle.setCenterX(0);
				smallCircle.setCenterY(0);
				viewModel.moveJoystic(0, 0);
				return;
			}
		}
	}

	public void Connect() {
		Dialog<ConnectCommand> dialog = new Dialog<ConnectCommand>();
		dialog.setTitle("Connection Dialog");
		dialog.setHeaderText("Please enter IP and Port");
		DialogPane dialogPane = dialog.getDialogPane();
		ButtonType connecectBtn = new ButtonType("Connect");
		dialogPane.getButtonTypes().addAll(connecectBtn, ButtonType.CANCEL);
		TextField textField1 = new TextField("127.0.0.1");
		TextField textField2 = new TextField("5402");
		dialogPane.setContent(new VBox(10, textField1, textField2));
		Platform.runLater(textField1::requestFocus);
		Platform.runLater(textField2::requestFocus);

		dialog.setResultConverter((ButtonType button) -> {
			if (button == connecectBtn) {
				String ip = textField1.getText();
				String port = textField2.getText();
				viewModel.connect(ip, port);
			} else if (button == ButtonType.CANCEL) {
				dialog.close();
			}
			return null;
		});

		@SuppressWarnings("unused")
		Optional<ConnectCommand> optionalResult = dialog.showAndWait();
	}

	@SuppressWarnings({ "unused", "static-access" })
	public void calculate() {
		Dialog<ConnectCommand> dialog = new Dialog<ConnectCommand>();
		dialog.setTitle("Calculate path Dialog");
		dialog.setHeaderText("Please enter IP and Port");
		DialogPane dialogPane = dialog.getDialogPane();
		ButtonType connecectBtn = new ButtonType("Connect");
		dialogPane.getButtonTypes().addAll(connecectBtn, ButtonType.CANCEL);
		TextField textField1 = new TextField("127.0.0.1");
		TextField textField2 = new TextField("5403");
		dialogPane.setContent(new VBox(10, textField1, textField2));
		Platform.runLater(textField1::requestFocus);
		Platform.runLater(textField2::requestFocus);
		Thread waitForResponse = new Thread();

		dialog.setResultConverter((ButtonType button) -> {
			if (button == connecectBtn) {
				MapDisplayer.numOfTimesEnterd++;
				MySerialServer mss = new MySerialServer(5403);
				mss.start(5403, new MyClientHandler(mapDis));
				try {
					Socket theServer = new Socket("127.0.0.1", 5403);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (button == ButtonType.CANCEL) {
				dialog.close();
			}
			return null;
		});
		Optional<ConnectCommand> optionalResult = dialog.showAndWait();
	}

	public void runScript() {
		if (autopilotButton.isSelected()) {
			new Thread(() -> {
				String script = txtArea.getText();
				if (!script.isEmpty())
					MyInterpreter.interpret(script.split("\n"));
			}).start();
//			new Thread(() -> {
//				mapDis.drawAirplaneMoves();
//			}).start();
			
		}
	}

	public void Load() {
		if (autopilotButton.isSelected()) {
			FileChooser fc = new FileChooser();
			fc.setTitle("Open script file");
//			fc.setInitialDirectory(new File("./resources"));
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt File (*.txt)", "*.txt");
			fc.getExtensionFilters().add(extFilter);
			File chosen = fc.showOpenDialog(null);
			if (chosen != null) {
				Scanner scaner;
				try {
					scaner = new Scanner(new File(chosen.getAbsolutePath()));
					while (scaner.hasNextLine()) {
						txtArea.appendText(scaner.nextLine() + "\n");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
