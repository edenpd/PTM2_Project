package expression;

public class Mul extends BinaryExpression {

	public Mul(Expression otherLeft, Expression otherRight) {
		super(otherLeft, otherRight);
	}

	@Override
	public double calculate() {
		return (this.left.calculate() * this.right.calculate());
	}
}