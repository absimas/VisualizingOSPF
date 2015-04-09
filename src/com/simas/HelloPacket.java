package com.simas;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */

public class HelloPacket extends Packet {

	// Default router multicast address = 224.0.0.5
	// DR/BDR multi cast address         = 224.0.0.6

	public String networkMask;
	public int helloInterval;
	public String options;
	public int routerPriority;
	public int routerDeadInterval;
	public String designatedRouter;
	public String backupDesignatedRouter;
	// The router IDs from which valid Hello packet was received in the last DeadInterval time.
	public int neighbour;

	public HelloPacket() {
		super(Type.HELLO);
	}

}
