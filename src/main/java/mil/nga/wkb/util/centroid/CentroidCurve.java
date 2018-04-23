package mil.nga.wkb.util.centroid;

import java.util.List;

import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.util.GeometryUtils;
import mil.nga.wkb.util.WkbException;

/**
 * Calculate the centroid from curve based geometries. Implementation based on
 * the JTS (Java Topology Suite) CentroidLine.
 * 
 * @author osbornb
 * @since 1.0.3
 */
public class CentroidCurve {

	/**
	 * Sum of curve point locations
	 */
	private Point sum = new Point();

	/**
	 * Total length of curves
	 */
	private double totalLength = 0;

	/**
	 * Constructor
	 */
	public CentroidCurve() {

	}

	/**
	 * Constructor
	 * 
	 * @param geometry
	 *            geometry to add
	 */
	public CentroidCurve(Geometry geometry) {
		add(geometry);
	}

	/**
	 * Add a curve based dimension 1 geometry to the centroid total. Ignores
	 * dimension 0 geometries.
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void add(Geometry geometry) {

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case LINESTRING:
		case CIRCULARSTRING:
			add((LineString) geometry);
			break;
		case MULTILINESTRING:
			MultiLineString multiLineString = (MultiLineString) geometry;
			addLineStrings(multiLineString.getLineStrings());
			break;
		case COMPOUNDCURVE:
			CompoundCurve compoundCurve = (CompoundCurve) geometry;
			addLineStrings(compoundCurve.getLineStrings());
			break;
		case GEOMETRYCOLLECTION:
		case MULTICURVE:
		case MULTISURFACE:
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			List<Geometry> geometries = geomCollection.getGeometries();
			for (Geometry subGeometry : geometries) {
				add(subGeometry);
			}
			break;
		case POINT:
		case MULTIPOINT:
			// Doesn't contribute to curve dimension
			break;
		default:
			throw new WkbException("Unsupported "
					+ this.getClass().getSimpleName() + " Geometry Type: "
					+ geometryType);
		}
	}

	/**
	 * Add line strings to the centroid total
	 * 
	 * @param lineStrings
	 *            line strings
	 */
	private void addLineStrings(List<LineString> lineStrings) {
		for (LineString lineString : lineStrings) {
			add(lineString);
		}
	}

	/**
	 * Add a line string to the centroid total
	 * 
	 * @param lineString
	 *            line string
	 */
	private void add(LineString lineString) {
		add(lineString.getPoints());
	}

	/**
	 * Add points to the centroid total
	 * 
	 * @param points
	 *            points
	 */
	private void add(List<Point> points) {
		for (int i = 0; i < points.size() - 1; i++) {
			Point point = points.get(i);
			Point nextPoint = points.get(i + 1);

			double length = GeometryUtils.distance(point, nextPoint);
			totalLength += length;

			double midX = (point.getX() + nextPoint.getX()) / 2;
			sum.setX(sum.getX() + (length * midX));
			double midY = (point.getY() + nextPoint.getY()) / 2;
			sum.setY(sum.getY() + (length * midY));
		}
	}

	/**
	 * Get the centroid point
	 * 
	 * @return centroid point
	 */
	public Point getCentroid() {
		Point centroid = new Point(sum.getX() / totalLength, sum.getY()
				/ totalLength);
		return centroid;
	}

}
