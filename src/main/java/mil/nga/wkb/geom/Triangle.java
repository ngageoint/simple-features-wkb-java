package mil.nga.wkb.geom;

/**
 * Triangle
 * 
 * @author osbornb
 */
public class Triangle extends Polygon {

	/**
	 * Constructor
	 */
	public Triangle() {
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
	public Triangle(boolean hasZ, boolean hasM) {
		super(GeometryType.TRIANGLE, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param triangle
	 *            triangle to copy
	 */
	public Triangle(Triangle triangle) {
		this(triangle.hasZ(), triangle.hasM());
		for (LineString ring : triangle.getRings()) {
			addRing((LineString) ring.copy());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new Triangle(this);
	}

}
