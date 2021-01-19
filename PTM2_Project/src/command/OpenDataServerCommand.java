package command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import expression.ShuntingYardAlgorithm;
import expression.SimulatorVariable;
import interpreter.Interpreter;

public class OpenDataServerCommand extends Command {

	private static volatile boolean isConnected = false;
	private int port;
	private int latency;
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static BufferedReader inputFromClient = null;

	public OpenDataServerCommand() {
		super();
		this.port = 0;
		this.latency = 0;
	}

	@Override
	public int execute() {
		calculateExpression();

		new Thread(() -> runServer()).start();

		return 0;
	}

	public static void stopServer() {
		isConnected = false;
	}

	private void runServer() {
		try {
			while (isConnected == false) {
				serverSocket = new ServerSocket(port);
//				serverSocket.setSoTimeout(1000);
				clientSocket = serverSocket.accept();
				inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				isConnected = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		readLines();

		try {
			inputFromClient.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				if (clientSocket.isClosed() == false) {
					clientSocket.close();
				}
				serverSocket.close();
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void readLines() {
		String[] variblesNames = { "simX", "simY", "simZ" };

		String[] variblesValues;
		String line;

		try {
			while (isConnected == true) {

				if ((line = inputFromClient.readLine()) == null) {
					continue;
				} else {
					variblesValues = line.split(",");

					for (int i = 0; i < variblesNames.length; i++) {
						if (isConnected == false) {
							break;
						}
						String simulatorVariableName = variblesNames[i];
						double simulatorVariableValue = Double.parseDouble(variblesValues[i]);

						SimulatorVariable simulatorVariable = this.interpreter.getSimulatorSymbolTable()
								.get(simulatorVariableName);

						if (simulatorVariable != null) {
							simulatorVariable.setValue(simulatorVariableValue);
						} else {
							simulatorVariable = new SimulatorVariable(simulatorVariableValue, simulatorVariableName);
							simulatorVariable.setValue(simulatorVariableValue);
							this.interpreter.getSimulatorSymbolTable().put(simulatorVariableName, simulatorVariable);
						}
					}

					Thread.sleep(1000 / this.latency);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void calculateExpression() {
		ArrayList<String[]> tokens = this.interpreter.getTokens();
		int indexBlockOfTokens = this.interpreter.getIndexBlockOfTokens();
		int indexToken = this.interpreter.getIndexToken() + 1;
		int lengthOfBlock = tokens.get(indexBlockOfTokens).length;
		String[] str = tokens.get(indexBlockOfTokens);
		ArrayList<String> list = new ArrayList<String>();

		// Creates the server's port
		for (; indexToken < (lengthOfBlock - 1); indexToken++) {
			// Checks for:
			// 1. [..."number", "number"...]
			// 2. [...")", "number"...]
			// 3. [...")", "("...]
			// 4. [..."number", "("...]
			if ((ShuntingYardAlgorithm.isDouble(str[indexToken]) && ShuntingYardAlgorithm.isDouble(str[indexToken + 1])
					|| (str[indexToken].equals(")") && ShuntingYardAlgorithm.isDouble(str[indexToken + 1]))
					|| (str[indexToken].equals(")") && str[indexToken + 1].equals("("))
					|| (ShuntingYardAlgorithm.isDouble(str[indexToken]) && str[indexToken + 1].equals("(")))) {
				list.add(str[indexToken]);
				break;
			}
			list.add(str[indexToken]);
		}

		this.port = (int) ShuntingYardAlgorithm.execute(list, new Interpreter().getServerSymbolTable());

		list.clear();

		for (; indexToken < lengthOfBlock; indexToken++) {
			list.add(str[indexToken]);
		}

		this.latency = (int) ShuntingYardAlgorithm.execute(list, this.interpreter.getServerSymbolTable());

		this.interpreter.setIndexToken(str.length);
	}

}