package mil.nga.wkb.util;

import java.util.List;

import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.CurvePolygon;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryEnvelope;
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
 * Builds an envelope from a Geometry
 * 
 * @author osbornb
 */
public class GeometryEnvelopeBuilder {

	/**
	 * Build Geometry Envelope
	 * 
	 * @param geometry
	 *            geometry to build envelope from
	 * @return geometry envelope
	 */
	public static GeometryEnvelope buildEnvelope(Geometry geometry) {

		GeometryEnvelope envelope = new GeometryEnvelope();

		envelope.setMinX(Double.MAX_VALUE);
		envelope.setMaxX(-Double.MAX_VALUE);
		envelope.setMinY(Double.MAX_VALUE);
		envelope.setMaxY(-Double.MAX_VALUE);

		buildEnvelope(geometry, envelope);

		return envelope;
	}

	/**
	 * Build Geometry Envelope
	 * 
	 * @param geometry
	 *            geometry to build envelope from
	 * @param envelope
	 *            geometry envelope to expand
	 */
	public static void buildEnvelope(Geometry geometry,
			GeometryEnvelope envelope) {

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POINT:
			addPoint(envelope, (Point) geometry);
			break;
		case LINESTRING:
			addLineString(envelope, (LineString) geometry);
			break;
		case POLYGON:
			addPolygon(envelope, (Polygon) geometry);
			break;
		case MULTIPOINT:
			addMultiPoint(envelope, (MultiPoint) geometry);
			break;
		case MULTILINESTRING:
			addMultiLineString(envelope, (MultiLineString) geometry);
			break;
		case MULTIPOLYGON:
			addMultiPolygon(envelope, (MultiPolygon) geometry);
			break;
		case CIRCULARSTRING:
			addLineString(envelope, (CircularString) geometry);
			break;
		case COMPOUNDCURVE:
			addCompoundCurve(envelope, (CompoundCurve) geometry);
			break;
		case CURVEPOLYGON:
			@SuppressWarnings("unchecked")
			CurvePolygon<Curve> curvePolygon = (CurvePolygon<Curve>) geometry;
			addCurvePolygon(envelope, curvePolygon);
			break;
		case POLYHEDRALSURFACE:
			addPolyhedralSurface(envelope, (PolyhedralSurface) geometry);
			break;
		case TIN:
			addPolyhedralSurface(envelope, (TIN) geometry);
			break;
		case TRIANGLE:
			addPolygon(envelope, (Triangle) geometry);
			break;
		case GEOMETRYCOLLECTION:
			updateHasZandM(envelope, geometry);
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> geomCollection = (GeometryCollection<Geometry>) geometry;
			List<Geometry> geometries = geomCollection.getGeometries();
			for (Geometry subGeometry : geometries) {
				buildEnvelope(subGeometry, envelope);
			}
			break;
		default:
		}

	}

	/**
	 * Update the has z and m values
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param geometry
	 *            geometry
	 */
	private static void updateHasZandM(GeometryEnvelope envelope,
			Geometry geometry) {
		if (!envelope.hasZ() && geometry.hasZ()) {
			envelope.setHasZ(true);
		}
		if (!envelope.hasM() && geometry.hasM()) {
			envelope.setHasM(true);
		}
	}

	/**
	 * Add Point
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param point
	 *            point
	 */
	private static void addPoint(GeometryEnvelope envelope, Point point) {

		updateHasZandM(envelope, point);

		double x = point.getX();
		double y = point.getY();
		if (x < envelope.getMinX()) {
			envelope.setMinX(x);
		}
		if (x > envelope.getMaxX()) {
			envelope.setMaxX(x);
		}
		if (y < envelope.getMinY()) {
			envelope.setMinY(y);
		}
		if (y > envelope.getMaxY()) {
			envelope.setMaxY(y);
		}
		if (point.hasZ()) {
			Double z = point.getZ();
			if (z != null) {
				if (envelope.getMinZ() == null || z < envelope.getMinZ()) {
					envelope.setMinZ(z);
				}
				if (envelope.getMaxZ() == null || z > envelope.getMaxZ()) {
					envelope.setMaxZ(z);
				}
			}
		}
		if (point.hasM()) {
			Double m = point.getM();
			if (m != null) {
				if (envelope.getMinM() == null || m < envelope.getMinM()) {
					envelope.setMinM(m);
				}
				if (envelope.getMaxM() == null || m > envelope.getMaxM()) {
					envelope.setMaxM(m);
				}
			}
		}
	}

	/**
	 * Add MultiPoint
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param multiPoint
	 *            multi point
	 */
	private static void addMultiPoint(GeometryEnvelope envelope,
			MultiPoint multiPoint) {

		updateHasZandM(envelope, multiPoint);

		List<Point> points = multiPoint.getPoints();
		for (Point point : points) {
			addPoint(envelope, point);
		}
	}

	/**
	 * Add LineString
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param lineString
	 *            line string
	 */
	private static void addLineString(GeometryEnvelope envelope,
			LineString lineString) {

		updateHasZandM(envelope, lineString);

		for (Point point : lineString.getPoints()) {
			addPoint(envelope, point);
		}
	}

	/**
	 * Add MultiLineString
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param multiLineString
	 *            multi line string
	 */
	private static void addMultiLineString(GeometryEnvelope envelope,
			MultiLineString multiLineString) {

		updateHasZandM(envelope, multiLineString);

		List<LineString> lineStrings = multiLineString.getLineStrings();
		for (LineString lineString : lineStrings) {
			addLineString(envelope, lineString);
		}
	}

	/**
	 * Add Polygon
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param polygon
	 *            polygon
	 */
	private static void addPolygon(GeometryEnvelope envelope, Polygon polygon) {

		updateHasZandM(envelope, polygon);

		List<LineString> rings = polygon.getRings();
		for (LineString ring : rings) {
			addLineString(envelope, ring);
		}
	}

	/**
	 * Add MultiPolygon
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param multiPolygon
	 *            multi polygon
	 */
	private static void addMultiPolygon(GeometryEnvelope envelope,
			MultiPolygon multiPolygon) {

		updateHasZandM(envelope, multiPolygon);

		List<Polygon> polygons = multiPolygon.getPolygons();
		for (Polygon polygon : polygons) {
			addPolygon(envelope, polygon);
		}
	}

	/**
	 * Add CompoundCurve
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param compoundCurve
	 *            compound curve
	 */
	private static void addCompoundCurve(GeometryEnvelope envelope,
			CompoundCurve compoundCurve) {

		updateHasZandM(envelope, compoundCurve);

		List<LineString> lineStrings = compoundCurve.getLineStrings();
		for (LineString lineString : lineStrings) {
			addLineString(envelope, lineString);
		}
	}

	/**
	 * Add CurvePolygon
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param curvePolygon
	 *            curve polygon
	 */
	private static void addCurvePolygon(GeometryEnvelope envelope,
			CurvePolygon<Curve> curvePolygon) {

		updateHasZandM(envelope, curvePolygon);

		List<Curve> rings = curvePolygon.getRings();
		for (Curve ring : rings) {
			buildEnvelope(ring, envelope);
		}
	}

	/**
	 * Add PolyhedralSurface
	 * 
	 * @param envelope
	 *            geometry envelope
	 * @param polyhedralSurface
	 *            polyhedral surface
	 */
	private static void addPolyhedralSurface(GeometryEnvelope envelope,
			PolyhedralSurface polyhedralSurface) {

		updateHasZandM(envelope, polyhedralSurface);

		List<Polygon> polygons = polyhedralSurface.getPolygons();
		for (Polygon polygon : polygons) {
			addPolygon(envelope, polygon);
		}
	}

}