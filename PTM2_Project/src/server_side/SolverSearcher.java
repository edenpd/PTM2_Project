package server_side;

import java.awt.Point;
import java.util.List;

//object adapter between solver and searcher
public class SolverSearcher implements Solver<Searchable<Position>, String> {
	
	Searcher<Position> s;
	
	public SolverSearcher(Searcher<Position> s){
		this.s=s;
	}
	
	@Override
	public String solve(Searchable<Position> p) {
		System.out.println("getting directions");
		return getDirections(s.search(p));
	}
//	
//	@Override
//	public String solve(Searchable<Position> problem) {
//		BFS bfs = new BFS();
//		List<Point> lst = bfs.search(problem);
//		Point prev = null;
//		String solution = "";
//		String direction;
//		for(Point p : lst) {
//			if(prev != null) {
//				direction = this.getDirection(prev, p);
//				solution = solution + "," + direction;
//			}
//			prev = p;
//		}
//		
//		solution = solution.substring(1);
//		
//		return solution;
//	}
//	
//	private String getDirection(Point from, Point to) {
//		if(from.x < to.x) {
//			return "Down";
//		}
//		if(from.x > to.x) {
//			return "Up";
//		}
//		if(from.y < to.y) {
//			return "Right";
//		}
//		if(from.y > to.y) {
//			return "Left";
//		}
//		return "";
//	}
//	
	
	public static String getDirections(List<State<Position>> backtrace) {
		int i=0;
		StringBuilder result = new StringBuilder();
		
		while(i< backtrace.size()-1)
		{
			if(backtrace.get(i).getState().getX() < backtrace.get(i+1).getState().getX())
			{
				result.append("Right,");
			}
			if(backtrace.get(i).getState().getX() > backtrace.get(i+1).getState().getX())
			{
				result.append("Left,");
			}
			if(backtrace.get(i).getState().getY() > backtrace.get(i+1).getState().getY())
			{
				result.append("Up,");
			}
			if(backtrace.get(i).getState().getY() < backtrace.get(i+1).getState().getY())
			{
				result.append("Down,");
			}
			i++;
		}
		System.out.println("Res is: "+ result.substring(0, result.length()-1));
		return result.substring(0, result.length()-1);
	}
	
//	public static void main(String[] args) {
//		double[][] mat = {
//				{1,1,1},
//				{50,50,1},
//				{50,50,1}
//		};
//		Position entrance = new Position(0,0);
//		Position exit = new Position(2,2);
//		MatrixSearchable ms = new MatrixSearchable(mat, entrance, exit);
//		System.out.print(new SolverSearcher(new BFS<Position>()).solve(ms));
//			//.forEach(st -> System.out.print(st+" -> "));
//		//System.out.println("done");
//	}


}
