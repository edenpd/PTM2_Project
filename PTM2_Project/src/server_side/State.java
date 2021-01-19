package server_side;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class State<T> {
	private T state;	          //current position in the matrix (x,y)
	private double cost;	      //cost in the current position
	private State<T> cameFrom; 	  //from where did i came to current position
	private double costToReach;
    private double fixedCost;

	
	public State() {
	}

	public State(T state) {
		this.state=state;
	}
	
	public State(T state, double cost, State<T> cameFrom) {	
		this.state = state;
		this.cost = cost;
		this.cameFrom = cameFrom;
		this.costToReach = Integer.MAX_VALUE;
        this.fixedCost = cost;
	}
	
	public List<State<T>> backTrace() {
		ArrayList<State<T>> backtrace = new ArrayList<>();
		State<T> current = this;
		while(current.hasCameFrom()) {
			backtrace.add(current);
			current = current.getCameFrom();
		} 
		backtrace.add(current); //now we have list of states from: end->start
		Collections.reverse(backtrace); //reverse the list so we begin from: start->end
		return backtrace;
	}
	
//	@Override
//	public boolean equals(Object s) {
//		State<T> st = (State<T>) s;
//		return state.equals(st.state);
//	}
	
	public int compareTo(Object o) {
		return Double.compare(this.getCostToReach(), ((State)o).getCostToReach());
	}
	
    @Override
    public boolean equals(Object obj)
    {
    	State s = (State)obj;
        return state.equals(s.state);
    }

	public State<T> getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
		this.costToReach = this.cameFrom.costToReach + this.cost;
	}
	
	public double getCostToReach() {
		return costToReach;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public T getState() {
		return state;
	}

	public void setState(T state) {
		this.state = state;
	}
	
	public boolean hasCameFrom() {
		return cameFrom !=null;
	}	
	
	@Override
	public String toString() {
		return state+" ";
	}
	
	public double getFixedCost()
    {
        return fixedCost;
    }
}

