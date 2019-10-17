package mil.nga.sf.wkb;

import java.io.IOException;
import java.nio.ByteOrder;

import mil.nga.sf.CircularString;
import mil.nga.sf.CompoundCurve;
import mil.nga.sf.Curve;
import mil.nga.sf.CurvePolygon;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryCollection;
import mil.nga.sf.GeometryType;
import mil.nga.sf.LineString;
import mil.nga.sf.MultiLineString;
import mil.nga.sf.MultiPoint;
import mil.nga.sf.MultiPolygon;
import mil.nga.sf.Point;
import mil.nga.sf.Polygon;
import mil.nga.sf.PolyhedralSurface;
import mil.nga.sf.TIN;
import mil.nga.sf.Triangle;
import mil.nga.sf.util.ByteWriter;
import mil.nga.sf.util.SFException;

/**
 * Well Known Binary writer
 * 
 * @author osbornb
 */
public class GeometryWriter {

	/**
	 * Write a geometry to the byte writer
	 * 
	 * @param writer
	 *            byte writer
	 * @param geometry
	 *            geometry
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeGeometry(ByteWriter writer, Geometry geometry)
			throws IOException {

		// Write the single byte order byte
		byte byteOrder = writer.getByteOrder() == ByteOrder.BIG_ENDIAN
				? (byte) 0
				: (byte) 1;
		writer.writeByte(byteOrder);

		// Write the geometry type integer
		writer.writeInt(GeometryCodes.getCode(geometry));

		GeometryType geometryType = geometry.getGeometryType();

		switch (geometryType) {

		case GEOMETRY:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case POINT:
			writePoint(writer, (Point) geometry);
			break;
		case LINESTRING:
			writeLineString(writer, (LineString) geometry);
			break;
		case POLYGON:
			writePolygon(writer, (Polygon) geometry);
			break;
		case MULTIPOINT:
			writeMultiPoint(writer, (MultiPoint) geometry);
			break;
		case MULTILINESTRING:
			writeMultiLineString(writer, (MultiLineString) geometry);
			break;
		case MULTIPOLYGON:
			writeMultiPolygon(writer, (MultiPolygon) geometry);
			break;
		case GEOMETRYCOLLECTION:
		case MULTICURVE:
		case MULTISURFACE:
			writeGeometryCollection(writer, (GeometryCollection<?>) geometry);
			break;
		case CIRCULARSTRING:
			writeCircularString(writer, (CircularString) geometry);
			break;
		case COMPOUNDCURVE:
			writeCompoundCurve(writer, (CompoundCurve) geometry);
			break;
		case CURVEPOLYGON:
			writeCurvePolygon(writer, (CurvePolygon<?>) geometry);
			break;
		case CURVE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case SURFACE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case POLYHEDRALSURFACE:
			writePolyhedralSurface(writer, (PolyhedralSurface) geometry);
			break;
		case TIN:
			writeTIN(writer, (TIN) geometry);
			break;
		case TRIANGLE:
			writeTriangle(writer, (Triangle) geometry);
			break;
		default:
			throw new SFException(
					"Geometry Type not supported: " + geometryType);
		}

	}

	/**
	 * Write a Point
	 * 
	 * @param writer
	 *            byte writer
	 * @param point
	 *            point
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writePoint(ByteWriter writer, Point point)
			throws IOException {

		writer.writeDouble(point.getX());
		writer.writeDouble(point.getY());

		if (point.hasZ()) {
			writer.writeDouble(point.getZ());
		}

		if (point.hasM()) {
			writer.writeDouble(point.getM());
		}
	}

	/**
	 * Write a Line String
	 * 
	 * @param writer
	 *            byte writer
	 * @param lineString
	 *            Line String
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeLineString(ByteWriter writer, LineString lineString)
			throws IOException {

		writer.writeInt(lineString.numPoints());

		for (Point point : lineString.getPoints()) {
			writePoint(writer, point);
		}
	}

	/**
	 * Write a Polygon
	 * 
	 * @param writer
	 *            byte writer
	 * @param polygon
	 *            Polygon
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writePolygon(ByteWriter writer, Polygon polygon)
			throws IOException {

		writer.writeInt(polygon.numRings());

		for (LineString ring : polygon.getRings()) {
			writeLineString(writer, ring);
		}
	}

	/**
	 * Write a Multi Point
	 * 
	 * @param writer
	 *            byte writer
	 * @param multiPoint
	 *            Multi Point
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeMultiPoint(ByteWriter writer, MultiPoint multiPoint)
			throws IOException {

		writer.writeInt(multiPoint.numPoints());

		for (Point point : multiPoint.getPoints()) {
			writeGeometry(writer, point);
		}
	}

	/**
	 * Write a Multi Line String
	 * 
	 * @param writer
	 *            byte writer
	 * @param multiLineString
	 *            Multi Line String
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeMultiLineString(ByteWriter writer,
			MultiLineString multiLineString) throws IOException {

		writer.writeInt(multiLineString.numLineStrings());

		for (LineString lineString : multiLineString.getLineStrings()) {
			writeGeometry(writer, lineString);
		}
	}

	/**
	 * Write a Multi Polygon
	 * 
	 * @param writer
	 *            byte writer
	 * @param multiPolygon
	 *            Multi Polygon
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeMultiPolygon(ByteWriter writer,
			MultiPolygon multiPolygon) throws IOException {

		writer.writeInt(multiPolygon.numPolygons());

		for (Polygon polygon : multiPolygon.getPolygons()) {
			writeGeometry(writer, polygon);
		}
	}

	/**
	 * Write a Geometry Collection
	 * 
	 * @param writer
	 *            byte writer
	 * @param geometryCollection
	 *            Geometry Collection
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeGeometryCollection(ByteWriter writer,
			GeometryCollection<?> geometryCollection) throws IOException {

		writer.writeInt(geometryCollection.numGeometries());

		for (Geometry geometry : geometryCollection.getGeometries()) {
			writeGeometry(writer, geometry);
		}
	}

	/**
	 * Write a Circular String
	 * 
	 * @param writer
	 *            byte writer
	 * @param circularString
	 *            Circular String
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeCircularString(ByteWriter writer,
			CircularString circularString) throws IOException {

		writer.writeInt(circularString.numPoints());

		for (Point point : circularString.getPoints()) {
			writePoint(writer, point);
		}
	}

	/**
	 * Write a Compound Curve
	 * 
	 * @param writer
	 *            byte writer
	 * @param compoundCurve
	 *            Compound Curve
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeCompoundCurve(ByteWriter writer,
			CompoundCurve compoundCurve) throws IOException {

		writer.writeInt(compoundCurve.numLineStrings());

		for (LineString lineString : compoundCurve.getLineStrings()) {
			writeGeometry(writer, lineString);
		}
	}

	/**
	 * Write a Curve Polygon
	 * 
	 * @param writer
	 *            byte writer
	 * @param curvePolygon
	 *            Curve Polygon
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeCurvePolygon(ByteWriter writer,
			CurvePolygon<?> curvePolygon) throws IOException {

		writer.writeInt(curvePolygon.numRings());

		for (Curve ring : curvePolygon.getRings()) {
			writeGeometry(writer, ring);
		}
	}

	/**
	 * Write a Polyhedral Surface
	 * 
	 * @param writer
	 *            byte writer
	 * @param polyhedralSurface
	 *            Polyhedral Surface
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writePolyhedralSurface(ByteWriter writer,
			PolyhedralSurface polyhedralSurface) throws IOException {

		writer.writeInt(polyhedralSurface.numPolygons());

		for (Polygon polygon : polyhedralSurface.getPolygons()) {
			writeGeometry(writer, polygon);
		}
	}

	/**
	 * Write a TIN
	 * 
	 * @param writer
	 *            byte writer
	 * @param tin
	 *            TIN
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeTIN(ByteWriter writer, TIN tin) throws IOException {

		writer.writeInt(tin.numPolygons());

		for (Polygon polygon : tin.getPolygons()) {
			writeGeometry(writer, polygon);
		}
	}

	/**
	 * Write a Triangle
	 * 
	 * @param writer
	 *            byte writer
	 * @param triangle
	 *            Triangle
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeTriangle(ByteWriter writer, Triangle triangle)
			throws IOException {

		writer.writeInt(triangle.numRings());

		for (LineString ring : triangle.getRings()) {
			writeLineString(writer, ring);
		}
	}

}
