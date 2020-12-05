package command;

public class DisconnectCommand extends Command {

	public DisconnectCommand() {
		super();
	}

	@Override
	public int execute() {
		ConnectCommand.closeConnection();

		OpenDataServerCommand.stopServer();

		return 1;
	}

}