package mil.nga.wkb.util;

import java.util.List;

import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
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
		return new GeometryEnvelopeBuilder().build(geometry);
	}

	/**
	 * Expand Geometry Envelope
	 * 
	 * @param geometry
	 *            geometry to build envelope from
	 * @param envelope
	 *            envelope to expand
	 */
	public static void buildEnvelope(Geometry geometry,
			GeometryEnvelope envelope) {
		new GeometryEnvelopeBuilder().build(geometry, envelope);
	}

	/**
	 * Build Geometry Envelope, expanded by wrapping values using the world
	 * width producing an envelope longitude range >= (min value - world width)
	 * and <= (max value + world width).
	 * 
	 * Example: For WGS84 with longitude values >= -180.0 and <= 180.0, provide
	 * a world width of 360.0 resulting in an envelope with longitude range >=
	 * -540.0 and <= 540.0.
	 * 
	 * Example: For web mercator with longitude values >= -20037508.342789244
	 * and <= 20037508.342789244, provide a world width of 40075016.685578488
	 * resulting in an envelope with longitude range >= -60112525.028367732 and
	 * <= 60112525.028367732.
	 * 
	 * @param geometry
	 *            geometry to build envelope from
	 * @param worldWidth
	 *            world longitude width in geometry projection
	 * @return geometry envelope
	 * @since 1.0.3
	 */
	public static GeometryEnvelope buildEnvelope(Geometry geometry,
			double worldWidth) {
		return new GeometryEnvelopeBuilder(worldWidth).build(geometry);
	}

	/**
	 * Expand Geometry Envelope, expanded by wrapping values using the world
	 * width producing an envelope longitude range >= (min value - world width)
	 * and <= (max value + world width).
	 * 
	 * Example: For WGS84 with longitude values >= -180.0 and <= 180.0, provide
	 * a world width of 360.0 resulting in an envelope with longitude range >=
	 * -540.0 and <= 540.0.
	 * 
	 * Example: For web mercator with longitude values >= -20037508.342789244
	 * and <= 20037508.342789244, provide a world width of 40075016.685578488
	 * resulting in an envelope with longitude range >= -60112525.028367732 and
	 * <= 60112525.028367732.
	 * 
	 * @param geometry
	 *            geometry to build envelope from
	 * @param envelope
	 *            envelope to expand
	 * @param worldWidth
	 *            world longitude width in geometry projection
	 * @since 1.0.3
	 */
	public static void buildEnvelope(Geometry geometry,
			GeometryEnvelope envelope, double worldWidth) {
		new GeometryEnvelopeBuilder(worldWidth).build(geometry, envelope);
	}

	/**
	 * World longitude width in geometry projection
	 */
	private final Double worldWidth;

	/**
	 * Constructor
	 * 
	 * @since 1.0.3
	 */
	public GeometryEnvelopeBuilder() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * If a non null world width is provided, expansions are done by wrapping
	 * values producing an envelope longitude range >= (min value - world width)
	 * and <= (max value + world width).
	 * 
	 * Example: For WGS84 with longitude values >= -180.0 and <= 180.0, provide
	 * a world width of 360.0 resulting in an envelope with longitude range >=
	 * -540.0 and <= 540.0.
	 * 
	 * Example: For web mercator with longitude values >= -20037508.342789244
	 * and <= 20037508.342789244, provide a world width of 40075016.685578488
	 * resulting in an envelope with longitude range >= -60112525.028367732 and
	 * <= 60112525.028367732.
	 * 
	 * @param worldWidth
	 *            null or world longitude width in geometry projection
	 * @since 1.0.3
	 */
	public GeometryEnvelopeBuilder(Double worldWidth) {
		this.worldWidth = worldWidth;
	}

	/**
	 * Get the world width
	 * 
	 * @return null or world longitude width in geometry projection
	 * @since 1.0.3
	 */
	public Double getWorldWidth() {
		return worldWidth;
	}

	/**
	 * Build Geometry Envelope
	 * 
	 * @param geometry
	 * @return geometry envelope
	 * @since 1.0.3
	 */
	public GeometryEnvelope build(Geometry geometry) {

		GeometryEnvelope envelope = new GeometryEnvelope();

		envelope.setMinX(Double.MAX_VALUE);
		envelope.setMaxX(-Double.MAX_VALUE);
		envelope.setMinY(Double.MAX_VALUE);
		envelope.setMaxY(-Double.MAX_VALUE);

		build(geometry, envelope);

		return envelope;
	}

	/**
	 * Expand Geometry Envelope
	 * 
	 * @param geometry
	 * @param envelope
	 * @since 1.0.3
	 */
	public void build(Geometry geometry, GeometryEnvelope envelope) {

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
				build(subGeometry, envelope);
			}
			break;
		default:
		}

	}

	/**
	 * Update the has z and m values
	 * 
	 * @param envelope
	 * @param geometry
	 */
	private void updateHasZandM(GeometryEnvelope envelope, Geometry geometry) {
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
	 * @param point
	 */
	private void addPoint(GeometryEnvelope envelope, Point point) {

		updateHasZandM(envelope, point);

		double x = point.getX();
		double y = point.getY();

		if (worldWidth != null && envelope.getMinX() != Double.MAX_VALUE
				&& envelope.getMaxX() != Double.MIN_VALUE) {
			if (x < envelope.getMinX()) {
				if (envelope.getMinX() - x > (x + worldWidth)
						- envelope.getMaxX()) {
					x += worldWidth;
				}
			} else if (x > envelope.getMaxX()) {
				if (x - envelope.getMaxX() > envelope.getMinX()
						- (x - worldWidth)) {
					x -= worldWidth;
				}
			}
		}

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
	 * @param multiPoint
	 */
	private void addMultiPoint(GeometryEnvelope envelope, MultiPoint multiPoint) {

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
	 * @param lineString
	 */
	private void addLineString(GeometryEnvelope envelope, LineString lineString) {

		updateHasZandM(envelope, lineString);

		for (Point point : lineString.getPoints()) {
			addPoint(envelope, point);
		}
	}

	/**
	 * Add MultiLineString
	 * 
	 * @param envelope
	 * @param multiLineString
	 */
	private void addMultiLineString(GeometryEnvelope envelope,
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
	 * @param polygon
	 */
	private void addPolygon(GeometryEnvelope envelope, Polygon polygon) {

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
	 * @param multiPolygon
	 */
	private void addMultiPolygon(GeometryEnvelope envelope,
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
	 * @param compoundCurve
	 */
	private void addCompoundCurve(GeometryEnvelope envelope,
			CompoundCurve compoundCurve) {

		updateHasZandM(envelope, compoundCurve);

		List<LineString> lineStrings = compoundCurve.getLineStrings();
		for (LineString lineString : lineStrings) {
			addLineString(envelope, lineString);
		}
	}

	/**
	 * Add PolyhedralSurface
	 * 
	 * @param envelope
	 * @param polyhedralSurface
	 */
	private void addPolyhedralSurface(GeometryEnvelope envelope,
			PolyhedralSurface polyhedralSurface) {

		updateHasZandM(envelope, polyhedralSurface);

		List<Polygon> polygons = polyhedralSurface.getPolygons();
		for (Polygon polygon : polygons) {
			addPolygon(envelope, polygon);
		}
	}

}
