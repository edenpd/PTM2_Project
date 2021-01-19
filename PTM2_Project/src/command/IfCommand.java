package command;

public class IfCommand extends ConditionParser {

	public IfCommand() {
		super();
	}

	@Override
	public int execute() {
		super.execute();

		if (checkCondtion()) {
			executeListOfCommands();
		}

		this.interpreter.setIndexBlockOfTokens(this.startIndexBlockOfTokens + this.commandList.size());
		this.interpreter.setIndexToken(0);
		return 0;
	}

}