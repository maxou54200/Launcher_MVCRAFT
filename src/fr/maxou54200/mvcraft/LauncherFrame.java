package fr.maxou54200.mvcraft;

import javax.swing.JFrame;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

public class LauncherFrame extends JFrame {

    private static LauncherFrame instance;
    private LauncherPanel launcherpanel;
    
    public static LauncherFrame getInstance() {
    	return instance;
    }

    public LauncherFrame() {
        this.setTitle("MV Craft");
        this.setSize(906, 510);
        //this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setIconImage(Swinger.getResource("logo.png"));
        this.setContentPane(launcherpanel = new LauncherPanel());
         
        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);
        
        this.setVisible(true);
        
    }


    public static void main(String[] args) {
    	Swinger.setSystemLookNFeel();
    	Swinger.setResourcePath("/fr/maxou54200/mvcraft/resources_custom/");
    	Launcher.MV_CRASHES_DIR.mkdir();
    	
        instance = new LauncherFrame();
        
    }

	public LauncherPanel getLauncherPanel() {
		return this.launcherpanel;
	}

}
    