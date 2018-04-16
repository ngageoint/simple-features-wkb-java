package mil.nga.wkb.geom;

/**
 * Geometry envelope
 * 
 * @author osbornb
 */
public class GeometryEnvelope {

	/**
	 * Min X
	 */
	private double minX;

	/**
	 * Max X
	 */
	private double maxX;

	/**
	 * Min Y
	 */
	private double minY;

	/**
	 * Max Y
	 */
	private double maxY;

	/**
	 * True if has z coordinates
	 */
	private boolean hasZ = false;

	/**
	 * Min Z
	 */
	private Double minZ;

	/**
	 * Max Z
	 */
	private Double maxZ;

	/**
	 * True if has M measurements
	 */
	private boolean hasM = false;

	/**
	 * Min M
	 */
	private Double minM;

	/**
	 * Max M
	 */
	private Double maxM;

	/**
	 * Constructor
	 */
	public GeometryEnvelope() {

	}

	/**
	 * Constructor
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	public GeometryEnvelope(boolean hasZ, boolean hasM) {
		this.hasZ = hasZ;
		this.hasM = hasM;
	}

	/**
	 * True if has Z coordinates
	 * 
	 * @return has z
	 */
	public boolean hasZ() {
		return hasZ;
	}

	/**
	 * True if has M measurements
	 * 
	 * @return has m
	 */
	public boolean hasM() {
		return hasM;
	}

	/**
	 * Get min x
	 * 
	 * @return min x
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * Set min x
	 * 
	 * @param minX
	 *            min x
	 */
	public void setMinX(double minX) {
		this.minX = minX;
	}

	/**
	 * Get max x
	 * 
	 * @return max x
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * Set max x
	 * 
	 * @param maxX
	 *            max x
	 */
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	/**
	 * Get min y
	 * 
	 * @return min y
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * Set min y
	 * 
	 * @param minY
	 *            min y
	 */
	public void setMinY(double minY) {
		this.minY = minY;
	}

	/**
	 * Get max y
	 * 
	 * @return max y
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * Set max y
	 * 
	 * @param maxY
	 *            max y
	 */
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	/**
	 * Has z coordinates
	 * 
	 * @return true if has z coordinates
	 */
	public boolean isHasZ() {
		return hasZ;
	}

	/**
	 * Set has z coordinates
	 * 
	 * @param hasZ
	 *            has z
	 */
	public void setHasZ(boolean hasZ) {
		this.hasZ = hasZ;
	}

	/**
	 * Get min z
	 * 
	 * @return min z
	 */
	public Double getMinZ() {
		return minZ;
	}

	/**
	 * Set min z
	 * 
	 * @param minZ
	 *            min z
	 */
	public void setMinZ(Double minZ) {
		this.minZ = minZ;
	}

	/**
	 * Get max z
	 * 
	 * @return max z
	 */
	public Double getMaxZ() {
		return maxZ;
	}

	/**
	 * Set max z
	 * 
	 * @param maxZ
	 *            max z
	 */
	public void setMaxZ(Double maxZ) {
		this.maxZ = maxZ;
	}

	/**
	 * Has m coordinates
	 * 
	 * @return true if has m coordinates
	 */
	public boolean isHasM() {
		return hasM;
	}

	/**
	 * Set has m coordinates
	 * 
	 * @param hasM
	 *            has m
	 */
	public void setHasM(boolean hasM) {
		this.hasM = hasM;
	}

	/**
	 * Get min m
	 * 
	 * @return min m
	 */
	public Double getMinM() {
		return minM;
	}

	/**
	 * Set min m
	 * 
	 * @param minM
	 *            min m
	 */
	public void setMinM(Double minM) {
		this.minM = minM;
	}

	/**
	 * Get max m
	 * 
	 * @return max m
	 */
	public Double getMaxM() {
		return maxM;
	}

	/**
	 * Set max m
	 * 
	 * @param maxM
	 *            max m
	 */
	public void setMaxM(Double maxM) {
		this.maxM = maxM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hasM ? 1231 : 1237);
		result = prime * result + (hasZ ? 1231 : 1237);
		result = prime * result + ((maxM == null) ? 0 : maxM.hashCode());
		long temp;
		temp = Double.doubleToLongBits(maxX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((maxZ == null) ? 0 : maxZ.hashCode());
		result = prime * result + ((minM == null) ? 0 : minM.hashCode());
		temp = Double.doubleToLongBits(minX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((minZ == null) ? 0 : minZ.hashCode());
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
		GeometryEnvelope other = (GeometryEnvelope) obj;
		if (hasM != other.hasM)
			return false;
		if (hasZ != other.hasZ)
			return false;
		if (maxM == null) {
			if (other.maxM != null)
				return false;
		} else if (!maxM.equals(other.maxM))
			return false;
		if (Double.doubleToLongBits(maxX) != Double
				.doubleToLongBits(other.maxX))
			return false;
		if (Double.doubleToLongBits(maxY) != Double
				.doubleToLongBits(other.maxY))
			return false;
		if (maxZ == null) {
			if (other.maxZ != null)
				return false;
		} else if (!maxZ.equals(other.maxZ))
			return false;
		if (minM == null) {
			if (other.minM != null)
				return false;
		} else if (!minM.equals(other.minM))
			return false;
		if (Double.doubleToLongBits(minX) != Double
				.doubleToLongBits(other.minX))
			return false;
		if (Double.doubleToLongBits(minY) != Double
				.doubleToLongBits(other.minY))
			return false;
		if (minZ == null) {
			if (other.minZ != null)
				return false;
		} else if (!minZ.equals(other.minZ))
			return false;
		return true;
	}

}
