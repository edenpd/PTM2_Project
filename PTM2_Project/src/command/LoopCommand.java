package command;

public class LoopCommand extends ConditionParser {

	public LoopCommand() {
		super();
	}

	@Override
	public int execute() {
		super.execute();

		while (checkCondtion()) {
			executeListOfCommands();
		}

		this.interpreter.setIndexBlockOfTokens(this.startIndexBlockOfTokens + this.commandList.size());
		this.interpreter.setIndexToken(0);
		return 0;
	}

}