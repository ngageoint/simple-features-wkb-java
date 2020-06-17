package mil.nga.sf.wkt;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import mil.nga.sf.CircularString;
import mil.nga.sf.CompoundCurve;
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
import mil.nga.sf.util.SFException;

/**
 * Well Known Text writer
 * 
 * @author osbornb
 */
public class GeometryWriter {
	
	/**
	 * Write a geometry to a well-known text string
	 * 
	 * @param geometry
	 *            geometry
	 * @return well-known text string
	 * @throws IOException
	 *             upon failure to write
	 */
	public static String writeGeometry(Geometry geometry) throws IOException {
		String text = null;
		StringWriter writer = new StringWriter();
		try {
			writeGeometry(writer, geometry);
			text = writer.toString();
		} finally {
			writer.close();
		}
		return text;
	}

	/**
	 * Write a geometry to well-known text
	 * 
	 * @param writer
	 *            writer
	 * @param geometry
	 *            geometry
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeGeometry(Writer writer, Geometry geometry)
			throws IOException {

		GeometryType geometryType = geometry.getGeometryType();

		// Write the geometry type
		writer.write(geometryType.name());
		writer.write(" ");

		boolean hasZ = geometry.hasZ();
		boolean hasM = geometry.hasM();

		if (hasZ || hasM) {
			if (hasZ) {
				writer.write("Z");
			}
			if (hasM) {
				writer.write("M");
			}
			writer.write(" ");
		}

		switch (geometryType) {

		case GEOMETRY:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case POINT:
			writePointText(writer, (Point) geometry);
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
	 *            writer
	 * @param point
	 *            point
	 * @throws IOException
	 *             upon failure to read
	 */
	public static void writePointText(Writer writer, Point point)
			throws IOException {

		writer.write("(");
		writePoint(writer, point);
		writer.write(")");

	}

	/**
	 * Write a Point
	 * 
	 * @param writer
	 *            writer
	 * @param point
	 *            point
	 * @throws IOException
	 *             upon failure to read
	 */
	public static void writePoint(Writer writer, Point point)
			throws IOException {

		writer.write(Double.toString(point.getX()));
		writer.write(" ");
		writer.write(Double.toString(point.getY()));

		if (point.hasZ()) {
			writer.write(" ");
			writer.write(Double.toString(point.getZ()));
		}

		if (point.hasM()) {
			writer.write(" ");
			writer.write(Double.toString(point.getM()));
		}

	}

	/**
	 * Read a Line String
	 * 
	 * @param writer
	 *            writer
	 * @param lineString
	 *            line string
	 * @throws IOException
	 *             upon failure to read
	 */
	public static void writeLineString(Writer writer, LineString lineString)
			throws IOException {

		if (lineString.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < lineString.numPoints(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writePoint(writer, lineString.getPoint(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Polygon
	 * 
	 * @param writer
	 *            writer
	 * @param polygon
	 *            Polygon
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writePolygon(Writer writer, Polygon polygon)
			throws IOException {

		if (polygon.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < polygon.numRings(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writeLineString(writer, polygon.getRing(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Multi Point
	 * 
	 * @param writer
	 *            writer
	 * @param multiPoint
	 *            multi point
	 * @throws IOException
	 *             upon failure to read
	 */
	public static void writeMultiPoint(Writer writer, MultiPoint multiPoint)
			throws IOException {

		if (multiPoint.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < multiPoint.numPoints(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writePointText(writer, multiPoint.getPoint(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Multi Line String
	 * 
	 * @param writer
	 *            writer
	 * @param multiLineString
	 *            Multi Line String
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeMultiLineString(Writer writer,
			MultiLineString multiLineString) throws IOException {

		if (multiLineString.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < multiLineString.numLineStrings(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writeLineString(writer, multiLineString.getLineString(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Multi Polygon
	 * 
	 * @param writer
	 *            writer
	 * @param multiPolygon
	 *            Multi Polygon
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeMultiPolygon(Writer writer,
			MultiPolygon multiPolygon) throws IOException {

		if (multiPolygon.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < multiPolygon.numPolygons(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writePolygon(writer, multiPolygon.getPolygon(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Geometry Collection
	 * 
	 * @param writer
	 *            writer
	 * @param geometryCollection
	 *            Geometry Collection
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeGeometryCollection(Writer writer,
			GeometryCollection<?> geometryCollection) throws IOException {

		if (geometryCollection.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < geometryCollection.numGeometries(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writeGeometry(writer, geometryCollection.getGeometry(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Circular String
	 * 
	 * @param writer
	 *            writer
	 * @param circularString
	 *            Circular String
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeCircularString(Writer writer,
			CircularString circularString) throws IOException {

		if (circularString.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < circularString.numPoints(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writePoint(writer, circularString.getPoint(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Compound Curve
	 * 
	 * @param writer
	 *            writer
	 * @param compoundCurve
	 *            Compound Curve
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeCompoundCurve(Writer writer,
			CompoundCurve compoundCurve) throws IOException {

		if (compoundCurve.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < compoundCurve.numLineStrings(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writeGeometry(writer, compoundCurve.getLineString(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Curve Polygon
	 * 
	 * @param writer
	 *            writer
	 * @param curvePolygon
	 *            Curve Polygon
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeCurvePolygon(Writer writer,
			CurvePolygon<?> curvePolygon) throws IOException {

		if (curvePolygon.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < curvePolygon.numRings(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writeGeometry(writer, curvePolygon.getRing(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Polyhedral Surface
	 * 
	 * @param writer
	 *            writer
	 * @param polyhedralSurface
	 *            Polyhedral Surface
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writePolyhedralSurface(Writer writer,
			PolyhedralSurface polyhedralSurface) throws IOException {

		if (polyhedralSurface.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < polyhedralSurface.numPolygons(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writePolygon(writer, polyhedralSurface.getPolygon(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a TIN
	 * 
	 * @param writer
	 *            writer
	 * @param tin
	 *            TIN
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeTIN(Writer writer, TIN tin) throws IOException {

		if (tin.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < tin.numPolygons(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writePolygon(writer, tin.getPolygon(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write a Triangle
	 * 
	 * @param writer
	 *            writer
	 * @param triangle
	 *            Triangle
	 * @throws IOException
	 *             upon failure to write
	 */
	public static void writeTriangle(Writer writer, Triangle triangle)
			throws IOException {

		if (triangle.isEmpty()) {
			writeEmpty(writer);
		} else {
			writer.write("(");

			for (int i = 0; i < triangle.numRings(); i++) {
				if (i > 0) {
					writer.write(", ");
				}
				writeLineString(writer, triangle.getRing(i));
			}

			writer.write(")");
		}

	}

	/**
	 * Write the empty set
	 * 
	 * @param writer
	 * @throws IOException
	 *             upon failure to read
	 */
	private static void writeEmpty(Writer writer) throws IOException {
		writer.write("EMPTY");
	}

}
