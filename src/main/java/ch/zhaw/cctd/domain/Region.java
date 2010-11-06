package ch.zhaw.cctd.domain;

import java.awt.Point;
import java.awt.Rectangle;

public class Region extends Rectangle
 {

	private static final long serialVersionUID = -4200967409487789290L;

	public Region(Point point) {
		super(point);
	}
	
	public Point getRandomPoint() {
		
		int x = (int) (super.getX() + Math.floor(Math.random() * (double) super.getWidth()));
		int y = (int) (super.getY() + Math.floor(Math.random() * (double) super.getHeight()));
		return new Point(x,y);
	}

}
