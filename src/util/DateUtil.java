package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String obtenerFechaActual() {
        Date fechaActual = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fechaActual);
    }

    public static String[] obtenerFechaFormateadaTituloReporte(Date fechaInicio, Date fechaFin) {
        // Formateador para el nombre del mes
        SimpleDateFormat mesFormat = new SimpleDateFormat("MMMM");
        // Formateador para el día del mes
        SimpleDateFormat diaFormat = new SimpleDateFormat("d");
        // Formateador para el año
        SimpleDateFormat anioFormat = new SimpleDateFormat("yyyy");

        // Construir los strings con el formato deseado
        String fechaInicioStr = "DEL " + diaFormat.format(fechaInicio) + "° DE " + mesFormat.format(fechaInicio).toUpperCase() + " DEL " + anioFormat.format(fechaInicio);
        String fechaFinStr = diaFormat.format(fechaFin) + " DE " + mesFormat.format(fechaFin).toUpperCase() + " DEL " + anioFormat.format(fechaFin);
        return new String[] {fechaInicioStr, fechaFinStr};
    }

}
