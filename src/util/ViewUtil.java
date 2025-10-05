
package util;

import java.awt.Component;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;


public class ViewUtil {
    
    public static void centerScreen(JDesktopPane desktop, JInternalFrame frame){
        int x = (desktop.getWidth() / 2) - frame.getWidth() /2;
        int y = (desktop.getHeight()/ 2) - frame.getHeight() /2;
        if (frame.isShowing()) {
            frame.setLocation(x, y);
        }
    }
    
    /**
     * Obtiene el JDesktopPane ancestro de un componente dado.
     * @param component El componente desde el cual buscar el ancestro.
     * @return El JDesktopPane ancestro, o null si no se encuentra.
     */
    public static JDesktopPane getDesktopPaneAncestor(Component component) {
        return (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, component);
    }
    
}
