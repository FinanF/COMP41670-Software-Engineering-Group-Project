package org.example;

import org.example.model.Event;
import org.example.model.EventType;
import org.example.model.ParetoDistribution;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulation {
    public PriorityQueue<Event> events;

    public static void main(String[] args) {
        ArrayList<Event> list = new ArrayList<Event>(100);
        for (int i=0;i<100;i++){
            ParetoDistribution pd= new ParetoDistribution(i+1,100);
            list.add(new Event(pd.TimeDuration(), EventType.SOURCE_TURNS_ON, 1 ));
        }
        for (int i=0;i<100;i++){
            Event e=list.get(i);
            System.out.println(e.getTimestamp());
        }

    }
}
