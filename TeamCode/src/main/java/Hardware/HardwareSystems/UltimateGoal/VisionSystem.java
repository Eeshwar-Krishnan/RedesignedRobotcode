package Hardware.HardwareSystems.UltimateGoal;

import android.content.Context;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import Hardware.HardwareSystems.HardwareSystem;
import Utils.Vision.Rings.RingPipeline;
import Utils.Vision.TowerGoal.TowerPipeline;

public class VisionSystem extends HardwareSystem {
    private WebcamName towerCamName, ringCamName;
    private OpenCvCamera ringCamera, towerCamera;
    private TowerPipeline towerPipeline;
    private RingPipeline ringPipeline;

    public VisionSystem(WebcamName towerCam, WebcamName ringCam, Context context){
        this.towerCamName = towerCam;
        this.ringCamName = ringCam;

        towerCamera = OpenCvCameraFactory.getInstance().createWebcam(towerCamName);
        ringCamera = OpenCvCameraFactory.getInstance().createWebcam(ringCamName);

        towerPipeline = new TowerPipeline(context, 60);
        ringPipeline = new RingPipeline();
    }

    @Override
    public void calibrate() {
        towerCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                towerCamera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                towerCamera.setPipeline(towerPipeline);
            }
        });

        ringCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                ringCamera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                ringCamera.setPipeline(ringPipeline);
            }
        });
    }

    @Override
    public void update() {

    }

    public void disableRing(){
        ringCamera.closeCameraDeviceAsync(new OpenCvCamera.AsyncCameraCloseListener() {
            @Override
            public void onClose() {

            }
        });
    }

    public void disableTower(){
        towerCamera.closeCameraDeviceAsync(new OpenCvCamera.AsyncCameraCloseListener() {
            @Override
            public void onClose() {

            }
        });
    }

    public int getRings(){
        return ringPipeline.getNumRings();
    }

    public double getHeading(){
        return towerPipeline.getHeading();
    }

    public double getRange(){
        return towerPipeline.getRange();
    }

    public boolean hasTowerTrack(){
        return towerPipeline.getTrack();
    }

    public double getPitch(){
        return towerPipeline.getPitch();
    }

    public double[] getPowershots(){
        double[] arr = towerPipeline.getPowershots();
        return new double[]{arr[0]-2, arr[1]-1, arr[2]-2};
    }

    public double calibrateTowerCam(){
        double pitchOffset = towerPipeline.calibratePitch();
        towerPipeline.setPitchOffset(pitchOffset);
        return pitchOffset;
    }

    public void setPitchOffset(double offset){
        towerPipeline.setPitchOffset(offset);
    }

    public double[] getPos(){
        return towerPipeline.getPosition();
    }
}