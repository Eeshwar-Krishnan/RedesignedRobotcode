package MathSystems;

public class Angle {
    private double angle;
    private Angle(double angle){
        this.angle = angle;
    }

    public double degrees(){
        return Math.toDegrees(angle);
    }

    public double radians(){
        return angle;
    }

    public static Angle degrees(double angle){
        return new Angle(Math.toRadians(angle));
    }

    public static Angle radians(double angle){
        return new Angle(angle);
    }

    public static Angle fromString(String angle){
        if(angle.endsWith("D")){
            return Angle.degrees(Double.parseDouble(angle.substring(0, angle.length()-1)));
        }else if(angle.endsWith("R")){
            return Angle.radians(Double.parseDouble(angle.substring(0, angle.length()-1)));
        }
        return Angle.degrees(Double.parseDouble(angle));
    }

    public static Angle ZERO(){
        return Angle.radians(0);
    }

    @Override
    public String toString() {
        return angle + " Randians | " + Math.toDegrees(angle) + " Degrees";
    }
}