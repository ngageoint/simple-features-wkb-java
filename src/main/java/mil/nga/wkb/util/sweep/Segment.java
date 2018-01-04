package mil.nga.wkb.util.sweep;

import mil.nga.wkb.geom.Point;

/**
 * Line segment of an edge between two points
 * 
 * @author osbornb
 * @since 1.0.5
 */
public class Segment {

	/**
	 * Edge number
	 */
	private int edge;

	/**
	 * Polygon ring number
	 */
	private int ring;

	/**
	 * Left point
	 */
	private Point leftPoint;

	/**
	 * Right point
	 */
	private Point rightPoint;

	/**
	 * Segment above
	 */
	private Segment above;

	/**
	 * Segment below
	 */
	private Segment below;

	/**
	 * Constructor
	 */
	public Segment() {

	}

	/**
	 * Get the edge number
	 * 
	 * @return edge number
	 */
	public int getEdge() {
		return edge;
	}

	/**
	 * Set the edge number
	 * 
	 * @param edge
	 *            edge number
	 */
	public void setEdge(int edge) {
		this.edge = edge;
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
	 * Set the polygon ring number
	 * 
	 * @param ring
	 *            polygon ring number
	 */
	public void setRing(int ring) {
		this.ring = ring;
	}

	/**
	 * Get the left point
	 * 
	 * @return left point
	 */
	public Point getLeftPoint() {
		return leftPoint;
	}

	/**
	 * Set the left point
	 * 
	 * @param leftPoint
	 *            left point
	 */
	public void setLeftPoint(Point leftPoint) {
		this.leftPoint = leftPoint;
	}

	/**
	 * Get the right point
	 * 
	 * @return right point
	 */
	public Point getRightPoint() {
		return rightPoint;
	}

	/**
	 * Set the right point
	 * 
	 * @param rightPoint
	 *            right point
	 */
	public void setRightPoint(Point rightPoint) {
		this.rightPoint = rightPoint;
	}

	/**
	 * Get the segment above
	 * 
	 * @return segment above
	 */
	public Segment getAbove() {
		return above;
	}

	/**
	 * Set the segment above
	 * 
	 * @param above
	 *            segment above
	 */
	public void setAbove(Segment above) {
		this.above = above;
	}

	/**
	 * Get the segment below
	 * 
	 * @return segment below
	 */
	public Segment getBelow() {
		return below;
	}

	/**
	 * Set the segment below
	 * 
	 * @param below
	 *            segment below
	 */
	public void setBelow(Segment below) {
		this.below = below;
	}

}
