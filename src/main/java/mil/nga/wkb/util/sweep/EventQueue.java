package mil.nga.wkb.util.sweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mil.nga.wkb.geom.LineString;
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
	 * @param ring
	 *            polygon ring
	 */
	public EventQueue(LineString ring) {
		addRing(ring, 0);
		sort();
	}

	/**
	 * Constructor
	 * 
	 * @param rings
	 *            polygon rings
	 */
	public EventQueue(List<LineString> rings) {
		for (int i = 0; i < rings.size(); i++) {
			LineString ring = rings.get(i);
			addRing(ring, i);
		}
		sort();
	}

	/**
	 * Add a ring to the event queue
	 * 
	 * @param ring
	 *            polygon ring
	 * @param ringIndex
	 *            ring index
	 */
	private void addRing(LineString ring, int ringIndex) {

		List<Point> points = ring.getPoints();

		for (int i = 0; i < points.size(); i++) {
			Event endpoint1 = new Event();
			Event endpoint2 = new Event();

			endpoint1.setEdge(i);
			endpoint1.setRing(ringIndex);
			endpoint2.setEdge(i);
			endpoint2.setRing(ringIndex);
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
	}

	/**
	 * Sort the events
	 */
	private void sort() {
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
