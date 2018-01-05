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

			Point point1 = points.get(i);
			Point point2 = points.get((i + 1) % points.size());

			EventType type1 = null;
			EventType type2 = null;
			if (SweepLine.xyOrder(point1, point2) < 0) {
				type1 = EventType.LEFT;
				type2 = EventType.RIGHT;
			} else {
				type1 = EventType.RIGHT;
				type2 = EventType.LEFT;
			}

			Event endpoint1 = new Event(i, ringIndex, point1, type1);
			Event endpoint2 = new Event(i, ringIndex, point2, type2);

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
