package Hardware;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;

import java.util.ArrayList;
import java.util.List;

import Hardware.HardwareSystems.HardwareSystem;

public abstract class HardwareController {
    protected LynxModule controlHub, revHub;
    private HardwareMap hardwareMap;
    private ArrayList<HardwareSystem> hardwareSystems;

    public HardwareController(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        this.hardwareSystems = new ArrayList<>();
        List<LynxModule> modules = hardwareMap.getAll(LynxModule.class);
        for(LynxModule lynx : modules){
            if(lynx.isUserModule()){
                if(lynx.isParent() && LynxConstants.isEmbeddedSerialNumber(lynx.getSerialNumber())){
                    controlHub = lynx;
                }else{
                    revHub = lynx;
                }
            }
        }
    }

    public abstract void setupSystems(HardwareMap hardwareMap);

    public void initialize(){
        setupSystems(hardwareMap);
        for(HardwareSystem system : hardwareSystems){
            system.initialize();
        }
    }
}
