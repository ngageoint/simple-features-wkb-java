package mil.nga.sf.wkb;

import mil.nga.sf.GeometryType;

/**
 * Geometry type info
 * 
 * @author osbornb
 */
public class GeometryTypeInfo {

	/**
	 * Geometry type code
	 */
	private final int geometryTypeCode;

	/**
	 * Geometry type
	 */
	private final GeometryType geometryType;

	/**
	 * Has Z values flag
	 */
	private final boolean hasZ;

	/**
	 * Has M values flag
	 */
	private final boolean hasM;

	/**
	 * Constructor
	 * 
	 * @param geometryTypeCode
	 *            geometry type code
	 * @param geometryType
	 *            geometry type
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	GeometryTypeInfo(int geometryTypeCode, GeometryType geometryType,
			boolean hasZ, boolean hasM) {
		this.geometryTypeCode = geometryTypeCode;
		this.geometryType = geometryType;
		this.hasZ = hasZ;
		this.hasM = hasM;
	}

	/**
	 * Get the geometry type code
	 * 
	 * @return geometry type code
	 */
	public int getGeometryTypeCode() {
		return geometryTypeCode;
	}

	/**
	 * Get the geometry type
	 * 
	 * @return geometry type
	 */
	public GeometryType getGeometryType() {
		return geometryType;
	}

	/**
	 * Has z values
	 * 
	 * @return true if has z values
	 */
	public boolean hasZ() {
		return hasZ;
	}

	/**
	 * Has m values
	 * 
	 * @return true if has m values
	 */
	public boolean hasM() {
		return hasM;
	}

}
