package mil.nga.wkb.util.sweep;

import mil.nga.wkb.geom.Point;

public class Event implements Comparable<Event> {

	private int edge;
	private EventType type;
	private Point point;

	public Event() {

	}

	public int getEdge() {
		return edge;
	}

	public void setEdge(int edge) {
		this.edge = edge;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Event other) {
		int value = 0;
		if (point.getX() > other.point.getX()) {
			value = 1;
		} else if (point.getX() < other.point.getX()) {
			value = -1;
		} else if (point.getY() > other.point.getY()) {
			value = 1;
		} else if (point.getY() > other.point.getY()) {
			value = -1;
		}
		return value;
	}

}