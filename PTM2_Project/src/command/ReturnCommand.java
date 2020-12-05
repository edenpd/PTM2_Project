package command;

import java.util.ArrayList;

import expression.ShuntingYardAlgorithm;

public class ReturnCommand extends Command {

	public ReturnCommand() {
		super();
	}

	@Override
	public int execute() {
		int indexToken = this.interpeter.getIndexToken();
		String[] block = this.interpeter.getTokens().get(this.interpeter.getIndexBlockOfTokens());
		ArrayList<String> expression = new ArrayList<String>();

		for (int i = (indexToken + 1); i < block.length; i++) {
			expression.add(block[i]);
		}

		this.interpeter
				.setReturnedValue(ShuntingYardAlgorithm.execute(expression, this.interpeter.getServerSymbolTable()));

		this.interpeter.setIndexToken(expression.size() + this.interpeter.getIndexToken());

		return 0;
	}
}