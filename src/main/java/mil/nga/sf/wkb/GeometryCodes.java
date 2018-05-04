package mil.nga.sf.wkb;

import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryType;
import mil.nga.sf.util.SFException;

/**
 * Geometry Code utilities to convert between geometry attributes and geometry
 * codes
 * 
 * @author osbornb
 */
public class GeometryCodes {

	/**
	 * Get the geometry code from the geometry
	 * 
	 * @param geometry
	 *            geometry
	 * @return geometry code
	 */
	public static int getCode(Geometry geometry) {
		int code = getCode(geometry.getGeometryType());
		if (geometry.hasZ()) {
			code += 1000;
		}
		if (geometry.hasM()) {
			code += 2000;
		}
		return code;
	}

	/**
	 * Get the geometry code from the geometry type
	 * 
	 * @param geometryType
	 *            geometry type
	 * @return geometry code
	 */
	public static int getCode(GeometryType geometryType) {

		int code;

		switch (geometryType) {
		case GEOMETRY:
			code = 0;
			break;
		case POINT:
			code = 1;
			break;
		case LINESTRING:
			code = 2;
			break;
		case POLYGON:
			code = 3;
			break;
		case MULTIPOINT:
			code = 4;
			break;
		case MULTILINESTRING:
			code = 5;
			break;
		case MULTIPOLYGON:
			code = 6;
			break;
		case GEOMETRYCOLLECTION:
			code = 7;
			break;
		case CIRCULARSTRING:
			code = 8;
			break;
		case COMPOUNDCURVE:
			code = 9;
			break;
		case CURVEPOLYGON:
			code = 10;
			break;
		case MULTICURVE:
			code = 11;
			break;
		case MULTISURFACE:
			code = 12;
			break;
		case CURVE:
			code = 13;
			break;
		case SURFACE:
			code = 14;
			break;
		case POLYHEDRALSURFACE:
			code = 15;
			break;
		case TIN:
			code = 16;
			break;
		case TRIANGLE:
			code = 17;
			break;
		default:
			throw new SFException(
					"Unsupported Geometry Type for code retrieval: "
							+ geometryType);
		}

		return code;
	}

	/**
	 * Get the Geometry Type from the code
	 * 
	 * @param code
	 *            geometry type code
	 * @return geometry type
	 */
	public static GeometryType getGeometryType(int code) {

		// Look at the last 2 digits to find the geometry type code
		int geometryTypeCode = code % 1000;

		GeometryType geometryType = null;

		switch (geometryTypeCode) {
		case 0:
			geometryType = GeometryType.GEOMETRY;
			break;
		case 1:
			geometryType = GeometryType.POINT;
			break;
		case 2:
			geometryType = GeometryType.LINESTRING;
			break;
		case 3:
			geometryType = GeometryType.POLYGON;
			break;
		case 4:
			geometryType = GeometryType.MULTIPOINT;
			break;
		case 5:
			geometryType = GeometryType.MULTILINESTRING;
			break;
		case 6:
			geometryType = GeometryType.MULTIPOLYGON;
			break;
		case 7:
			geometryType = GeometryType.GEOMETRYCOLLECTION;
			break;
		case 8:
			geometryType = GeometryType.CIRCULARSTRING;
			break;
		case 9:
			geometryType = GeometryType.COMPOUNDCURVE;
			break;
		case 10:
			geometryType = GeometryType.CURVEPOLYGON;
			break;
		case 11:
			geometryType = GeometryType.MULTICURVE;
			break;
		case 12:
			geometryType = GeometryType.MULTISURFACE;
			break;
		case 13:
			geometryType = GeometryType.CURVE;
			break;
		case 14:
			geometryType = GeometryType.SURFACE;
			break;
		case 15:
			geometryType = GeometryType.POLYHEDRALSURFACE;
			break;
		case 16:
			geometryType = GeometryType.TIN;
			break;
		case 17:
			geometryType = GeometryType.TRIANGLE;
			break;
		default:
			throw new SFException(
					"Unsupported Geometry code for type retrieval: " + code);
		}

		return geometryType;
	}

	/**
	 * Determine if the geometry code has a Z (3D) value
	 * 
	 * @param code
	 *            geometry code
	 * @return true is has Z
	 */
	public static boolean hasZ(int code) {

		boolean hasZ = false;

		int mode = getGeometryMode(code);

		switch (mode) {
		case 0:
		case 2:
			break;
		case 1:
		case 3:
			hasZ = true;
			break;
		default:
			throw new SFException(
					"Unexpected Geometry code for Z determination: " + code);
		}

		return hasZ;
	}

	/**
	 * Determine if the geometry code has a M (linear referencing system) value
	 * 
	 * @param code
	 *            geometry code
	 * @return true is has M
	 */
	public static boolean hasM(int code) {

		boolean hasM = false;

		int mode = getGeometryMode(code);

		switch (mode) {
		case 0:
		case 1:
			break;
		case 2:
		case 3:
			hasM = true;
			break;
		default:
			throw new SFException(
					"Unexpected Geometry code for M determination: " + code);
		}

		return hasM;
	}

	/**
	 * Get the geometry mode from the geometry code. Returns the digit in the
	 * thousands place. (z is enabled when 1 or 3, m is enabled when 2 or 3)
	 * 
	 * @param code
	 *            geometry code
	 * @return geometry mode
	 */
	public static int getGeometryMode(int code) {
		return code / 1000;
	}

}
