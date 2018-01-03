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
	 * Event type, left or right point
	 */
	private EventType type;

	/**
	 * Polygon point
	 */
	private Point point;

	/**
	 * Constructor
	 */
	public Event() {

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
	 * Set the edge
	 * 
	 * @param edge
	 *            edge number
	 */
	public void setEdge(int edge) {
		this.edge = edge;
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
	 * Set the event type
	 * 
	 * @param type
	 *            event type
	 */
	public void setType(EventType type) {
		this.type = type;
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
	 * Set the polygon point
	 * 
	 * @param point
	 *            polygon point
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Event other) {
		return SweepLine.xyOrder(point, other.point);
	}

}