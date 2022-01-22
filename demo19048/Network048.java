package demo19048;

import base.*;
import java.util.*;

// We assume only one instance of Network will be created for a program
public class Network048 extends Network {
	private ArrayList<Hub> hubs;
	private ArrayList<Highway> highways;
	private ArrayList<Truck> trucks;

	public Network048() {
		super();
		hubs = new ArrayList<Hub>();
		highways = new ArrayList<Highway>();
		trucks = new ArrayList<Truck>();
	}
	
	// keep track of the following entities
	public void add(Hub hub) {
		this.hubs.add(hub);
	}
	public void add(Highway hwy) {
		this.highways.add(hwy);
	}
	public void add(Truck truck) {
		this.trucks.add(truck);
	}
	

	
	// start the simulation
	// derived class calls start on each of the Hubs and Trucks
	public void start() {

		for(Hub h : this.hubs) {
			h.start();
		}

		for(Truck t : this.trucks) {
			t.start();
		}
	}

	// derived class calls draw on each hub, highway, and truck
	// passing in display
	public void redisplay(Display disp) {

		for(Hub hub : this.hubs) {
			hub.draw(disp);
		}

		for(Highway highway : this.highways) {
			highway.draw(disp);
		}

		for(Truck truck : this.trucks) {
			truck.draw(disp);
		}
	}
	
	protected Hub findNearestHubForLoc(Location loc) {
		Hub nearestHub = this.hubs.get(0);
		int distSq = loc.distSqrd(nearestHub.getLoc());

		for(Hub h : this.hubs) {
			if(distSq > loc.distSqrd(h.getLoc())) {
				nearestHub = h;
				distSq = loc.distSqrd(h.getLoc());
			}
		}

		return nearestHub;
	}
	
	static Network theNet;
}


