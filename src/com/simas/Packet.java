package com.simas;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */

public abstract class Packet {

	// Header fields
	public int version;
	public Type packetType;
	public int packetLength;
	public int routerId;
	public int areaId;
	public String checkSum;
	public AuthType authType;
	public String authentication; // ToDo private field?

	public enum Type {
		HELLO(1),           // Hello
		DB_DESCRIPTION(2),  // Database Description
		LSR(3),             // Link State Request
		LSU(4),             // Link State Update
		LSA(5);              // Link State Acknowledgment

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

	public Packet(Type packetType) {
		this.packetType = packetType;
	}

}