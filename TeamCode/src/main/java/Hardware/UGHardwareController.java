package Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import Hardware.HardwareSystems.UGSystems.DrivetrainSystem;
import Hardware.HardwareSystems.UGSystems.IntakeSystem;
import Hardware.HardwareSystems.UGSystems.OdometrySystem;
import Hardware.HardwareSystems.UGSystems.ShooterSystem;

public class UGHardwareController extends HardwareController {
    private DrivetrainSystem drivetrainSystem;
    private ShooterSystem shooterSystem;
    private IntakeSystem intakeSystem;
    private OdometrySystem odometrySystem;

    public UGHardwareController(HardwareMap hardwareMap) {
        super(hardwareMap);
    }

    @Override
    public void setupSystems(HardwareMap hardwareMap) {
        drivetrainSystem = new DrivetrainSystem(hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br"), hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"));
        shooterSystem = new ShooterSystem(hardwareMap.dcMotor.get("ol"), hardwareMap.dcMotor.get("shooter"), hardwareMap.servo.get("shooterLoadArm"), hardwareMap.servo.get("turret"), hardwareMap.servo.get("shooterTilt"));
        intakeSystem = new IntakeSystem(hardwareMap.dcMotor.get("intake"), hardwareMap.dcMotor.get("oa"), hardwareMap.servo.get("intakeShield"));
        odometrySystem = new OdometrySystem(hardwareMap.dcMotor.get("intake"), hardwareMap.dcMotor.get("oa"), hardwareMap.dcMotor.get("br"));
        chubHardwareSystems.add(drivetrainSystem);
        ehubHardwareSystems.add(shooterSystem);
        ehubHardwareSystems.add(intakeSystem);
        chubHardwareSystems.add(odometrySystem);
    }

    public DrivetrainSystem getDrivetrainSystem() {
        return drivetrainSystem;
    }

    public ShooterSystem getShooterSystem() {
        return shooterSystem;
    }

    public IntakeSystem getIntakeSystem() {
        return intakeSystem;
    }

    public OdometrySystem getOdometrySystem() {
        return odometrySystem;
    }
}
