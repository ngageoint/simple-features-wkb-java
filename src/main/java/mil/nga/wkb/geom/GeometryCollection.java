package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of zero or more Geometry instances.
 * 
 * @author osbornb
 */
public class GeometryCollection<T extends Geometry> extends Geometry {

	/**
	 * List of geometries
	 */
	private List<T> geometries = new ArrayList<T>();

	/**
	 * Constructor
	 */
	public GeometryCollection() {
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
	public GeometryCollection(boolean hasZ, boolean hasM) {
		super(GeometryType.GEOMETRYCOLLECTION, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param geometryCollection
	 *            geometry collection to copy
	 */
	public GeometryCollection(GeometryCollection<T> geometryCollection) {
		this(geometryCollection.hasZ(), geometryCollection.hasM());
		for (T geometry : geometryCollection.getGeometries()) {
			@SuppressWarnings("unchecked")
			T geometryCopy = (T) geometry.copy();
			addGeometry(geometryCopy);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            geometry type
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	protected GeometryCollection(GeometryType type, boolean hasZ, boolean hasM) {
		super(type, hasZ, hasM);
	}

	/**
	 * Get the list of geometries
	 * 
	 * @return geometries
	 */
	public List<T> getGeometries() {
		return geometries;
	}

	/**
	 * Set the geometries
	 * 
	 * @param geometries
	 *            geometries
	 */
	public void setGeometries(List<T> geometries) {
		this.geometries = geometries;
	}

	/**
	 * Add a geometry
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void addGeometry(T geometry) {
		geometries.add(geometry);
	}

	/**
	 * Get the number of geometries in the collection
	 * 
	 * @return number of geometries
	 */
	public int numGeometries() {
		return geometries.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new GeometryCollection<T>(this);
	}

}
