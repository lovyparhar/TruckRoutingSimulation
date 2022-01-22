package demo19048;

import base.*;

public class Truck048 extends Truck {

	private boolean atHub, atHighway;
	private Hub lastHub, currentHub;
	private Highway currentHighway;
	private int id;

	// Final phase is the phase when the truck is going towards destination
	private boolean finalPhase;
	private int currentTime;

	// The current location in form of double is preserved
	private Double realLocX, realLocY;

	// This is for naming
	static int count = 1;

	public Truck048() {
		super();
		this.atHub = false;
		this.atHighway = false;
		this.lastHub = null;
		this.currentHub = null;
		this.currentHighway = null;
		this.finalPhase = false;
		this.id = count;
		this.currentTime = 0;
		this.realLocX = null;
		this.realLocY = null;
		count++;
	}


	public synchronized Hub getLastHub() {
		return this.lastHub;
	}


	public synchronized void enter(Highway hwy) {
		this.currentHighway = hwy;
		this.lastHub = hwy.getStart();
		this.atHighway = true;
		this.atHub = false;
	}
	

	public String getTruckName() {
		return "Truck19048-" + this.id;
	}



	protected synchronized void update(int deltaT) {

		this.currentTime += deltaT;

		// Extra steps for managing two types of locations that are stored /////
		if(this.getLoc() == null) {
			return;
		}
		// Set the real location if not set
		if(this.realLocX == null) {
			this.realLocX = Double.valueOf(this.getLoc().getX());
			this.realLocY = Double.valueOf(this.getLoc().getY());
		}
		////////////////////////////////////////////////////////////////////////


		if(currentTime < this.getStartTime()) {
			return;
		}

		if(finalPhase) {
			this.move(this.getDest(), deltaT);
			return;
		}



		// The destination hub for the truck
		Hub destinationHub = Network.getNearestHub(this.getDest());

		if(!atHub && !atHighway) {

			Hub nextHub = Network.getNearestHub(this.getLoc());

			// If it reaches a hub
			if(nextHub.getLoc().getX() == this.getLoc().getX() && 
				nextHub.getLoc().getY() == this.getLoc().getY()) {

				// If that hub was the destination hub, no need to add to that hub
				if(nextHub == destinationHub) {
					finalPhase = true;
					atHub = false;

					if(atHighway) {
						currentHighway.remove(this);
					}

					atHighway = false;
				}
				else if(nextHub.add(this)) {
					this.currentHub = nextHub;
					atHub = true;
				}
			}
			else {
				this.move(nextHub.getLoc(), deltaT);
			}

		}

		else if(atHighway) {

			// IF AT HIGHWAY, GO TOWARDS THE END HUB OF THAT HIGHWAY
			Hub nextHub = this.currentHighway.getEnd();


			// IF IT REACHED THE END HUB OF THAT HIGHWAY
			if(nextHub.getLoc().getX() == this.getLoc().getX() && 
				nextHub.getLoc().getY() == this.getLoc().getY()) {

				if(nextHub == destinationHub) {
					finalPhase = true;
					atHub = false;
					currentHighway.remove(this);
					atHighway = false;
				}
				else if(nextHub.add(this)) {
					this.currentHub = nextHub;
					currentHighway.remove(this);
					atHub = true;
					atHighway = false;
				}
			}
			else {
				this.move(nextHub.getLoc(), deltaT);
			}


		}

		else if(atHub) {

			if(destinationHub.getLoc().getX() == this.getLoc().getX() &&
				destinationHub.getLoc().getY() == this.getLoc().getY()) {

				finalPhase = true;
				atHub = false;
				atHighway = false;
			}

		}
	}



	// THIS IS THE FUNCTION HAVING ALL THE COMPLEXITIES OF MOVING THE TRUCK
	private synchronized void move(Location l2, int delT) {

		double distanceBwLocations = (this.realLocX - l2.getX())*(this.realLocX - l2.getX()) + 
							(this.realLocY - l2.getY())*(this.realLocY - l2.getY());

		distanceBwLocations = Math.sqrt(distanceBwLocations);


		double cosTheta = (l2.getX() - this.realLocX)/distanceBwLocations;
		double sinTheta = (l2.getY() - this.realLocY)/distanceBwLocations;



		double time = delT;
		double speed;

		if(finalPhase) {
			speed = 100;
		}
		else if(atHighway) {
			speed = this.currentHighway.getMaxSpeed();
		}
		else {
			speed = 100;
		}


		double totalDis = speed * (time/1000.0);
		double xDistance = speed * cosTheta * (time/1000.0);
		double yDistance = speed * sinTheta * (time/1000.0);

		if(totalDis > distanceBwLocations) {
			this.setLoc(l2);
			return;
		}

		this.realLocX += xDistance;
		this.realLocY += yDistance;

		Long x = Math.round(this.realLocX);
		Long y = Math.round(this.realLocY);

		Location newLocation = new Location(x.intValue(), y.intValue());

		this.setLoc(newLocation);

	}	
}
