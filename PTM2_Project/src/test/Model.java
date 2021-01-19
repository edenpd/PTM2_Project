package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import command.ConnectCommand;
import expression.SimulatorVariable;
import expression.Variable;
import interpreter.Interpreter;
import server_side.MyClientHandler;
import server_side.MySerialServer;
import view.MapDisplayer;

public class Model {

	MapDisplayer mapDis;
	Interpreter interpreter;

	public Model(MapDisplayer mapDis) {
		this.mapDis = mapDis;
		this.interpreter = new Interpreter();
	}

	public void throttleBar(double move) {
		
		String path = "/controls/engines/current-engine/throttle";
		String varName = "throttle";

		if (this.interpreter.getSimulatorSymbolTable().containsKey(path)) {
			this.interpreter.getSimulatorSymbolTable().replace(path, new SimulatorVariable(move, path));
		}

		if (this.interpreter.getServerSymbolTable().containsKey(varName)) {
			this.interpreter.getServerSymbolTable().replace(varName, new Variable(move, varName));
		}

		ConnectCommand.sendToServer("set " + path + " " + move);

	}

	public void rudderBar(double move) {
		String path = "/controls/flight/rudder";
		String varName = "rudder";
		if (this.interpreter.getSimulatorSymbolTable().containsKey(path)) {
			this.interpreter.getSimulatorSymbolTable().replace(path, new SimulatorVariable(move, path));
		}

		if (this.interpreter.getServerSymbolTable().containsKey(varName)) {
			this.interpreter.getServerSymbolTable().replace(varName, new Variable(move, varName));
		}
		ConnectCommand.sendToServer("set " + path + " " + move);
	}

	public void moveJoystick(double aileronVal, double elevatorVal) {
		String aileronPath = "/controls/flight/aileron";
		String elevatorPath = "/controls/flight/elevator";
		String aileronVar = "aileron";
		String elevatorVar = "elevator";

		this.interpreter.getSimulatorSymbolTable().replace(aileronPath, new SimulatorVariable(aileronVal, aileronPath));
		this.interpreter.getServerSymbolTable().replace(aileronVar, new Variable(aileronVal, aileronVar));
		this.interpreter.getSimulatorSymbolTable().replace(elevatorPath, new SimulatorVariable(elevatorVal, elevatorPath));
		this.interpreter.getServerSymbolTable().replace(elevatorVar, new Variable(elevatorVal, elevatorVar));
		
		ConnectCommand.sendToServer("set " + aileronPath + " " + aileronVal);
		ConnectCommand.sendToServer("set " + elevatorPath + " " + elevatorVal);
	}

	public void connect(String ip, String port) {
		String[] parameters1 = { "openDataServer 5400 10" };
		this.interpreter.interpret(parameters1);
		String[] parameters2 = { "connect " + ip + " " + port };
		this.interpreter.interpret(parameters2);
	}

	public void calculatePath() {
		Thread calcThread = new Thread();
		MySerialServer mss = new MySerialServer(5403);
		mss.start(5403, new MyClientHandler(mapDis));
		try {
			Socket theServer = new Socket("127.0.0.1", 5403);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runScript() {
		try {
			BufferedReader scriptReader = new BufferedReader(new FileReader("./resources/script.txt"));
			String line;
			List<String> lines = new ArrayList<>();
			while ((line = scriptReader.readLine()) != null) {
				lines.add(line);
			}
			scriptReader.close();
			MyInterpreter.interpret(lines.toArray(new String[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
