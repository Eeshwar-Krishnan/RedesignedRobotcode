package Utils.Path;

import java.util.ArrayList;

import MathSystems.Position;
import MathSystems.Vector.Vector2;

public class Path {
	private ArrayList<Position> lines;
	private ArrayList<Vector2> pathLines;
	private Position endPoint;
	private Position startPoint;

	public Path(Position startPoint, Position endPoint, ArrayList<Position> lines) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.lines = lines;
		pathLines = new ArrayList<Vector2>();
		for(Position v : lines) {
			pathLines.add(v.toVector3().getVector2());
		}
	}
	
	public Position getStartpoint() {
		return startPoint;
	}
	
	public Position getEndpoint() {
		return endPoint;
	}
	
	public ArrayList<Vector2> getPathLines(){
		return pathLines;
	}
	
	public ArrayList<Position> getLines(){
		return lines;
	}
}
