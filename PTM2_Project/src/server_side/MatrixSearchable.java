package server_side;

import java.util.ArrayList;
import java.util.List;

public class MatrixSearchable implements Searchable<Position> {

	double[][] mat;
	Position initialState;
	Position goalState;

	public MatrixSearchable(double[][] mat, Position initialPosition, Position goalPosition) {
		initialState = initialPosition;
		goalState = goalPosition;
		this.mat = mat;
	}

	@Override
	public State<Position> getInitialState() {
		if (((int) (initialState.getX()) >= 0) && ((int) (initialState.getX()) < mat.length)
				&& ((int) (initialState.getY()) >= 0)
				&& ((int) (initialState.getY()) < mat[(int) (initialState.getX())].length))
			return new State<Position>(initialState, mat[(int) (initialState.getX())][(int) initialState.getY()], null);
		else
			return new State<Position>(initialState, mat[0][0], null);

	}

	@Override
	public boolean isGoalState(State<Position> state) {
		return goalState.equals(state.getState());
	}

	// calculate the possible moves including costs
	@Override
	public ArrayList<State<Position>> getAllPossibleStates(State<Position> s) {
		ArrayList<State<Position>> neighbours = new ArrayList<>();
		List<Position> lst = getMatrixMoves(s.getState());
		for (Position pos : lst) {
			try {
				neighbours.add(new State<Position>(pos, s.getCost() + mat[(int) pos.getY()][(int) pos.getX()], s));
			} catch (Exception e) {
				int a = 1;
			}
		}

		return neighbours;

	}

	// calculate the possible moves
	public List<Position> getMatrixMoves(Position p) {

		ArrayList<Position> moves = new ArrayList<>();
		int x = (int) p.getX();
		int y = (int) p.getY();

		if (isValid(x + 1, y))
			moves.add(new Position(x + 1, y));
		if (isValid(x, y + 1))
			moves.add(new Position(x, y + 1));
		if (isValid(x - 1, y))
			moves.add(new Position(x - 1, y));
		if (isValid(x, y - 1))
			moves.add(new Position(x, y - 1));

		return moves;
	}

	public boolean isValid(int x, int y) {
		return (y >= 0 && y < mat.length && x >= 0 && x < mat[0].length);		 
	}

	@Override
	public State<Position> getGoalState() {
		return new State<Position>(goalState, mat[0][0], null);
	}
}
