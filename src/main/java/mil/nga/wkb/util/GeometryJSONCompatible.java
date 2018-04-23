package mil.nga.wkb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * JSON compatible object representation of a Geometry
 * 
 * @author osbornb
 * @since 1.0.2
 */
public class GeometryJSONCompatible {

	/**
	 * Get a Geometry object that is JSON compatible
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry JSON object
	 */
	public static Object getJSONCompatibleGeometry(Geometry geometry) {

		Map<String, Object> jsonObject = new HashMap<>();

		Object geometryObject = getJSONCompatibleGeometryObject(geometry);

		if (geometryObject != null) {
			jsonObject
					.put(geometry.getGeometryType().getName(), geometryObject);
		}

		return jsonObject;
	}

	/**
	 * Get a Geometry object that is JSON compatible
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry JSON object
	 */
	private static Object getJSONCompatibleGeometryObject(Geometry geometry) {

		Object geometryObject = null;

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POINT:
			geometryObject = getPoint((Point) geometry);
			break;
		case LINESTRING:
			geometryObject = getLineString((LineString) geometry);
			break;
		case POLYGON:
			geometryObject = getPolygon((Polygon) geometry);
			break;
		case MULTIPOINT:
			geometryObject = getMultiPoint((MultiPoint) geometry);
			break;
		case MULTILINESTRING:
			geometryObject = getMultiLineString((MultiLineString) geometry);
			break;
		case MULTIPOLYGON:
			geometryObject = getMultiPolygon((MultiPolygon) geometry);
			break;
		case CIRCULARSTRING:
			geometryObject = getLineString((CircularString) geometry);
			break;
		case COMPOUNDCURVE:
			geometryObject = getCompoundCurve((CompoundCurve) geometry);
			break;
		case CURVEPOLYGON:
			@SuppressWarnings("unchecked")
			CurvePolygon<Curve> curvePolygon = (CurvePolygon<Curve>) geometry;
			geometryObject = getCurvePolygon(curvePolygon);
			break;
		case POLYHEDRALSURFACE:
			geometryObject = getPolyhedralSurface((PolyhedralSurface) geometry);
			break;
		case TIN:
			geometryObject = getPolyhedralSurface((TIN) geometry);
			break;
		case TRIANGLE:
			geometryObject = getPolygon((Triangle) geometry);
			break;
		case GEOMETRYCOLLECTION:
		case MULTICURVE:
		case MULTISURFACE:
			List<Object> jsonGeoCollectionObject = new ArrayList<>();
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			List<Geometry> geometries = geomCollection.getGeometries();
			for (int i = 0; i < geometries.size(); i++) {
				Geometry subGeometry = geometries.get(i);
				jsonGeoCollectionObject
						.add(getJSONCompatibleGeometry(subGeometry));
			}
			geometryObject = jsonGeoCollectionObject;
			break;
		default:
		}

		return geometryObject;
	}

	/**
	 * Get Point object
	 * 
	 * @param point
	 *            point
	 * @return point object
	 */
	private static Object getPoint(Point point) {
		Map<String, Double> jsonObject = new HashMap<>();
		jsonObject.put("x", point.getX());
		jsonObject.put("y", point.getY());
		if (point.hasZ()) {
			jsonObject.put("z", point.getZ());
		}
		if (point.hasM()) {
			jsonObject.put("m", point.getM());
		}
		return jsonObject;
	}

	/**
	 * Get MultiPoint object
	 * 
	 * @param multiPoint
	 *            multi point
	 * @return multi point object
	 */
	private static Object getMultiPoint(MultiPoint multiPoint) {
		List<Object> jsonObject = new ArrayList<>();
		List<Point> points = multiPoint.getPoints();
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			jsonObject.add(getPoint(point));
		}
		return jsonObject;
	}

	/**
	 * Get LineString object
	 * 
	 * @param lineString
	 *            line string
	 * @return line string object
	 */
	private static Object getLineString(LineString lineString) {
		List<Object> jsonObject = new ArrayList<>();
		for (Point point : lineString.getPoints()) {
			jsonObject.add(getPoint(point));
		}
		return jsonObject;
	}

	/**
	 * Get MultiLineString object
	 * 
	 * @param multiLineString
	 *            multi line string
	 * @return multi line string object
	 */
	private static Object getMultiLineString(MultiLineString multiLineString) {
		List<Object> jsonObject = new ArrayList<>();
		List<LineString> lineStrings = multiLineString.getLineStrings();
		for (int i = 0; i < lineStrings.size(); i++) {
			LineString lineString = lineStrings.get(i);
			jsonObject.add(getLineString(lineString));
		}
		return jsonObject;
	}

	/**
	 * Get Polygon object
	 * 
	 * @param polygon
	 *            polygon
	 * @return polygon object
	 */
	private static Object getPolygon(Polygon polygon) {
		List<Object> jsonObject = new ArrayList<>();
		List<LineString> rings = polygon.getRings();
		for (int i = 0; i < rings.size(); i++) {
			LineString ring = rings.get(i);
			jsonObject.add(getLineString(ring));
		}
		return jsonObject;
	}

	/**
	 * Get MultiPolygon object
	 * 
	 * @param multiPolygon
	 *            multi polygon
	 * @return multi polygon object
	 */
	private static Object getMultiPolygon(MultiPolygon multiPolygon) {
		List<Object> jsonObject = new ArrayList<>();
		List<Polygon> polygons = multiPolygon.getPolygons();
		for (int i = 0; i < polygons.size(); i++) {
			Polygon polygon = polygons.get(i);
			jsonObject.add(getPolygon(polygon));
		}
		return jsonObject;
	}

	/**
	 * Get CompoundCurve object
	 * 
	 * @param compoundCurve
	 *            compound curve
	 * @return compound curve object
	 */
	private static Object getCompoundCurve(CompoundCurve compoundCurve) {
		List<Object> jsonObject = new ArrayList<>();
		List<LineString> lineStrings = compoundCurve.getLineStrings();
		for (int i = 0; i < lineStrings.size(); i++) {
			LineString lineString = lineStrings.get(i);
			jsonObject.add(getLineString(lineString));
		}
		return jsonObject;
	}

	/**
	 * Get CurvePolygon object
	 * 
	 * @param curvePolygon
	 *            curve polygon
	 * @return curve polygon object
	 */
	private static Object getCurvePolygon(CurvePolygon<Curve> curvePolygon) {
		List<Object> jsonObject = new ArrayList<>();
		List<Curve> rings = curvePolygon.getRings();
		for (int i = 0; i < rings.size(); i++) {
			Curve ring = rings.get(i);
			jsonObject.add(getJSONCompatibleGeometryObject(ring));
		}
		return jsonObject;
	}

	/**
	 * Get PolyhedralSurface object
	 * 
	 * @param polyhedralSurface
	 *            polyhedral surface
	 * @return polyhedral surface object
	 */
	private static Object getPolyhedralSurface(
			PolyhedralSurface polyhedralSurface) {
		List<Object> jsonObject = new ArrayList<>();
		List<Polygon> polygons = polyhedralSurface.getPolygons();
		for (int i = 0; i < polygons.size(); i++) {
			Polygon polygon = polygons.get(i);
			jsonObject.add(getPolygon(polygon));
		}
		return jsonObject;
	}

}
