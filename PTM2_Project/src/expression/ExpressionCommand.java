package expression;

import command.Command;

public class ExpressionCommand implements Expression {

	Command command;

	public ExpressionCommand(Command otherCommand) {
		this.command = otherCommand;
	}

	@Override
	public double calculate() {
		return this.command.execute();
	}
}