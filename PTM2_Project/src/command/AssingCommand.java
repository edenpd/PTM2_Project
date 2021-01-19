package command;

import java.util.ArrayList;

import expression.ShuntingYardAlgorithm;
import expression.Variable;

public class AssingCommand extends Command {

	public AssingCommand() {
		super();
	}

	@Override
	public int execute() {
		ArrayList<String[]> tokens = this.interpreter.getTokens();
		int indexBlockOfTokens = this.interpreter.getIndexBlockOfTokens();
		int indexToken = this.interpreter.getIndexToken();
		String variableServerName = tokens.get(indexBlockOfTokens)[indexToken - 1];

		if (tokens.get(indexBlockOfTokens)[indexToken + 1].equals("bind")) {
			return 0;
		}

		ArrayList<String> expression = new ArrayList<String>();
		String[] block = this.interpreter.getTokens().get(this.interpreter.getIndexBlockOfTokens());

		for (int i = (indexToken + 1); i < block.length; i++) {
			expression.add(block[i]);
		}

		double result = ShuntingYardAlgorithm.execute(expression, this.interpreter.getServerSymbolTable());

		if (this.interpreter.getServerSymbolTable().containsKey(variableServerName) == true) {
			Variable serverVariable = this.interpreter.getServerSymbolTable().get(variableServerName);
			serverVariable.setValue(result);
			this.interpreter.getServerSymbolTable().put(variableServerName, serverVariable);
		} else {
			System.out.println("Error occured...There is not variable with the name: " + variableServerName + "...");
		}

		this.interpreter.setIndexToken(expression.size() + this.interpreter.getIndexToken());

		return 0;
	}
}
