package mil.nga.wkb.geom;

/**
 * The root of the geometry type hierarchy
 * 
 * @author osbornb
 */
public abstract class Geometry {

	/**
	 * Geometry type
	 */
	private final GeometryType geometryType;

	/**
	 * Has z coordinates
	 */
	private final boolean hasZ;

	/**
	 * Has m values
	 */
	private final boolean hasM;

	/**
	 * Constructor
	 * 
	 * @param geometryType
	 *            geometry type
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	protected Geometry(GeometryType geometryType, boolean hasZ, boolean hasM) {
		this.geometryType = geometryType;
		this.hasZ = hasZ;
		this.hasM = hasM;
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
	 * Does the geometry have z coordinates
	 * 
	 * @return true if has z coordinates
	 */
	public boolean hasZ() {
		return hasZ;
	}

	/**
	 * Does the geometry have m coordinates
	 * 
	 * @return true if has m coordinates
	 */
	public boolean hasM() {
		return hasM;
	}

	/**
	 * Get the Well-Known Binary code
	 * 
	 * @return Well-Known Binary code
	 */
	public int getWkbCode() {
		int code = getGeometryType().getCode();
		if (hasZ) {
			code += 1000;
		}
		if (hasM) {
			code += 2000;
		}
		return code;
	}

	/**
	 * Copy the geometry
	 * 
	 * @return geometry copy
	 */
	public abstract Geometry copy();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((geometryType == null) ? 0 : geometryType.hashCode());
		result = prime * result + (hasM ? 1231 : 1237);
		result = prime * result + (hasZ ? 1231 : 1237);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Geometry other = (Geometry) obj;
		if (geometryType != other.geometryType)
			return false;
		if (hasM != other.hasM)
			return false;
		if (hasZ != other.hasZ)
			return false;
		return true;
	}

}
