package com.simas.Packet;

import com.simas.MainFrame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */

public class HelloPacket extends Packet {

	// Default router multicast address = 224.0.0.5
	// DR/BDR multi cast address        = 224.0.0.6

	// The network mask of the originating interface
	public String networkMask;
	public int helloInterval;
	public String options;
	// Default priority for all routers is 1. Priority 0 means it will not be elected as a DR/BDR
	public int priority = 1;
	// The maximum time before a silent router is declared down (no Hello packets received)
	public int deadInterval;
	public String designatedRouter;
	public String backupDesignatedRouter;
	// The router IDs from which valid Hello packet was received in the last DeadInterval time
	public Set<Integer> neighbor = new HashSet<>();

	public HelloPacket(int routerId) {
		super(routerId, Type.HELLO);
		helloInterval = MainFrame.helloInterval;
		deadInterval = MainFrame.deadInterval;
	}

	public HelloPacket(int routerId,  int helloInterval, int deadInterval) {
		super(routerId, Type.HELLO);
		this.helloInterval = helloInterval;
		this.deadInterval = deadInterval;
	}

}
