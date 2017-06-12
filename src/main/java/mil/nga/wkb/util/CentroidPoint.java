package mil.nga.wkb.util;

import java.util.List;

import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.Point;

/**
 * Calculate the centroid from point based geometries. Implementation based on
 * the JTS (Java Topology Suite) CentroidPoint.
 * 
 * @author osbornb
 * @since 1.0.3
 */
public class CentroidPoint {

	/**
	 * Point count
	 */
	private int count = 0;

	/**
	 * Sum of point locations
	 */
	private Point sum = new Point();

	/**
	 * Constructor
	 */
	public CentroidPoint() {

	}

	/**
	 * Constructor
	 * 
	 * @param geometry
	 *            geometry to add
	 */
	public CentroidPoint(Geometry geometry) {
		add(geometry);
	}

	/**
	 * Add a point based dimension 0 geometry to the centroid total
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void add(Geometry geometry) {
		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POINT:
			add((Point) geometry);
			break;
		case MULTIPOINT:
			MultiPoint multiPoint = (MultiPoint) geometry;
			for (Point point : multiPoint.getPoints()) {
				add(point);
			}
			break;
		case GEOMETRYCOLLECTION:
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			List<Geometry> geometries = geomCollection.getGeometries();
			for (Geometry subGeometry : geometries) {
				add(subGeometry);
			}
			break;
		default:
			throw new WkbException("Unsupported "
					+ this.getClass().getSimpleName() + " Geometry Type: "
					+ geometryType);
		}
	}

	/**
	 * Add a point to the centroid total
	 * 
	 * @param point
	 *            point
	 */
	private void add(Point point) {
		count++;
		sum.setX(sum.getX() + point.getX());
		sum.setY(sum.getY() + point.getY());
	}

	/**
	 * Get the centroid point
	 * 
	 * @return centroid point
	 */
	public Point getCentroid() {
		Point centroid = new Point(sum.getX() / count, sum.getY() / count);
		return centroid;
	}

}
