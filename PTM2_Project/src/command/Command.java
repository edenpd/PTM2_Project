package command;

import interpreter.Interpreter;

public abstract class Command {

	Interpreter interpeter;

	public Command() {
		this.interpeter = null;
	}

	public abstract int execute();

	public void setInterpeter(Interpreter otherInterpeter) {
		this.interpeter = otherInterpeter;
	}
}