package expression;

public class Div extends BinaryExpression {

	public Div(Expression otherLeft, Expression otherRight) {
		super(otherLeft, otherRight);
	}

	@Override
	public double calculate() {
		return (this.left.calculate() / this.right.calculate());
	}
}