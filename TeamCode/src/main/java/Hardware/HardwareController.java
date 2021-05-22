package Hardware;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LynxModuleMeta;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.ThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import Hardware.HardwareSystems.HardwareSystem;
import State.StateMachine;
import State.States.AsyncState;
import Utils.OpmodeStatus;
import Utils.ThreadManager;

public abstract class HardwareController {
    public HardwareMap map;
    private final ArrayList<LynxModule> revHubs;
    public HashMap<LynxModule, ArrayList<HardwareSystem>> hardwareSystems;
    public ArrayList<HardwareSystem> controlHubSystems;
    public ArrayList<HardwareSystem> revHubSystems;
    public LynxModule controlHub, revHub;
    private Runnable controlhubThread, revhubThread;
    private AtomicBoolean active;
    private long timestamp;
    private final StateMachine controlStates;
    private final StateMachine revStates;

    public HardwareController(HardwareMap map){
        this.map = map;
        revHubs = new ArrayList<>();
        hardwareSystems = new HashMap<>();
        active = new AtomicBoolean(true);
        controlStates = new StateMachine();
        revStates = new StateMachine();
        timestamp = 0;
    }

    public void init(){
        revHubs.addAll(map.getAll(LynxModule.class));
        for(LynxModule m : revHubs){
            m.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if(m.isParent() && LynxConstants.isEmbeddedSerialNumber(m.getSerialNumber())){
                controlHub = m;
            }else{
                revHub = m;
            }
        }
        setupSystems();
        for(LynxModule m : revHubs){
            m.clearBulkCache();
        }
        for(HardwareSystem system : controlHubSystems){
            system.calibrate();
        }
        for(HardwareSystem system : revHubSystems){
            system.calibrate();
        }
        controlhubThread = new Runnable() {
            @Override
            public void run() {
                while(active.get()){
                    controlHub.clearBulkCache();
                    for (HardwareSystem system : controlHubSystems) {
                        system.update();
                    }
                    synchronized (controlStates){
                        controlStates.update();
                    }
                }
            }
        };
        revhubThread = new Runnable() {
            @Override
            public void run() {
                while(active.get()){
                    revHub.clearBulkCache();
                    for(HardwareSystem system : controlHubSystems){
                        system.update();
                    }
                    synchronized (revStates){
                        revStates.update();
                    }
                }
            }
        };
        ThreadManager.execute(controlhubThread);
        ThreadManager.execute(revhubThread);
    }

    public abstract void setupSystems();

    public void forceRefresh(){
        synchronized (revHubs) {
            for (LynxModule m : revHubs) {
                m.clearBulkCache();
            }
        }
    }

    public void attachControlState(String name, AsyncState state){
        synchronized (controlStates){
            controlStates.appendState(name, state);
        }
    }

    public void attachRevState(String name, AsyncState state){
        synchronized (revStates){
            revStates.appendState(name, state);
        }
    }

    public void heartbeat(){
        if(timestamp == 0){
            timestamp = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - timestamp > 5000 || OpmodeStatus.opmodeActive()){
            //TODO
            //Probably better ways to handle this, but if the hardware hasn't talked to the logic thread in more then 5 seconds
            //Then the logic thread (AKA) the opmode is probably dead, and we should kill the hardware thread before problems happen
            active.set(false);
        }
    }

    public void deactivate(){
        active.set(false);
    }
}
