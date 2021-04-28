package MathSystems.Path;

import java.util.ArrayList;

import MathSystems.Angle;
import MathSystems.MathUtils;
import MathSystems.Position;
import MathSystems.Vector2;
import MathSystems.Vector3;

public class PathBuilder {
	private ArrayList<Position> lines;
	private Position startPoint;
	
	public PathBuilder(Position startPoint) {
		lines = new ArrayList<>();
		this.startPoint = startPoint;
		lines.add(startPoint);
	}

	public PathBuilder(double x, double y, Angle angle){
		this(new Position(x, y, angle));
	}

	public PathBuilder(Vector2 linPos, Angle angle){
		this(new Position(linPos, angle));
	}
	
	public PathBuilder lineTo(double x, double y, Angle r) {
		lines.add(new Position(x, y, r));
		return this;
	}

	public PathBuilder lineTo(Vector2 point, Angle angle) {
		return lineTo(point.getA(), point.getB(), angle);
	}

	public PathBuilder lineTo(Position position){
		lines.add(position);
		return this;
	}
	
	public PathBuilder lineTo(Vector2 point) {
		return lineTo(point.getA(), point.getB());
	}
	
	public PathBuilder lineTo(double x, double y) {
		return lineTo(x, y, lines.get(lines.size()-1).getR());
	}
	
	public PathBuilder bezierSplineTo(Vector2 end, Vector2 control) {
		return bezierSplineTo(end, control, lines.get(lines.size()-1).getR());
	}

	public PathBuilder bezierSplineTo(Position end, Vector2 control){
		return bezierSplineTo(end.getPos(), control, end.getR());
	}
	
	public PathBuilder bezierSplineTo(Vector2 end, Vector2 control, Angle angle) {
		ArrayList<Vector2> vectors = (MathUtils.approxCurve(lines.get(lines.size()-1).getPos(), end, control));
		for(Vector2 v : vectors) {
			lines.add(new Position(v, angle));
		}
		return this;
	}
	
	public Path complete() {
		return new Path(startPoint, lines.get(lines.size()-1), lines);
	}
}
