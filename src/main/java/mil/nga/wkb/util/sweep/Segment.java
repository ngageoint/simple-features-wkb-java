package mil.nga.wkb.util.sweep;

import mil.nga.wkb.geom.Point;

public class Segment {

	private int edge;
	private Point leftPoint;
	private Point rightPoint;
	private Segment above;
	private Segment below;

	public Segment() {

	}

	public int getEdge() {
		return edge;
	}

	public void setEdge(int edge) {
		this.edge = edge;
	}

	public Point getLeftPoint() {
		return leftPoint;
	}

	public void setLeftPoint(Point leftPoint) {
		this.leftPoint = leftPoint;
	}

	public Point getRightPoint() {
		return rightPoint;
	}

	public void setRightPoint(Point rightPoint) {
		this.rightPoint = rightPoint;
	}

	public Segment getAbove() {
		return above;
	}

	public void setAbove(Segment above) {
		this.above = above;
	}

	public Segment getBelow() {
		return below;
	}

	public void setBelow(Segment below) {
		this.below = below;
	}

}
