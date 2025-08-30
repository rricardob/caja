
package util;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;


public class ViewUtil {
    
    public static void centerScreen(JDesktopPane desktop, JInternalFrame frame){
        int x = (desktop.getWidth() / 2) - frame.getWidth() /2;
        int y = (desktop.getHeight()/ 2) - frame.getHeight() /2;
        if (frame.isShowing()) {
            frame.setLocation(x, y);
        }
    }
    
}
