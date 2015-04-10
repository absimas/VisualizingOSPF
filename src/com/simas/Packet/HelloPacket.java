package com.simas.Packet;

import com.simas.MainFrame;

import java.util.ArrayList;
import java.util.List;

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
	public int priority;
	// The maximum time before a silent router is declared down (no Hello packets received)
	public int deadInterval;
	public String designatedRouter;
	public String backupDesignatedRouter;
	// The router IDs from which valid Hello packet was received in the last DeadInterval time
	public List<Integer> neighbor = new ArrayList<>();

	public HelloPacket(int routerId, String netMask) {
		super(routerId, Type.HELLO);
		networkMask = netMask;
		helloInterval = MainFrame.helloInterval;
		deadInterval = MainFrame.deadInterval;
	}

	public HelloPacket(int routerId, String netMask, int helloInterval, int deadInterval) {
		super(routerId, Type.HELLO);
		networkMask = netMask;
		this.helloInterval = helloInterval;
		this.deadInterval = deadInterval;
	}

}
