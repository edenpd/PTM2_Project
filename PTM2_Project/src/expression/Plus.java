package expression;

public class Plus extends BinaryExpression {

	public Plus(Expression otherLeft, Expression otherRight) {
		super(otherLeft, otherRight);
	}

	@Override
	public double calculate() {
		return (this.left.calculate() + this.right.calculate());
	}
}