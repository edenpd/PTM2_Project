package command;

import java.util.ArrayList;

import expression.ShuntingYardAlgorithm;

public class SleepCommand extends Command {

	public SleepCommand() {
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

		int timeToSleep = (int) ShuntingYardAlgorithm.execute(expression, this.interpreter.getServerSymbolTable());

		try {
			Thread.sleep(timeToSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.interpreter.setIndexToken(expression.size() + this.interpreter.getIndexToken());

		return 0;
	}
}