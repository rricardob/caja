/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author ricardo
 */
public class ViewUtil {
    
    public static void centerScreen(JDesktopPane desktop, JInternalFrame frame){
        int x = (desktop.getWidth() / 2) - frame.getWidth() /2;
        int y = (desktop.getHeight()/ 2) - frame.getHeight() /2;
        if (frame.isShowing()) {
            frame.setLocation(x, y);
        }
    }
    
}
