package Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import Hardware.HardwareSystems.UltimateGoal.Intake;
import Hardware.HardwareSystems.UltimateGoal.Odometry;
import Hardware.HardwareSystems.UltimateGoal.Shooter;

public class UltimateGoalHardware extends HardwareController{
    private Drivetrain drivetrain;
    private Intake intake;
    private Shooter shooter;
    private Odometry odometry;
    public UltimateGoalHardware(HardwareMap map) {
        super(map);
    }

    @Override
    public void setupSystems() {
        drivetrain = new Drivetrain(map.dcMotor.get("bl"), map.dcMotor.get("br"), map.dcMotor.get("tl"), map.dcMotor.get("tr"));
        intake = new Intake(map.dcMotor.get("intake"), map.dcMotor.get("oa"), map.servo.get("stickLeft"), map.servo.get("stickRight"));
        controlHubSystems.add(drivetrain);
        revHubSystems.add(intake);
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
}
