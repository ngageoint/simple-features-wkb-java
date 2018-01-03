package mil.nga.wkb.util.sweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mil.nga.wkb.geom.Point;

/**
 * Event queue for processing events
 * 
 * @author osbornb
 * @since 1.0.5
 */
public class EventQueue implements Iterable<Event> {

	/**
	 * List of events
	 */
	private List<Event> events = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param points
	 *            polygon points
	 */
	public EventQueue(List<Point> points) {

		for (int i = 0; i < points.size(); i++) {
			Event endpoint1 = new Event();
			Event endpoint2 = new Event();

			endpoint1.setEdge(i);
			endpoint2.setEdge(i);
			endpoint1.setPoint(points.get(i));
			endpoint2.setPoint(points.get((i + 1) % points.size()));
			if (endpoint1.compareTo(endpoint2) < 0) {
				endpoint1.setType(EventType.LEFT);
				endpoint2.setType(EventType.RIGHT);
			} else {
				endpoint1.setType(EventType.RIGHT);
				endpoint2.setType(EventType.LEFT);
			}

			events.add(endpoint1);
			events.add(endpoint2);
		}

		Collections.sort(events);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Event> iterator() {
		return events.iterator();
	}

}
