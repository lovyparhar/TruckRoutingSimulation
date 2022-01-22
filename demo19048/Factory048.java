package demo19048;

import base.*;

public class Factory048 extends Factory {

	public Network createNetwork() {
		return (new Network048());
	}
	public Highway createHighway() {
		return (new Highway048());
	}
	public Hub createHub(Location location) {
		return (new Hub048(location));
	}
	public Truck createTruck() {
		return (new Truck048());
	}
	
}
