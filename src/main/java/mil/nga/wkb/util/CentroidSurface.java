package mil.nga.wkb.util;

import java.util.List;

import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.PolyhedralSurface;

/**
 * Calculate the centroid from surface based geometries. Implementation based on
 * the JTS (Java Topology Suite) CentroidArea.
 * 
 * @author osbornb
 * @since 1.0.3
 */
public class CentroidSurface {

	/**
	 * Base point for triangles
	 */
	private Point base;

	/**
	 * Area sum
	 */
	private double area = 0;

	/**
	 * Sum of surface point locations
	 */
	private Point sum = new Point();

	/**
	 * Constructor
	 */
	public CentroidSurface() {

	}

	/**
	 * Constructor
	 * 
	 * @param geometry
	 *            geometry to add
	 */
	public CentroidSurface(Geometry geometry) {
		add(geometry);
	}

	/**
	 * Add a surface based dimension 2 geometry to the centroid total. Ignores
	 * dimension 0 and 1 geometries.
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void add(Geometry geometry) {

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POLYGON:
		case TRIANGLE:
			add((Polygon) geometry);
			break;
		case MULTIPOLYGON:
			MultiPolygon multiPolygon = (MultiPolygon) geometry;
			add(multiPolygon.getPolygons());
			break;
		case POLYHEDRALSURFACE:
		case TIN:
			PolyhedralSurface polyhedralSurface = (PolyhedralSurface) geometry;
			add(polyhedralSurface.getPolygons());
			break;
		case GEOMETRYCOLLECTION:
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			List<Geometry> geometries = geomCollection.getGeometries();
			for (Geometry subGeometry : geometries) {
				add(subGeometry);
			}
			break;
		case POINT:
		case MULTIPOINT:
		case LINESTRING:
		case CIRCULARSTRING:
		case MULTILINESTRING:
		case COMPOUNDCURVE:
			// Doesn't contribute to surface dimension
			break;
		default:
			throw new WkbException("Unsupported "
					+ this.getClass().getSimpleName() + " Geometry Type: "
					+ geometryType);
		}
	}

	/**
	 * Add polygons to the centroid total
	 * 
	 * @param polygons
	 *            polygons
	 */
	private void add(List<Polygon> polygons) {
		for (Polygon polygon : polygons) {
			add(polygon);
		}
	}

	/**
	 * Add a polygon to the centroid total
	 * 
	 * @param polygon
	 *            polygon
	 */
	private void add(Polygon polygon) {
		List<LineString> rings = polygon.getRings();
		add(rings.get(0));
		for (int i = 1; i < rings.size(); i++) {
			addHole(rings.get(i));
		}
	}

	/**
	 * Add a line string to the centroid total
	 * 
	 * @param lineString
	 *            line string
	 */
	private void add(LineString lineString) {
		add(true, lineString);
	}

	/**
	 * Add a line string hole to subtract from the centroid total
	 * 
	 * @param lineString
	 *            line string
	 */
	private void addHole(LineString lineString) {
		add(false, lineString);
	}

	/**
	 * Add or substract a line string to or from the centroid total
	 * 
	 * @param positive
	 *            true if an addition, false if a subtraction
	 * @param lineString
	 *            line string
	 */
	private void add(boolean positive, LineString lineString) {
		List<Point> points = lineString.getPoints();
		Point firstPoint = points.get(0);
		if (base == null) {
			base = firstPoint;
		}
		for (int i = 0; i < points.size() - 1; i++) {
			Point point = points.get(i);
			Point nextPoint = points.get(i + 1);
			addTriangle(positive, base, point, nextPoint);
		}
		Point lastPoint = points.get(points.size() - 1);
		if (firstPoint.getX() != lastPoint.getX()
				|| firstPoint.getY() != lastPoint.getY()) {
			addTriangle(positive, base, lastPoint, firstPoint);
		}
	}

	/**
	 * Add or subtract a triangle of points to or from the centroid total
	 * 
	 * @param positive
	 *            true if an addition, false if a subtraction
	 * @param point1
	 *            point 1
	 * @param point2
	 *            point 2
	 * @param point3
	 *            point 3
	 */
	private void addTriangle(boolean positive, Point point1, Point point2,
			Point point3) {

		double sign = (positive) ? 1.0 : -1.0;
		Point triangleCenter3 = centroid3(point1, point2, point3);
		double area2 = area2(point1, point2, point3);
		sum.setX(sum.getX() + (sign * area2 * triangleCenter3.getX()));
		sum.setY(sum.getY() + (sign * area2 * triangleCenter3.getY()));
		area += sign * area2;
	}

	/**
	 * Calculate three times the centroid of the point triangle
	 * 
	 * @param point1
	 *            point 1
	 * @param point2
	 *            point 2
	 * @param point3
	 *            point 3
	 * @return 3 times centroid point
	 */
	private Point centroid3(Point point1, Point point2, Point point3) {
		double x = point1.getX() + point2.getX() + point3.getX();
		double y = point1.getY() + point2.getY() + point3.getY();
		Point point = new Point(x, y);
		return point;
	}

	/**
	 * Calculate twice the area of the point triangle
	 * 
	 * @param point1
	 *            point 1
	 * @param point2
	 *            point 2
	 * @param point3
	 *            point 3
	 * @return 2 times triangle area
	 */
	private static double area2(Point point1, Point point2, Point point3) {
		return (point2.getX() - point1.getX())
				* (point3.getY() - point1.getY())
				- (point3.getX() - point1.getX())
				* (point2.getY() - point1.getY());
	}

	/**
	 * Get the centroid point
	 * 
	 * @return centroid point
	 */
	public Point getCentroid() {
		Point centroid = new Point(sum.getX() / 3 / area, sum.getY() / 3 / area);
		return centroid;
	}

}
