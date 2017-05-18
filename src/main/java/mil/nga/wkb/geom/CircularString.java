package mil.nga.wkb.geom;

/**
 * Circular String, Curve sub type
 * 
 * @author osbornb
 */
public class CircularString extends LineString {

	/**
	 * Constructor
	 */
	public CircularString() {
		this(false, false);
	}

	/**
	 * Constructor
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	public CircularString(boolean hasZ, boolean hasM) {
		super(GeometryType.CIRCULARSTRING, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param circularString
	 *            circular string to copy
	 */
	public CircularString(CircularString circularString) {
		this(circularString.hasZ(), circularString.hasM());
		for (Point point : circularString.getPoints()) {
			addPoint((Point) point.copy());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new CircularString(this);
	}

}
