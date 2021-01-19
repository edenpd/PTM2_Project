package server_side;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class commonSearcher<T> implements Searcher<T> {
	
//	//PriorityQueue<State<T>> openList; 
//	Set<State<T>> closedList;
//	
//	public commonSearcher() {
//		closedList = new HashSet<>();
//	}
	
	protected PriorityQueue<State<T>> openList;
    protected int evaluatedNodes;

    public commonSearcher()
    {
        Comparator<State<T>> stateCostComparator = Comparator.comparingDouble(State::getCost);

        this.openList = new PriorityQueue<>(stateCostComparator);
        this.evaluatedNodes = 0;
    }

    protected State<T> popOpenList()
    {
        this.evaluatedNodes++;
        return this.openList.poll();
    }

    @Override
    public int getNumberOfNodesEvaluated()
    {
        return this.evaluatedNodes;
    }

    @Override
    public abstract List<State<T>> search(Searchable<T> s);

}
