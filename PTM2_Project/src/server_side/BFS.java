package server_side;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class BFS<T> extends commonSearcher<T> {

	@Override
	public List<State<T>> search(Searchable<T> s) {
		System.out.println("searching path!");
		addToOpenList(s.getInitialState());
		this.evaluatedNodes++;

		HashSet<State<T>> closedSet = new HashSet<State<T>>();
		double currentCost;

		while (openList.size() > 0) {

			State<T> n = popOpenList();
			closedSet.add(n);
//			System.out.println("state:" + n.toString());
			if (n.equals(s.getGoalState())) {
				this.clearAll();
				return backTrace(n);
			}

			ArrayList<State<T>> successors = s.getAllPossibleStates(n);
			for (State state : closedSet) {
				if (successors.contains(state))
					successors.remove(state);
			}
//			successors.removeIf(temp -> closedSet.contains(temp));

			for (State state : successors) {
				currentCost = n.getCost() + state.getFixedCost();
				if (!closedSet.contains(state) && !this.openList.contains(state)) {
					this.evaluatedNodes++;
					state.setCost(currentCost);

					state.setCameFrom(n);
					addToOpenList(state);
				} else {
					if (state.getCost() > currentCost) {
						if (!this.openList.contains(state)) {
							addToOpenList(state);
						} else {
							this.openList.remove(state);
							state.setCost(currentCost);
							state.setCameFrom(n);
							addToOpenList(state);
						}
					}
				}
			}
		}

		return null;
	}

	protected State<T> popOpenList() {
		return this.openList.poll();
	}

	protected void addToOpenList(State<T> s) {
		this.openList.add(s);
	}

	private ArrayList<State<T>> backTrace(State<T> goalState) {
		State<T> currentState = goalState;
		ArrayList<State<T>> routeList = new ArrayList<State<T>>();

		while (currentState.getCameFrom() != null) {
			routeList.add(currentState);
			currentState = currentState.getCameFrom();
		}
		routeList.add(currentState);
		Collections.reverse(routeList);
		return routeList;
	}

	private void clearAll() {
		this.openList.clear();
		this.evaluatedNodes = 0;
	}

//	@Override
//	public List<State<T>> search(Searchable<T> searchable) {
//		System.out.println("searching path!");
//
////		PriorityQueue<State<T>> openList = new PriorityQueue<>((s1, s2) -> Double.compare(s1.getCost(), s2.getCost()));
////		HashSet<State<T>> closedSet = new HashSet<State<T>>();
//		PriorityQueue<State<T>> openList = new PriorityQueue<State<T>>();
//
//
//		openList.add(searchable.getInitialState());
//
//		while (!openList.isEmpty()) {
//			
//			State<T> currentState = openList.poll();
//			closedList.add(currentState);
//			
//			if (searchable.isGoalState(currentState)) {
//				System.out.println("Path found!");
//				return currentState.backTrace();
//			}
//			
//			List<State<T>> possibleStates = searchable.getAllPossibleStates(currentState);
//			
//			for(State<T> state : possibleStates) {
//				if(!closedList.contains(state)) {
//					if(!openList.contains(state)) {
//						state.setCameFrom(currentState);
//						openList.add(state);
//					} else {
//						if(currentState.getCost() + state.getCost() < state.getCost()) {							
//							openList.remove(state);
//							state.setCameFrom(currentState);
//							openList.add(state);
//						}
//					}
//				}
//			}
//			
////			for(State<T> state : possibleStates) {
////				if (!openList.contains(state))
////					openList.add(state);
////			}
//
//		}
//		return null;
//	}
}
