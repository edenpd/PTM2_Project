package command;

import java.util.HashMap;

public class CommandFactory {

	private HashMap<String, Command> commandCreatorMap;

	public CommandFactory() {
		this.commandCreatorMap = new HashMap<String, Command>();
		this.commandCreatorMap.put("return", new ReturnCommand());
		this.commandCreatorMap.put("var", new DefineVariableCommand());
		this.commandCreatorMap.put("=", new AssingCommand());
		this.commandCreatorMap.put("connect", new ConnectCommand());
		this.commandCreatorMap.put("disconnect", new DisconnectCommand());
		this.commandCreatorMap.put("bind", new BindCommand());
		this.commandCreatorMap.put("openDataServer", new OpenDataServerCommand());
		this.commandCreatorMap.put("sleep", new SleepCommand());
		this.commandCreatorMap.put("if", new IfCommand());
		this.commandCreatorMap.put("while", new LoopCommand());
	}

	// Returns a new type of command
	public Command getCommand(String commandName) {
		return this.commandCreatorMap.get(commandName);
	}

	// Return command exists
	public boolean containsKey(String key) {
		return this.commandCreatorMap.containsKey(key);
	}
}
