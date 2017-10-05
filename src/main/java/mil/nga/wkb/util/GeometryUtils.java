package mil.nga.wkb.util;

import java.util.ArrayList;
import java.util.List;

import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.PolyhedralSurface;
import mil.nga.wkb.geom.TIN;
import mil.nga.wkb.geom.Triangle;

/**
 * Utilities for Geometry objects
 * 
 * @author osbornb
 * @since 1.0.3
 */
public class GeometryUtils {

	/**
	 * Get the dimension of the Geometry, 0 for points, 1 for curves, 2 for
	 * surfaces. If a collection, the largest dimension is returned.
	 * 
	 * @param geometry
	 *            geometry object
	 * @return dimension (0, 1, or 2)
	 */
	public static int getDimension(Geometry geometry) {

		int dimension = -1;

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POINT:
		case MULTIPOINT:
			dimension = 0;
			break;
		case LINESTRING:
		case MULTILINESTRING:
		case CIRCULARSTRING:
		case COMPOUNDCURVE:
			dimension = 1;
			break;
		case POLYGON:
		case CURVEPOLYGON:
		case MULTIPOLYGON:
		case POLYHEDRALSURFACE:
		case TIN:
		case TRIANGLE:
			dimension = 2;
			break;
		case GEOMETRYCOLLECTION:
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			List<Geometry> geometries = geomCollection.getGeometries();
			for (Geometry subGeometry : geometries) {
				dimension = Math.max(dimension, getDimension(subGeometry));
			}
			break;
		default:
			throw new WkbException("Unsupported Geometry Type: " + geometryType);
		}

		return dimension;
	}

	/**
	 * Get the Pythagorean theorem distance between two points
	 * 
	 * @param point1
	 *            point 1
	 * @param point2
	 *            point 2
	 * @return distance
	 */
	public static double distance(Point point1, Point point2) {
		double diffX = point1.getX() - point2.getX();
		double diffY = point1.getY() - point2.getY();

		double distance = Math.sqrt(diffX * diffX + diffY * diffY);

		return distance;
	}

	/**
	 * Get the centroid point of the Geometry
	 * 
	 * @param geometry
	 *            geometry object
	 * @return centroid point
	 */
	public static Point getCentroid(Geometry geometry) {
		Point centroid = null;
		int dimension = getDimension(geometry);
		switch (dimension) {
		case 0:
			CentroidPoint point = new CentroidPoint(geometry);
			centroid = point.getCentroid();
			break;
		case 1:
			CentroidCurve curve = new CentroidCurve(geometry);
			centroid = curve.getCentroid();
			break;
		case 2:
			CentroidSurface surface = new CentroidSurface(geometry);
			centroid = surface.getCentroid();
			break;
		}
		return centroid;
	}

	/**
	 * Minimize the geometry using the shortest x distance between each
	 * connected set of points. The resulting geometry point x values will be in
	 * the range: (3 * min value <= x <= 3 * max value
	 *
	 * Example: For WGS84 provide a max x of 180.0. Resulting x values will be
	 * in the range: -540.0 <= x <= 540.0
	 *
	 * Example: For web mercator provide a world width of 20037508.342789244.
	 * Resulting x values will be in the range: -60112525.028367732 <= x <=
	 * 60112525.028367732
	 *
	 * @param geometry
	 *            geometry
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	public static void minimizeGeometry(Geometry geometry, double maxX) {

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case LINESTRING:
			minimize((LineString) geometry, maxX);
			break;
		case POLYGON:
			minimize((Polygon) geometry, maxX);
			break;
		case MULTILINESTRING:
			minimize((MultiLineString) geometry, maxX);
			break;
		case MULTIPOLYGON:
			minimize((MultiPolygon) geometry, maxX);
			break;
		case CIRCULARSTRING:
			minimize((CircularString) geometry, maxX);
			break;
		case COMPOUNDCURVE:
			minimize((CompoundCurve) geometry, maxX);
			break;
		case POLYHEDRALSURFACE:
			minimize((PolyhedralSurface) geometry, maxX);
			break;
		case TIN:
			minimize((TIN) geometry, maxX);
			break;
		case TRIANGLE:
			minimize((Triangle) geometry, maxX);
			break;
		case GEOMETRYCOLLECTION:
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			for (Geometry subGeometry : geomCollection.getGeometries()) {
				minimizeGeometry(subGeometry, maxX);
			}
			break;
		default:
			break;

		}
	}

	/**
	 * Minimize the line string
	 * 
	 * @param lineString
	 *            line string
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void minimize(LineString lineString, double maxX) {

		List<Point> points = lineString.getPoints();
		if (points.size() > 1) {
			Point point = points.get(0);
			for (int i = 1; i < points.size(); i++) {
				Point nextPoint = points.get(i);
				if (point.getX() < nextPoint.getX()) {
					if (nextPoint.getX() - point.getX() > point.getX()
							- nextPoint.getX() + (maxX * 2.0)) {
						nextPoint.setX(nextPoint.getX() - (maxX * 2.0));
					}
				} else if (point.getX() > nextPoint.getX()) {
					if (point.getX() - nextPoint.getX() > nextPoint.getX()
							- point.getX() + (maxX * 2.0)) {
						nextPoint.setX(nextPoint.getX() + (maxX * 2.0));
					}
				}
			}
		}
	}

	/**
	 * Minimize the multi line string
	 * 
	 * @param multiLineString
	 *            multi line string
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void minimize(MultiLineString multiLineString, double maxX) {

		List<LineString> lineStrings = multiLineString.getLineStrings();
		for (LineString lineString : lineStrings) {
			minimize(lineString, maxX);
		}
	}

	/**
	 * Minimize the polygon
	 * 
	 * @param polygon
	 *            polygon
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void minimize(Polygon polygon, double maxX) {

		for (LineString ring : polygon.getRings()) {
			minimize(ring, maxX);
		}
	}

	/**
	 * Minimize the multi polygon
	 * 
	 * @param multiPolygon
	 *            multi polygon
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void minimize(MultiPolygon multiPolygon, double maxX) {

		List<Polygon> polygons = multiPolygon.getPolygons();
		for (Polygon polygon : polygons) {
			minimize(polygon, maxX);
		}
	}

	/**
	 * Minimize the compound curve
	 * 
	 * @param compoundCurve
	 *            compound curve
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void minimize(CompoundCurve compoundCurve, double maxX) {

		for (LineString lineString : compoundCurve.getLineStrings()) {
			minimize(lineString, maxX);
		}
	}

	/**
	 * Minimize the polyhedral surface
	 * 
	 * @param polyhedralSurface
	 *            polyhedral surface
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void minimize(PolyhedralSurface polyhedralSurface,
			double maxX) {

		for (Polygon polygon : polyhedralSurface.getPolygons()) {
			minimize(polygon, maxX);
		}
	}

	/**
	 * Normalize the geometry so all points outside of the min and max value
	 * range are adjusted to fall within the range.
	 *
	 * Example: For WGS84 provide a max x of 180.0. Resulting x values will be
	 * in the range: -180.0 <= x <= 180.0.
	 *
	 * Example: For web mercator provide a world width of 20037508.342789244.
	 * Resulting x values will be in the range: -20037508.342789244 <= x <=
	 * 20037508.342789244.
	 *
	 * @param geometry
	 *            geometry
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	public static void normalizeGeometry(Geometry geometry, double maxX) {

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POINT:
			normalize((Point) geometry, maxX);
			break;
		case LINESTRING:
			normalize((LineString) geometry, maxX);
			break;
		case POLYGON:
			normalize((Polygon) geometry, maxX);
			break;
		case MULTIPOINT:
			normalize((MultiPoint) geometry, maxX);
			break;
		case MULTILINESTRING:
			normalize((MultiLineString) geometry, maxX);
			break;
		case MULTIPOLYGON:
			normalize((MultiPolygon) geometry, maxX);
			break;
		case CIRCULARSTRING:
			normalize((CircularString) geometry, maxX);
			break;
		case COMPOUNDCURVE:
			normalize((CompoundCurve) geometry, maxX);
			break;
		case POLYHEDRALSURFACE:
			normalize((PolyhedralSurface) geometry, maxX);
			break;
		case TIN:
			normalize((TIN) geometry, maxX);
			break;
		case TRIANGLE:
			normalize((Triangle) geometry, maxX);
			break;
		case GEOMETRYCOLLECTION:
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			for (Geometry subGeometry : geomCollection.getGeometries()) {
				normalizeGeometry(subGeometry, maxX);
			}
			break;
		default:
			break;

		}

	}

	/**
	 * Normalize the point
	 * 
	 * @param point
	 *            point
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(Point point, double maxX) {

		if (point.getX() < -maxX) {
			point.setX(point.getX() + (maxX * 2.0));
		} else if (point.getX() > maxX) {
			point.setX(point.getX() - (maxX * 2.0));
		}
	}

	/**
	 * Normalize the multi point
	 * 
	 * @param multiPoint
	 *            multi point
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(MultiPoint multiPoint, double maxX) {

		List<Point> points = multiPoint.getPoints();
		for (Point point : points) {
			normalize(point, maxX);
		}
	}

	/**
	 * Normalize the line string
	 * 
	 * @param lineString
	 *            line string
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(LineString lineString, double maxX) {

		for (Point point : lineString.getPoints()) {
			normalize(point, maxX);
		}
	}

	/**
	 * Normalize the multi line string
	 * 
	 * @param multiLineString
	 *            multi line string
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(MultiLineString multiLineString, double maxX) {

		List<LineString> lineStrings = multiLineString.getLineStrings();
		for (LineString lineString : lineStrings) {
			normalize(lineString, maxX);
		}
	}

	/**
	 * Normalize the polygon
	 * 
	 * @param polygon
	 *            polygon
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(Polygon polygon, double maxX) {

		for (LineString ring : polygon.getRings()) {
			normalize(ring, maxX);
		}
	}

	/**
	 * Normalize the multi polygon
	 * 
	 * @param multiPolygon
	 *            multi polygon
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(MultiPolygon multiPolygon, double maxX) {

		List<Polygon> polygons = multiPolygon.getPolygons();
		for (Polygon polygon : polygons) {
			normalize(polygon, maxX);
		}
	}

	/**
	 * Normalize the compound curve
	 * 
	 * @param compoundCurve
	 *            compound curve
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(CompoundCurve compoundCurve, double maxX) {

		for (LineString lineString : compoundCurve.getLineStrings()) {
			normalize(lineString, maxX);
		}
	}

	/**
	 * Normalize the polyhedral surface
	 * 
	 * @param polyhedralSurface
	 *            polyhedral surface
	 * @param maxX
	 *            max positive x value in the geometry projection
	 */
	private static void normalize(PolyhedralSurface polyhedralSurface,
			double maxX) {

		for (Polygon polygon : polyhedralSurface.getPolygons()) {
			normalize(polygon, maxX);
		}
	}

	/**
	 * Simplify the ordered points (representing a line, polygon, etc) using the
	 * Douglas Peucker algorithm to create a similar curve with fewer points.
	 * Points should be in a meters unit type projection. The tolerance is the
	 * minimum tolerated distance between consecutive points.
	 *
	 * @param points
	 *            geometry points
	 * @param tolerance
	 *            minimum tolerance in meters for consecutive points
	 * @return simplified points
	 * @since 1.0.4
	 */
	public static List<Point> simplifyPoints(List<Point> points,
			double tolerance) {
		return simplifyPoints(points, tolerance, 0, points.size() - 1);
	}

	/**
	 * Simplify the ordered points (representing a line, polygon, etc) using the
	 * Douglas Peucker algorithm to create a similar curve with fewer points.
	 * Points should be in a meters unit type projection. The tolerance is the
	 * minimum tolerated distance between consecutive points.
	 *
	 * @param points
	 *            geometry points
	 * @param tolerance
	 *            minimum tolerance in meters for consecutive points
	 * @param startIndex
	 *            start index
	 * @param endIndex
	 *            end index
	 * @return simplified points
	 */
	private static List<Point> simplifyPoints(List<Point> points,
			double tolerance, int startIndex, int endIndex) {

		List<Point> result = null;

		double dmax = 0.0;
		int index = 0;

		Point startPoint = points.get(startIndex);
		Point endPoint = points.get(endIndex);

		for (int i = startIndex + 1; i < endIndex; i++) {
			Point point = points.get(i);

			double d = perpendicularDistance(point, startPoint, endPoint);

			if (d > dmax) {
				index = i;
				dmax = d;
			}
		}

		if (dmax > tolerance) {

			List<Point> recResults1 = simplifyPoints(points, tolerance,
					startIndex, index);
			List<Point> recResults2 = simplifyPoints(points, tolerance, index,
					endIndex);

			result = recResults1.subList(0, recResults1.size() - 1);
			result.addAll(recResults2);

		} else {
			result = new ArrayList<Point>();
			result.add(startPoint);
			result.add(endPoint);
		}

		return result;
	}

	/**
	 * Calculate the perpendicular distance between the point and the line
	 * represented by the start and end points. Points should be in a meters
	 * unit type projection.
	 *
	 * @param point
	 *            point
	 * @param lineStart
	 *            point representing the line start
	 * @param lineEnd
	 *            point representing the line end
	 * @return distance in meters
	 * @since 1.0.4
	 */
	public static double perpendicularDistance(Point point, Point lineStart,
			Point lineEnd) {

		double x = point.getX();
		double y = point.getY();
		double startX = lineStart.getX();
		double startY = lineStart.getY();
		double endX = lineEnd.getX();
		double endY = lineEnd.getY();

		double vX = endX - startX;
		double vY = endY - startY;
		double wX = x - startX;
		double wY = y - startY;
		double c1 = wX * vX + wY * vY;
		double c2 = vX * vX + vY * vY;

		double x2;
		double y2;
		if (c1 <= 0) {
			x2 = startX;
			y2 = startY;
		} else if (c2 <= c1) {
			x2 = endX;
			y2 = endY;
		} else {
			double b = c1 / c2;
			x2 = startX + b * vX;
			y2 = startY + b * vY;
		}

		double distance = Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));

		return distance;
	}

}
