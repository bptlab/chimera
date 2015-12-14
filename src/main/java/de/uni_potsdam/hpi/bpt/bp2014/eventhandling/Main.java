package de.uni_potsdam.hpi.bpt.bp2014.eventhandling;

import java.util.concurrent.*;

/**
 * Created by Jonas on 02.12.2015.
 */
public class Main {
	public static void main(String[] args) throws InterruptedException{

		EventQueryQueue q = new EventQueryQueue();
		q.registerQuery("DemoQuery", "SELECT * FROM TemperatureEvent WHERE Temperature > 20", "test@test.com", "http://localhost:8080");
		System.out.println("registered Event");
		System.out.println("uuid is " + q.getUuid());
		System.out.println("waiting for event");
		q.receiveEvent();

		while (!q.hasReceived) {
			//so am i
			System.out.println("still waiting");
			Thread.sleep(5000);
		}
		System.out.println("received Event");
	}
}
