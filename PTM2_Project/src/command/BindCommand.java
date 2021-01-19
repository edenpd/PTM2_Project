package command;

import java.util.ArrayList;

import expression.SimulatorVariable;
import expression.Variable;

public class BindCommand extends Command {

	public BindCommand() {
		super();
	}

	@Override
	public int execute() {
		ArrayList<String[]> tokens = this.interpreter.getTokens();
		int indexBlockOfTokens = this.interpreter.getIndexBlockOfTokens();
		int indexToken = this.interpreter.getIndexToken();
		String variableSimulatorName = tokens.get(indexBlockOfTokens)[indexToken + 1];
		String variableServerName = tokens.get(indexBlockOfTokens)[indexToken - 2];

		SimulatorVariable simulatorVariable = this.interpreter.getSimulatorSymbolTable().get(variableSimulatorName);
		Variable serverVariable = null;

		if (this.interpreter.getServerSymbolTable().containsKey(variableServerName) == true) {
			serverVariable = this.interpreter.getServerSymbolTable().get(variableServerName);
		} else {
			System.out.println("Error occured...There is not variable with the name: " + variableServerName + "...");
			return 0;
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		simulatorVariable.addObserver(serverVariable);
		serverVariable.addObserver(simulatorVariable);

		serverVariable.setValue(simulatorVariable.getValue());

		this.interpreter.setIndexToken(indexToken + 1);
		return 0;
	}
}