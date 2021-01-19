package command;

import java.util.ArrayList;

import expression.ExpressionCommand;
import expression.ShuntingYardAlgorithm;

public class ConditionParser extends Command {

	protected ArrayList<String[]> commandList;
	protected ArrayList<String> leftExpression;
	protected ArrayList<String> rightExpression;
	protected String operator;
	protected int startIndexBlockOfTokens;

	public ConditionParser() {
		super();
		this.commandList = new ArrayList<String[]>();
		this.leftExpression = new ArrayList<String>();
		this.rightExpression = new ArrayList<String>();
		this.operator = null;
		this.startIndexBlockOfTokens = 0;

	}

	@Override
	public int execute() {
		this.startIndexBlockOfTokens = this.interpreter.getIndexBlockOfTokens();
		getCondition();

		this.interpreter.setIndexBlockOfTokens(this.interpreter.getIndexBlockOfTokens() + 1);
		this.interpreter.setIndexToken(0);

		getCommands();
		return 0;
	}

	private void getCondition() {
		ArrayList<String[]> tokens = this.interpreter.getTokens();
		int indexBlockOfTokens = this.interpreter.getIndexBlockOfTokens();
		int indexToken = this.interpreter.getIndexToken() + 1;
		boolean check = true;

		while (check == true) {
			switch (tokens.get(indexBlockOfTokens)[indexToken]) {
			case "<":
				check = false;
				break;

			case ">":
				check = false;
				break;

			case "=":
				check = false;
				break;

			case "!":
				check = false;
				break;

			default:
				this.leftExpression.add(tokens.get(indexBlockOfTokens)[indexToken]);
				indexToken++;
				break;
			}
		}

		this.operator = tokens.get(indexBlockOfTokens)[indexToken];

		if (tokens.get(indexBlockOfTokens)[indexToken + 1].equals("=")) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(this.operator).append("=");
			this.operator = stringBuilder.toString();
			indexToken++;
		}

		indexToken++;

		while (true) {
			if (tokens.get(indexBlockOfTokens)[indexToken].equals("{")) {
				break;
			}

			this.rightExpression.add(tokens.get(indexBlockOfTokens)[indexToken]);
			indexToken++;
		}
	}

	private void getCommands() {
		ArrayList<String[]> tokens = this.interpreter.getTokens();
		int indexBlockOfTokens = this.interpreter.getIndexBlockOfTokens();

		while (true) {
			if (tokens.get(indexBlockOfTokens)[0].equals("}")) {
				break;
			} else
				this.commandList.add(tokens.get(indexBlockOfTokens));

			indexBlockOfTokens++;
		}
	}

	protected boolean checkCondtion() {
		double leftResult = ShuntingYardAlgorithm.execute(leftExpression, this.interpreter.getServerSymbolTable());
		double rightResult = ShuntingYardAlgorithm.execute(rightExpression, this.interpreter.getServerSymbolTable());

		switch (this.operator) {
		case ">":
			return (leftResult > rightResult);
		case "<":
			return (leftResult < rightResult);
		case "==":
			return (leftResult == rightResult);
		case "!=":
			return (leftResult != rightResult);
		case "<=":
			return (leftResult <= rightResult);
		case ">=":
			return (leftResult >= rightResult);
		}

		return false;
	}

	protected void executeListOfCommands() {
		// Loops at tokens
		for (int indexBlockOfTokens = 0; indexBlockOfTokens < this.commandList.size(); indexBlockOfTokens++) {
			// Loops at words of token
			for (int indexToken = 0; indexToken < this.commandList.get(indexBlockOfTokens).length; indexToken++) {
				Command command = this.interpreter.getCommandFactory()
						.getCommand(this.commandList.get(indexBlockOfTokens)[indexToken]);
				if (command != null) {
					this.interpreter.setIndexBlockOfTokens(indexBlockOfTokens + this.startIndexBlockOfTokens + 1);
					this.interpreter.setIndexToken(indexToken);
					command.setInterpreter(this.interpreter);
					new ExpressionCommand(command).calculate();
				}
			}
		}
	}
}