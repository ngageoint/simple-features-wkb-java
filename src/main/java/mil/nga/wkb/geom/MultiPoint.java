package mil.nga.wkb.geom;

import java.util.List;

/**
 * A restricted form of GeometryCollection where each Geometry in the collection
 * must be of type Point.
 * 
 * @author osbornb
 */
public class MultiPoint extends GeometryCollection<Point> {

	/**
	 * Constructor
	 */
	public MultiPoint() {
		this(false, false);
	}

	/**
	 * Constructor
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	public MultiPoint(boolean hasZ, boolean hasM) {
		super(GeometryType.MULTIPOINT, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param multiPoint
	 *            multi point to copy
	 */
	public MultiPoint(MultiPoint multiPoint) {
		this(multiPoint.hasZ(), multiPoint.hasM());
		for (Point point : multiPoint.getPoints()) {
			addPoint((Point) point.copy());
		}
	}

	/**
	 * Get the points
	 * 
	 * @return points
	 */
	public List<Point> getPoints() {
		return getGeometries();
	}

	/**
	 * Set the points
	 * 
	 * @param points
	 *            points
	 */
	public void setPoints(List<Point> points) {
		setGeometries(points);
	}

	/**
	 * Add a point
	 * 
	 * @param point
	 *            point
	 */
	public void addPoint(Point point) {
		addGeometry(point);
	}

	/**
	 * Get the number of points
	 * 
	 * @return number of points
	 */
	public int numPoints() {
		return numGeometries();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new MultiPoint(this);
	}

}
