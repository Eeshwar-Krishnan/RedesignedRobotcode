package Hardware;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import Hardware.HardwareSystems.HardwareSystem;

public abstract class HardwareController {
    private static final long HEARTBEAT_TIMEOUT_MS = 2000;

    protected LynxModule controlHub, expansionHub;
    private final HardwareMap hardwareMap;
    public ArrayList<HardwareSystem> chubHardwareSystems, ehubHardwareSystems;
    private final AtomicLong heartbeat;
    private AtomicLong chubLatency, ehubLatency;

    public HardwareController(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        this.chubHardwareSystems = new ArrayList<>();
        this.ehubHardwareSystems = new ArrayList<>();
        this.heartbeat = new AtomicLong(-1);
        this.chubLatency = new AtomicLong(1);
        this.ehubLatency = new AtomicLong(1);
        List<LynxModule> modules = hardwareMap.getAll(LynxModule.class);
        for(LynxModule lynx : modules){
            lynx.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if(lynx.isUserModule()){
                if(lynx.isParent() && LynxConstants.isEmbeddedSerialNumber(lynx.getSerialNumber())){
                    controlHub = lynx;
                }else{
                    expansionHub = lynx;
                }
            }
        }
    }

    public abstract void setupSystems(HardwareMap hardwareMap);

    public void initialize(){
        setupSystems(hardwareMap);

        controlHub.clearBulkCache();
        for(HardwareSystem system : chubHardwareSystems){
            system.initialize();
        }

        expansionHub.clearBulkCache();
        for(HardwareSystem system : ehubHardwareSystems){
            system.initialize();
        }

        heartbeat.set(System.currentTimeMillis());

        new Thread(() -> {
            long lastTime = System.currentTimeMillis();
            while((System.currentTimeMillis() - heartbeat.get()) < HEARTBEAT_TIMEOUT_MS){
                controlHub.clearBulkCache();
                for(HardwareSystem system : chubHardwareSystems){
                    system.update();
                }
                long now = System.currentTimeMillis();
                chubLatency.set(now - lastTime);
                lastTime = now;
            }
        }).start();

        new Thread(() -> {
            long lastTime = System.currentTimeMillis();
            while((System.currentTimeMillis() - heartbeat.get()) < HEARTBEAT_TIMEOUT_MS){
                expansionHub.clearBulkCache();
                for(HardwareSystem system : ehubHardwareSystems){
                    system.update();
                }
                long now = System.currentTimeMillis();
                ehubLatency.set(now - lastTime);
                lastTime = now;
            }
        }).start();
    }

    public void update(){
        heartbeat.set(System.currentTimeMillis());
    }

    public long getChubLatency(){
        return chubLatency.get();
    }

    public long getEhubLatency(){
        return ehubLatency.get();
    }
}
