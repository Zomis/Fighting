package net.zomis.fight.ext;

import java.util.ArrayList;
import java.util.List;

public class FNode<T> {

	private List<Object>	chosenIndexes;
	private T	f;

	public FNode(List<Object> chosenIndexes, T t) {
		this.chosenIndexes = new ArrayList<Object>(chosenIndexes);
		this.f = t;
	}
	
	public T getF() {
		return f;
	}
	
	public Object getIndex(int i) {
		if (chosenIndexes.size() <= i)
			throw new IllegalStateException("Index " + i + " has not been chosen yet");
		return chosenIndexes.get(i);
	}
	
	public List<Object> getChosenIndexes() {
		return new ArrayList<>(chosenIndexes);
	}

	public boolean hasIndex(int i) {
		return chosenIndexes.size() > i;
	}
	
}
