package command;

import java.util.ArrayList;

import expression.ShuntingYardAlgorithm;

public class ReturnCommand extends Command {

	public ReturnCommand() {
		super();
	}

	@Override
	public int execute() {
		int indexToken = this.interpreter.getIndexToken();
		String[] block = this.interpreter.getTokens().get(this.interpreter.getIndexBlockOfTokens());
		ArrayList<String> expression = new ArrayList<String>();

		for (int i = (indexToken + 1); i < block.length; i++) {
			expression.add(block[i]);
		}

		this.interpreter
				.setReturnedValue(ShuntingYardAlgorithm.execute(expression, this.interpreter.getServerSymbolTable()));

		this.interpreter.setIndexToken(expression.size() + this.interpreter.getIndexToken());

		return 0;
	}
}