package expression;

public class Number implements Expression {

	private double value;

	public Number(double otherValue) {
		this.value = otherValue;
	}

	public void setValue(double otherValue) {
		this.value = otherValue;
	}

	public double getValue() {
		return value;
	}

	@Override
	public double calculate() {
		return this.getValue();
	}
}