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
	 * Write a geometry to a well-known bytes
	 * 
	 * @param geometry
	 *            geometry
	 * @return well-known bytes
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.3
	 */
	public static byte[] writeGeometry(Geometry geometry) throws IOException {
		return writeGeometry(geometry, ByteWriter.DEFAULT_BYTE_ORDER);
	}

	/**
	 * Write a geometry to a well-known bytes
	 * 
	 * @param geometry
	 *            geometry
	 * @param byteOrder
	 *            byte order
	 * @return well-known bytes
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.3
	 */
	public static byte[] writeGeometry(Geometry geometry, ByteOrder byteOrder)
			throws IOException {
		byte[] bytes = null;
		GeometryWriter writer = new GeometryWriter(byteOrder);
		try {
			writer.write(geometry);
			bytes = writer.getBytes();
		} finally {
			writer.close();
		}
		return bytes;
	}

	/**
	 * Byte Writer
	 */
	private ByteWriter writer;

	/**
	 * Constructor
	 * 
	 * @since 2.0.4
	 */
	public GeometryWriter() {
		this(ByteWriter.DEFAULT_BYTE_ORDER);
	}

	/**
	 * Constructor
	 * 
	 * @param byteOrder
	 *            byte order
	 * @since 2.0.4
	 */
	public GeometryWriter(ByteOrder byteOrder) {
		this(new ByteWriter(byteOrder));
	}

	/**
	 * Constructor
	 * 
	 * @param writer
	 *            byte writer
	 * @since 2.0.4
	 */
	public GeometryWriter(ByteWriter writer) {
		this.writer = writer;
	}

	/**
	 * Get the byte writer
	 * 
	 * @return byte writer
	 * @since 2.0.4
	 */
	public ByteWriter getByteWriter() {
		return writer;
	}

	/**
	 * Get the written bytes
	 * 
	 * @return written bytes
	 * @since 2.0.4
	 */
	public byte[] getBytes() {
		return writer.getBytes();
	}

	/**
	 * Close the byte writer
	 * 
	 * @since 2.0.4
	 */
	public void close() {
		writer.close();
	}

	/**
	 * Write a geometry to the byte writer
	 * 
	 * @param geometry
	 *            geometry
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void write(Geometry geometry) throws IOException {

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
			writePoint((Point) geometry);
			break;
		case LINESTRING:
			writeLineString((LineString) geometry);
			break;
		case POLYGON:
			writePolygon((Polygon) geometry);
			break;
		case MULTIPOINT:
			writeMultiPoint((MultiPoint) geometry);
			break;
		case MULTILINESTRING:
			writeMultiLineString((MultiLineString) geometry);
			break;
		case MULTIPOLYGON:
			writeMultiPolygon((MultiPolygon) geometry);
			break;
		case GEOMETRYCOLLECTION:
		case MULTICURVE:
		case MULTISURFACE:
			writeGeometryCollection((GeometryCollection<?>) geometry);
			break;
		case CIRCULARSTRING:
			writeCircularString((CircularString) geometry);
			break;
		case COMPOUNDCURVE:
			writeCompoundCurve((CompoundCurve) geometry);
			break;
		case CURVEPOLYGON:
			writeCurvePolygon((CurvePolygon<?>) geometry);
			break;
		case CURVE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case SURFACE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case POLYHEDRALSURFACE:
			writePolyhedralSurface((PolyhedralSurface) geometry);
			break;
		case TIN:
			writeTIN((TIN) geometry);
			break;
		case TRIANGLE:
			writeTriangle((Triangle) geometry);
			break;
		default:
			throw new SFException(
					"Geometry Type not supported: " + geometryType);
		}

	}

	/**
	 * Write a Point
	 * 
	 * @param point
	 *            point
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writePoint(Point point) throws IOException {
		writeXY(point);
		writeZ(point);
		writeM(point);
	}

	/**
	 * Write a Point X and Y value
	 * 
	 * @param point
	 *            point
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeXY(Point point) throws IOException {
		writer.writeDouble(point.getX());
		writer.writeDouble(point.getY());
	}

	/**
	 * Write a Point Z value
	 * 
	 * @param point
	 *            point
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeZ(Point point) throws IOException {
		if (point.hasZ()) {
			writer.writeDouble(point.getZ());
		}
	}

	/**
	 * Write a Point M value
	 * 
	 * @param point
	 *            point
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeM(Point point) throws IOException {
		if (point.hasM()) {
			writer.writeDouble(point.getM());
		}
	}

	/**
	 * Write a Line String
	 * 
	 * @param lineString
	 *            Line String
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeLineString(LineString lineString) throws IOException {

		writer.writeInt(lineString.numPoints());

		for (Point point : lineString.getPoints()) {
			writePoint(point);
		}

	}

	/**
	 * Write a Polygon
	 * 
	 * @param polygon
	 *            Polygon
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writePolygon(Polygon polygon) throws IOException {

		writer.writeInt(polygon.numRings());

		for (LineString ring : polygon.getRings()) {
			writeLineString(ring);
		}

	}

	/**
	 * Write a Multi Point
	 * 
	 * @param multiPoint
	 *            Multi Point
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeMultiPoint(MultiPoint multiPoint) throws IOException {

		writer.writeInt(multiPoint.numPoints());

		for (Point point : multiPoint.getPoints()) {
			write(point);
		}

	}

	/**
	 * Write a Multi Line String
	 * 
	 * @param multiLineString
	 *            Multi Line String
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeMultiLineString(MultiLineString multiLineString)
			throws IOException {

		writer.writeInt(multiLineString.numLineStrings());

		for (LineString lineString : multiLineString.getLineStrings()) {
			write(lineString);
		}

	}

	/**
	 * Write a Multi Polygon
	 * 
	 * @param multiPolygon
	 *            Multi Polygon
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeMultiPolygon(MultiPolygon multiPolygon)
			throws IOException {

		writer.writeInt(multiPolygon.numPolygons());

		for (Polygon polygon : multiPolygon.getPolygons()) {
			write(polygon);
		}

	}

	/**
	 * Write a Geometry Collection
	 * 
	 * @param geometryCollection
	 *            Geometry Collection
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeGeometryCollection(
			GeometryCollection<?> geometryCollection) throws IOException {

		writer.writeInt(geometryCollection.numGeometries());

		for (Geometry geometry : geometryCollection.getGeometries()) {
			write(geometry);
		}

	}

	/**
	 * Write a Circular String
	 * 
	 * @param circularString
	 *            Circular String
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeCircularString(CircularString circularString)
			throws IOException {

		writer.writeInt(circularString.numPoints());

		for (Point point : circularString.getPoints()) {
			writePoint(point);
		}

	}

	/**
	 * Write a Compound Curve
	 * 
	 * @param compoundCurve
	 *            Compound Curve
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeCompoundCurve(CompoundCurve compoundCurve)
			throws IOException {

		writer.writeInt(compoundCurve.numLineStrings());

		for (LineString lineString : compoundCurve.getLineStrings()) {
			write(lineString);
		}

	}

	/**
	 * Write a Curve Polygon
	 * 
	 * @param curvePolygon
	 *            Curve Polygon
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeCurvePolygon(CurvePolygon<?> curvePolygon)
			throws IOException {

		writer.writeInt(curvePolygon.numRings());

		for (Curve ring : curvePolygon.getRings()) {
			write(ring);
		}

	}

	/**
	 * Write a Polyhedral Surface
	 * 
	 * @param polyhedralSurface
	 *            Polyhedral Surface
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writePolyhedralSurface(PolyhedralSurface polyhedralSurface)
			throws IOException {

		writer.writeInt(polyhedralSurface.numPolygons());

		for (Polygon polygon : polyhedralSurface.getPolygons()) {
			write(polygon);
		}

	}

	/**
	 * Write a TIN
	 * 
	 * @param tin
	 *            TIN
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeTIN(TIN tin) throws IOException {

		writer.writeInt(tin.numPolygons());

		for (Polygon polygon : tin.getPolygons()) {
			write(polygon);
		}

	}

	/**
	 * Write a Triangle
	 * 
	 * @param triangle
	 *            Triangle
	 * @throws IOException
	 *             upon failure to write
	 * @since 2.0.4
	 */
	public void writeTriangle(Triangle triangle) throws IOException {

		writer.writeInt(triangle.numRings());

		for (LineString ring : triangle.getRings()) {
			writeLineString(ring);
		}

	}

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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.write(geometry);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writePoint(point);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeLineString(lineString);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writePolygon(polygon);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeMultiPoint(multiPoint);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeMultiLineString(multiLineString);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeMultiPolygon(multiPolygon);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeGeometryCollection(geometryCollection);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeCircularString(circularString);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeCompoundCurve(compoundCurve);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeCurvePolygon(curvePolygon);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writePolyhedralSurface(polyhedralSurface);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeTIN(tin);
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
		GeometryWriter geometryWriter = new GeometryWriter(writer);
		geometryWriter.writeTriangle(triangle);
	}

}
