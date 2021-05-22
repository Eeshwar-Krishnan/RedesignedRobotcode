package Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import Hardware.HardwareSystems.UltimateGoal.Intake;
import Hardware.HardwareSystems.UltimateGoal.Odometry;
import Hardware.HardwareSystems.UltimateGoal.Shooter;
import Hardware.HardwareSystems.UltimateGoal.VisionSystem;

public class UltimateGoalHardware extends HardwareController{
    private Drivetrain drivetrain;
    private Intake intake;
    private Shooter shooter;
    private Odometry odometry;
    private VisionSystem vision;
    public UltimateGoalHardware(HardwareMap map) {
        super(map);
    }

    @Override
    public void setupSystems() {
        drivetrain = new Drivetrain(map.dcMotor.get("bl"), map.dcMotor.get("br"), map.dcMotor.get("tl"), map.dcMotor.get("tr"));
        intake = new Intake(map.dcMotor.get("intake"), map.dcMotor.get("oa"), map.servo.get("stickLeft"), map.servo.get("stickRight"));
        vision = new VisionSystem(map.get(WebcamName.class, "Webcam 1"), map.get(WebcamName.class, "Webcam 2"), map.appContext);
        controlHubSystems.add(drivetrain);
        revHubSystems.add(intake);
        revHubSystems.add(vision);
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public Intake getIntake() {
        return intake;
    }

    public Odometry getOdometry() {
        return odometry;
    }

    public Shooter getShooter() {
        return shooter;
    }

    public VisionSystem getVision() {
        return vision;
    }
}
