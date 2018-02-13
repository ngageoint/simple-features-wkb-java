package mil.nga.wkb.util.sweep;

import mil.nga.wkb.geom.Point;

/**
 * Event element
 * 
 * @author osbornb
 * @since 1.0.5
 */
public class Event implements Comparable<Event> {

	/**
	 * Edge number
	 */
	private int edge;

	/**
	 * Polygon ring number
	 */
	private int ring;

	/**
	 * Polygon point
	 */
	private Point point;

	/**
	 * Event type, left or right point
	 */
	private EventType type;

	/**
	 * Constructor
	 * 
	 * @param edge
	 *            edge number
	 * @param ring
	 *            ring number
	 * @param point
	 *            point
	 * @param type
	 *            event type
	 */
	public Event(int edge, int ring, Point point, EventType type) {
		this.edge = edge;
		this.ring = ring;
		this.point = point;
		this.type = type;
	}

	/**
	 * Get the edge
	 * 
	 * @return edge number
	 */
	public int getEdge() {
		return edge;
	}

	/**
	 * Get the polygon ring number
	 * 
	 * @return polygon ring number
	 */
	public int getRing() {
		return ring;
	}

	/**
	 * Get the polygon point
	 * 
	 * @return polygon point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * Get the event type
	 * 
	 * @return event type
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Event other) {
		return SweepLine.xyOrder(point, other.point);
	}

}