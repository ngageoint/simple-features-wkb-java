package mil.nga.wkb.geom.extended;

import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.util.WkbException;

/**
 * Extended Geometry Collection providing abstract geometry collection type
 * support
 * 
 * @author osbornb
 *
 * @param <T>
 *            geometry type
 */
public class ExtendedGeometryCollection<T extends Geometry> extends
		GeometryCollection<T> {

	/**
	 * Extended geometry collection geometry type
	 */
	private GeometryType geometryType = GeometryType.GEOMETRYCOLLECTION;

	/**
	 * Constructor, wraps a geometry collection as extended
	 * 
	 * @param geometryCollection
	 */
	public ExtendedGeometryCollection(GeometryCollection<T> geometryCollection) {
		super(GeometryType.GEOMETRYCOLLECTION, geometryCollection.hasZ(),
				geometryCollection.hasM());
		setGeometries(geometryCollection.getGeometries());
		updateGeometryType();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param extendedGeometryCollection
	 *            extended geometry collection to copy
	 */
	public ExtendedGeometryCollection(
			ExtendedGeometryCollection<T> extendedGeometryCollection) {
		super(GeometryType.GEOMETRYCOLLECTION, extendedGeometryCollection
				.hasZ(), extendedGeometryCollection.hasM());
		for (T geometry : extendedGeometryCollection.getGeometries()) {
			@SuppressWarnings("unchecked")
			T geometryCopy = (T) geometry.copy();
			addGeometry(geometryCopy);
		}
		geometryType = extendedGeometryCollection.getGeometryType();
	}

	/**
	 * Update the extended geometry type based upon the contained geometries
	 */
	public void updateGeometryType() {
		GeometryType geometryType = getCollectionType();
		switch (geometryType) {
		case GEOMETRYCOLLECTION:
		case MULTICURVE:
		case MULTISURFACE:
			break;
		case MULTIPOINT:
			geometryType = GeometryType.GEOMETRYCOLLECTION;
			break;
		case MULTILINESTRING:
			geometryType = GeometryType.MULTICURVE;
			break;
		case MULTIPOLYGON:
			geometryType = GeometryType.MULTISURFACE;
			break;
		default:
			throw new WkbException(
					"Unsupported extended geometry collection geometry type: "
							+ geometryType);
		}
		this.geometryType = geometryType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometryType getGeometryType() {
		return geometryType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new ExtendedGeometryCollection<T>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((geometryType == null) ? 0 : geometryType.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		ExtendedGeometryCollection<T> other = (ExtendedGeometryCollection<T>) obj;
		if (geometryType != other.geometryType)
			return false;
		return true;
	}

}
