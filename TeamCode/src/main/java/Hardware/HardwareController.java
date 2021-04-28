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

public abstract class HardwareController {
    public HardwareMap map;
    private final ArrayList<LynxModule> revHubs;
    public HashMap<LynxModule, ArrayList<HardwareSystem>> hardwareSystems;
    public ArrayList<HardwareSystem> controlHubSystems;
    public ArrayList<HardwareSystem> revHubSystems;
    public LynxModule controlHub, revHub;
    private Thread controlhubThread, revhubThread;
    private AtomicBoolean active;

    public HardwareController(HardwareMap map){
        this.map = map;
        revHubs = new ArrayList<>();
        hardwareSystems = new HashMap<>();
        active = new AtomicBoolean(true);
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
        controlhubThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(active.get()){
                    controlHub.clearBulkCache();
                    for (HardwareSystem system : controlHubSystems) {
                        system.update();
                    }
                }
            }
        });
        revhubThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(active.get()){
                    revHub.clearBulkCache();
                    for(HardwareSystem system : controlHubSystems){
                        system.update();
                    }
                }
            }
        });
        controlhubThread.start();
        revhubThread.start();
    }

    public abstract void setupSystems();

    public void forceInterruptRefresh(){
        for (LynxModule m : revHubs) {
            m.clearBulkCache();
        }
    }

    public void deactivate(){
        active.set(false);
    }
}
