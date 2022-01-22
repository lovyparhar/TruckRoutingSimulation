package demo19048;

import base.*;
import java.util.*;

// Highways are unidirectional

public class Highway048 extends Highway {
	
	private LinkedList<Truck> truckQueue;

	public Highway048() {
		super();
		truckQueue = new LinkedList<Truck>();
	}
	
	// returns true if Highway is not full
	// i.e. number of trucks is below capacity
	public synchronized boolean hasCapacity() {
		if(truckQueue.size() < this.getCapacity()) {
			return true;
		}
		return false;
	}	
	
	// fails if already at capacity
	public synchronized boolean add(Truck truck) {
		if(!this.hasCapacity()) {
			return false;
		}
		else {
			if(!truckQueue.contains(truck)) {
				truckQueue.add(truck);
			}
			return true;
		}
	}

	public synchronized void remove(Truck truck) {
		if(truckQueue.contains(truck)) {
			truckQueue.remove(truck);
		}
	}

	
}
	
