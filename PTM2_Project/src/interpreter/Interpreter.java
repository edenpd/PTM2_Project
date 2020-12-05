package interpreter;

import java.util.ArrayList;
import java.util.HashMap;

import command.Command;
import command.CommandFactory;
import expression.ExpressionCommand;
import expression.SimulatorVariable;
import expression.Variable;

public class Interpreter {
	// Data members
	private HashMap<String, Variable> serverSymbolTable;
	private HashMap<String, SimulatorVariable> simulatorSymbolTable;
	private CommandFactory commandFactory;
	private ArrayList<String[]> tokens;
	private double returnedValue;
	private int indexToken;
	private int indexBlockOfTokens;

	public Interpreter() {
		this.serverSymbolTable = new HashMap<String, Variable>();
		this.simulatorSymbolTable = new HashMap<String, SimulatorVariable>();
		this.tokens = new ArrayList<String[]>();
		this.commandFactory = new CommandFactory();
		this.returnedValue = 0;
		this.indexToken = 0;
		this.indexBlockOfTokens = 0;
	}

	// Getters & Setters
	public HashMap<String, Variable> getServerSymbolTable() {
		return this.serverSymbolTable;
	}

	public HashMap<String, SimulatorVariable> getSimulatorSymbolTable() {
		return this.simulatorSymbolTable;
	}

	public CommandFactory getCommandFactory() {
		return this.commandFactory;
	}

	public void setTokens(ArrayList<String[]> otherTokens) {
		this.tokens = otherTokens;
	}

	public ArrayList<String[]> getTokens() {
		return tokens;
	}

	public void setReturnedValue(double otherReturnedValue) {
		this.returnedValue = otherReturnedValue;
	}

	public double getReturnedValue() {
		return this.returnedValue;
	}

	public void setIndexToken(int otherIndexToken) {
		this.indexToken = otherIndexToken;
	}

	public int getIndexToken() {
		return this.indexToken;
	}

	public void setIndexBlockOfTokens(int otherIndexBlockOfTokens) {
		this.indexBlockOfTokens = otherIndexBlockOfTokens;
	}

	public int getIndexBlockOfTokens() {
		return this.indexBlockOfTokens;
	}

	public double interpret(String[] codeLines) {
		resetInterpeter();
		lexing(codeLines);
		parsing();
//		printData();
		return getReturnedValue();
	}

	public void lexing(String[] codeLines) {
		for (String line : codeLines) {

			line = line.replaceAll("\\{", "\\ { ").replaceAll("\\}", "\\ } ").replaceAll("\\>", "\\ > ")
					.replaceAll("\\<", "\\ < ").replaceAll("\\+", "\\ + ").replaceAll("\\-", "\\ - ")
					.replaceAll("\\*", "\\ * ").replaceAll("\\/", "\\ / ").replaceAll("\\(", "\\ ( ")
					.replaceAll("\\)", "\\ ) ").replaceAll("\\=", "\\ = ").trim();

			this.tokens.add(line.split("\\s+"));
		}
	}

	public void printData() {
		System.out.println("\nResults:");
		System.out.println(this.serverSymbolTable.keySet());
		System.out.println(this.simulatorSymbolTable.keySet());

		for (Variable var : this.serverSymbolTable.values()) {
			System.out.print(var.getName() + " = " + var.getValue() + ", ");
		}
		System.out.println("");
		for (Variable var : this.simulatorSymbolTable.values()) {
			System.out.print(var.getName() + " = " + var.getValue() + ", ");
		}
		System.out.println("");
	}

	public void parsing() {
		// Loops at tokens
		for (this.indexBlockOfTokens = 0; this.indexBlockOfTokens < this.tokens.size(); this.indexBlockOfTokens++) {
			// Loops at words in token
			for (this.indexToken = 0; this.indexToken < this.tokens.get(indexBlockOfTokens).length; this.indexToken++) {
				Command command = this.commandFactory.getCommand(this.tokens.get(indexBlockOfTokens)[this.indexToken]);
				if (command != null) {
					command.setInterpeter(this);
					new ExpressionCommand(command).calculate();
				}
			}
		}
	}

	// Resets variables
	private void resetInterpeter() {
		this.tokens.clear();
		this.tokens = null;
		this.tokens = new ArrayList<String[]>();
		this.indexToken = 0;
		this.indexBlockOfTokens = 0;
		this.returnedValue = 0;
		this.serverSymbolTable.clear();
		this.simulatorSymbolTable.clear();
		this.simulatorSymbolTable.put("simX", new SimulatorVariable(0.0, "simX"));
		this.simulatorSymbolTable.put("simY", new SimulatorVariable(0.0, "simY"));
		this.simulatorSymbolTable.put("simZ", new SimulatorVariable(0.0, "simZ"));
	}
}