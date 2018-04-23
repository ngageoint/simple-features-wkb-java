package mil.nga.wkb.util;

import java.util.List;

import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.CurvePolygon;
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
 * String representation of a Geometry
 * 
 * @author osbornb
 */
public class GeometryPrinter {

	/**
	 * Get Geometry Information as a String
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry String
	 */
	public static String getGeometryString(Geometry geometry) {

		StringBuilder message = new StringBuilder();

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POINT:
			addPointMessage(message, (Point) geometry);
			break;
		case LINESTRING:
			addLineStringMessage(message, (LineString) geometry);
			break;
		case POLYGON:
			addPolygonMessage(message, (Polygon) geometry);
			break;
		case MULTIPOINT:
			addMultiPointMessage(message, (MultiPoint) geometry);
			break;
		case MULTILINESTRING:
			addMultiLineStringMessage(message, (MultiLineString) geometry);
			break;
		case MULTIPOLYGON:
			addMultiPolygonMessage(message, (MultiPolygon) geometry);
			break;
		case CIRCULARSTRING:
			addLineStringMessage(message, (CircularString) geometry);
			break;
		case COMPOUNDCURVE:
			addCompoundCurveMessage(message, (CompoundCurve) geometry);
			break;
		case CURVEPOLYGON:
			@SuppressWarnings("unchecked")
			CurvePolygon<Curve> curvePolygon = (CurvePolygon<Curve>) geometry;
			addCurvePolygonMessage(message, curvePolygon);
			break;
		case POLYHEDRALSURFACE:
			addPolyhedralSurfaceMessage(message, (PolyhedralSurface) geometry);
			break;
		case TIN:
			addPolyhedralSurfaceMessage(message, (TIN) geometry);
			break;
		case TRIANGLE:
			addPolygonMessage(message, (Triangle) geometry);
			break;
		case GEOMETRYCOLLECTION:
		case MULTICURVE:
		case MULTISURFACE:
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			message.append("Geometries: " + geomCollection.numGeometries());
			List<Geometry> geometries = geomCollection.getGeometries();
			for (int i = 0; i < geometries.size(); i++) {
				Geometry subGeometry = geometries.get(i);
				message.append("\n\n");
				message.append(Geometry.class.getSimpleName() + " " + (i + 1));
				message.append("\n");
				message.append(subGeometry.getGeometryType().getName());
				message.append("\n");
				message.append(getGeometryString(subGeometry));
			}
			break;
		default:
		}

		return message.toString();
	}

	/**
	 * Add Point message
	 * 
	 * @param message
	 *            string message
	 * @param point
	 *            point
	 */
	private static void addPointMessage(StringBuilder message, Point point) {
		message.append("Latitude: ").append(point.getY());
		message.append("\nLongitude: ").append(point.getX());
	}

	/**
	 * Add MultiPoint message
	 * 
	 * @param message
	 *            string message
	 * @param multiPoint
	 *            multi point
	 */
	private static void addMultiPointMessage(StringBuilder message,
			MultiPoint multiPoint) {
		message.append(Point.class.getSimpleName() + "s: "
				+ multiPoint.numPoints());
		List<Point> points = multiPoint.getPoints();
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			message.append("\n\n");
			message.append(Point.class.getSimpleName() + " " + (i + 1));
			message.append("\n");
			addPointMessage(message, point);
		}
	}

	/**
	 * Add LineString message
	 * 
	 * @param message
	 *            string message
	 * @param lineString
	 *            line string
	 */
	private static void addLineStringMessage(StringBuilder message,
			LineString lineString) {
		message.append(Point.class.getSimpleName() + "s: "
				+ lineString.numPoints());
		for (Point point : lineString.getPoints()) {
			message.append("\n\n");
			addPointMessage(message, point);
		}
	}

	/**
	 * Add MultiLineString message
	 * 
	 * @param message
	 *            string message
	 * @param multiLineString
	 *            multi line string
	 */
	private static void addMultiLineStringMessage(StringBuilder message,
			MultiLineString multiLineString) {
		message.append(LineString.class.getSimpleName() + "s: "
				+ multiLineString.numLineStrings());
		List<LineString> lineStrings = multiLineString.getLineStrings();
		for (int i = 0; i < lineStrings.size(); i++) {
			LineString lineString = lineStrings.get(i);
			message.append("\n\n");
			message.append(LineString.class.getSimpleName() + " " + (i + 1));
			message.append("\n");
			addLineStringMessage(message, lineString);
		}
	}

	/**
	 * Add Polygon message
	 * 
	 * @param message
	 *            string message
	 * @param polygon
	 *            polygon
	 */
	private static void addPolygonMessage(StringBuilder message, Polygon polygon) {
		message.append("Rings: " + polygon.numRings());
		List<LineString> rings = polygon.getRings();
		for (int i = 0; i < rings.size(); i++) {
			LineString ring = rings.get(i);
			message.append("\n\n");
			if (i > 0) {
				message.append("Hole " + i);
				message.append("\n");
			}
			addLineStringMessage(message, ring);
		}
	}

	/**
	 * Add MultiPolygon message
	 * 
	 * @param message
	 *            string message
	 * @param multiPolygon
	 *            multi polygon
	 */
	private static void addMultiPolygonMessage(StringBuilder message,
			MultiPolygon multiPolygon) {
		message.append(Polygon.class.getSimpleName() + "s: "
				+ multiPolygon.numPolygons());
		List<Polygon> polygons = multiPolygon.getPolygons();
		for (int i = 0; i < polygons.size(); i++) {
			Polygon polygon = polygons.get(i);
			message.append("\n\n");
			message.append(Polygon.class.getSimpleName() + " " + (i + 1));
			message.append("\n");
			addPolygonMessage(message, polygon);
		}
	}

	/**
	 * Add CompoundCurve message
	 * 
	 * @param message
	 *            string message
	 * @param compoundCurve
	 *            compound curve
	 */
	private static void addCompoundCurveMessage(StringBuilder message,
			CompoundCurve compoundCurve) {
		message.append(LineString.class.getSimpleName() + "s: "
				+ compoundCurve.numLineStrings());
		List<LineString> lineStrings = compoundCurve.getLineStrings();
		for (int i = 0; i < lineStrings.size(); i++) {
			LineString lineString = lineStrings.get(i);
			message.append("\n\n");
			message.append(LineString.class.getSimpleName() + " " + (i + 1));
			message.append("\n");
			addLineStringMessage(message, lineString);
		}
	}

	/**
	 * Add CurvePolygon message
	 * 
	 * @param message
	 *            string message
	 * @param curvePolygon
	 *            curve polygon
	 */
	private static void addCurvePolygonMessage(StringBuilder message,
			CurvePolygon<Curve> curvePolygon) {
		message.append("Rings: " + curvePolygon.numRings());
		List<Curve> rings = curvePolygon.getRings();
		for (int i = 0; i < rings.size(); i++) {
			Curve ring = rings.get(i);
			message.append("\n\n");
			if (i > 0) {
				message.append("Hole " + i);
				message.append("\n");
			}
			message.append(getGeometryString(ring));
		}
	}

	/**
	 * Add PolyhedralSurface message
	 * 
	 * @param message
	 *            string message
	 * @param polyhedralSurface
	 *            polyhedral surface
	 */
	private static void addPolyhedralSurfaceMessage(StringBuilder message,
			PolyhedralSurface polyhedralSurface) {
		message.append(Polygon.class.getSimpleName() + "s: "
				+ polyhedralSurface.numPolygons());
		List<Polygon> polygons = polyhedralSurface.getPolygons();
		for (int i = 0; i < polygons.size(); i++) {
			Polygon polygon = polygons.get(i);
			message.append("\n\n");
			message.append(Polygon.class.getSimpleName() + " " + (i + 1));
			message.append("\n");
			addPolygonMessage(message, polygon);
		}
	}

}
