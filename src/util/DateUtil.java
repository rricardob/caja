
package util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
    
    public static String obtenerFechaActual(){
        Date fechaActual = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fechaActual); 
    }
    
}
