package demo19048;
import base.*;
import java.util.*;


public class Hub048 extends Hub {

	// This is a queue of trucks in form of ArrayList
	private ArrayList<Truck> truckQueue;

	// Constructor, which also initializes the list of trucks
	public Hub048(Location loc) {
		super(loc);
		truckQueue = new ArrayList<Truck>(); 
	}


	// can be used to add a Truck to the queue of the Hub, called by the highway(most prob)
	// returns false if the Q is full and add fails
	public synchronized boolean add(Truck truck) {
		if(truckQueue.size() >= getCapacity()) {
			return false;
		}
		else {
			if(!truckQueue.contains(truck)) {
				truckQueue.add(truck);
			}
			return true;
		}
	}

	// can be used to remove any truck from the queue
	public synchronized void remove(Truck truck) {
		if(truckQueue.contains(truck)) {
			truckQueue.remove(truck);
		}
	}


	///////////////////////////////////////////////////////////////



	// The dfs required to select the next highway, used in the getNextHighway function
	// It goes back to last only if it is the only option left, it never goes back if
	// there is some other path to destination.
	private synchronized Highway dfsToHighway(Hub current, Hub dest, HashSet<Hub> visited, Hub last) {

		// The current node is put to the visited set
		visited.add(current);
		// The list of possible highway choices to the truck on current hub
		ArrayList<Highway> highwayChoices = current.getHighways();


		for(Highway h : highwayChoices) {

			// If the higway connects to the destination, return it
			if(h.getEnd() == dest) {
				return h;
			}

			// If the higway connects to the last, don't consider it now, 
			// we will consider it later if no other option left
			if(h.getEnd() == last) {
				continue;
			}

			// If the highway already got visited then don't visit it again
			if(visited.contains(h.getEnd())) {
				continue;
			}

			Highway result = dfsToHighway(h.getEnd(), dest, visited, last);

			// If some route to destination was found going from h then return h
			if(result != null) {
				return h;
			}
		}


		// If everything fails then try to consider the highway to last if possible
		for(Highway h : highwayChoices) {
			if(h.getEnd() == last) {
				Highway result = dfsToHighway(h.getEnd(), dest, visited, last);

				// If some highway was road was found going from h then return h
				if(result != null) {
					return h;
				}	
			}
		}

		// If no highway is there such that you can reach the destination then return null
		return null;

	}




	// provides routing information, uses dfs to find a route(not necessarily optimal)
	public synchronized Highway getNextHighway(Hub last, Hub dest) {

		HashSet<Hub> visited = new HashSet<Hub>();

		if(this == dest)
		{
			return null;
		}

		Highway next = dfsToHighway(this, dest, visited, last);

		return next;
	}




	///////////////////////////////////////////////////////////////


	// to be implemented in derived classes. Called at each time step
	protected synchronized void processQ(int deltaT) {

		// System.out.println("Number of trucks at the hub at Location : " + this.getLoc() + " is " + truckQueue.size());
		Iterator<Truck> i = truckQueue.iterator();
		while(i.hasNext())
		{
			Truck t = i.next();

			// Get the destination hub according to the truck
			Hub destinationHub = Network.getNearestHub(t.getDest());

			// Get the next choice of highway
			Highway nextHighway = getNextHighway(t.getLastHub(), destinationHub);


			// IF THERE IS A PATH TO THE DESTINATION PUT IT ONTO THE NEXT HIGHWAY.
			if(nextHighway != null) {

				if(nextHighway.add(t)) {
					t.enter(nextHighway);
					i.remove();
				}
			} 
		}

	}

}

