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
	 * 
	 * @param edge
	 *            edge number
	 * @param ring
	 *            ring number
	 * @param leftPoint
	 *            left point
	 * @param rightPoint
	 *            right point
	 */
	public Segment(int edge, int ring, Point leftPoint, Point rightPoint) {
		this.edge = edge;
		this.ring = ring;
		this.leftPoint = leftPoint;
		this.rightPoint = rightPoint;
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
	 * Get the polygon ring number
	 * 
	 * @return polygon ring number
	 */
	public int getRing() {
		return ring;
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
	 * Get the right point
	 * 
	 * @return right point
	 */
	public Point getRightPoint() {
		return rightPoint;
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
