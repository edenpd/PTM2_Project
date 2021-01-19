package command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import expression.ShuntingYardAlgorithm;

public class ConnectCommand extends Command {

	private static volatile boolean isConnected = false;
	private static Socket simulatorServerSocket = null;
	private static PrintWriter outToServer = null;
	private static BufferedReader in;

	public ConnectCommand() {
		super();
	}

	@Override
	public int execute() {

		ArrayList<String[]> tokens = this.interpreter.getTokens();
		int indexBlockOfTokens = this.interpreter.getIndexBlockOfTokens();
		int indexToken = this.interpreter.getIndexToken();
		String ip = tokens.get(indexBlockOfTokens)[indexToken + 1];
		int port = 0;

		ArrayList<String> expression = new ArrayList<String>();
		String[] block = this.interpreter.getTokens().get(this.interpreter.getIndexBlockOfTokens());

		for (int i = (indexToken + 2); i < block.length; i++) {
			expression.add(block[i]);
		}

		port = (int) ShuntingYardAlgorithm.execute(expression, this.interpreter.getServerSymbolTable());

		while (isConnected == false) {
			try {
				simulatorServerSocket = new Socket(ip, port);
				outToServer = new PrintWriter(simulatorServerSocket.getOutputStream());

				isConnected = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.interpreter.setIndexToken(expression.size() + 1);

		return 0;
	}

	public static void sendToServer(String line) {
		if (isConnected == true) {
			outToServer.println(line);
			outToServer.flush();
		}
	}
	
	public static Double getFromServer(String line) {
		sendToServer(line);
		InputStream input;
		
		try {
			input = simulatorServerSocket.getInputStream();
			in = new BufferedReader(new InputStreamReader(input));
			String s = null;
			while(!(s = in.readLine()).contains(line)) {
				String substring = s.substring(s.indexOf("'")+1, s.lastIndexOf("'"));
				return Double.parseDouble(!substring.isEmpty()?substring:"0");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeConnection() {
		if (isConnected == true) {
			
			sendToServer("bye");
			outToServer.close();

			while (true) {
				try {
					simulatorServerSocket.close();
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			isConnected = false;
		}
	}

}