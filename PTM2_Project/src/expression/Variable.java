package expression;

import java.util.Observable;
import java.util.Observer;

public class Variable extends Observable implements Expression, Observer {
	
	private double value;
	private String name;

	public Variable(double otherValue, String otherName) {
		this.value = otherValue;
		this.name = otherName;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double otherValue) {
		this.value = otherValue;
		setChanged();
		notifyObservers(this.value);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String otherName) {
		this.name = otherName;
	}

	@Override
	public double calculate() {
		return getValue();
	}

	@Override
	public String toString() {
		return ((Double) (this.value)).toString();
	}

	@Override
	public void update(Observable observable, Object arg) {
		Variable otherVariable = (Variable) observable;

		if (this.value != otherVariable.getValue()) {
			setValue(otherVariable.getValue());
		}
	}
}