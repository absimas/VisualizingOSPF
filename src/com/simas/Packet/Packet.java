package com.simas.Packet;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */

public abstract class Packet {

	// Header fields
	// OSPF version // Currently 2; 3 is for IPv6
	public int version = 2;
	public Type type;
	// Packet length in bytes including the header
	public int length;
	public int routerId;
	public int areaId;
	public String checkSum;
	public AuthType authType;
	public String authentication;

	public enum Type {
		HELLO(1),           // Hello
		DB_DESCRIPTION(2),  // Database Description
		LSR(3),             // Link State Request
		LSU(4),             // Link State Update
		LSA(5);             // Link State Acknowledgment

		int id;

		Type(int id) {
			this.id = id;
		}
	}

	public enum AuthType {
		NONE,   // None
		SIMPLE, // Simple (clear text) password
		MD5;     // MD5
	}

	public Packet(int routerId, Type type) {
		this.routerId = routerId;
		this.type = type;
	}

}