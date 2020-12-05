package expression;

public class Minus extends BinaryExpression {

	public Minus(Expression otherLeft, Expression otherRight) {
		super(otherLeft, otherRight);
	}

	@Override
	public double calculate() {
		return (this.left.calculate() - this.right.calculate());
	}
}