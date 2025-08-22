package util.ui;

import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Helpers UI (brindan una serie de características a nivel de UI. Como por
 * ejemplo : efectos de foco , efectos de textos tipo burburja (tooltip), entre
 * otras muchas características , ect ... En este caso estaremos usandolo para
 * generar efecto de foco "colo azul suave" , efecto de mensajes tipo
 * burburja(tooltip) y efecto placeholder , cuando colocamos el mouse dentro de
 * los campos en donde designemos dichos helpers.
 */
public final class UIHelpers {

    private UIHelpers() {
    }

    public static void attachHintAndFocusColor(final JTextComponent comp, final String tooltip) {
        if (comp == null) {
            return;
        }
        comp.setToolTipText(tooltip);
        final Color defaultBg = comp.getBackground();
        final Color focusBg = new Color(229, 243, 255);

        comp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                comp.setBackground(focusBg);
            }

            @Override
            public void focusLost(FocusEvent e) {
                comp.setBackground(defaultBg);
            }
        });
    }

    /**
     * Attach placeholder (texto gris). No muestra placeholder si componente no
     * es editable o no está enabled. Gestiona focus y cambios en
     * editable/enabled.
     */
    public static void attachPlaceholder(final JTextComponent comp, final String placeholder) {
        if (comp == null || placeholder == null) {
            return;
        }

        final Color defaultFg = comp.getForeground();
        final Color placeholderFg = new Color(150, 150, 150);

        comp.putClientProperty("ui.placeholder.text", placeholder);
        comp.putClientProperty("ui.placeholder.defaultFg", defaultFg);
        comp.putClientProperty("ui.placeholder.placeholderFg", placeholderFg);

        // Mostrar placeholder (usa bypass del DocumentFilter)
        Runnable showPlaceholder = () -> {
            if (!comp.isEditable() || !comp.isEnabled()) {
                comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
                return;
            }
            String cur = comp.getText();
            if (cur == null || cur.isEmpty()) {
                setTextBypassFilter(comp, placeholder);      // <- Aquí evitamos el filtro
                comp.setForeground(placeholderFg);
                comp.putClientProperty("ui.placeholder.visible", Boolean.TRUE);
            } else {
                comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
                comp.setForeground(defaultFg);
            }
        };

        // Ocultar placeholder (cuando el usuario comienza a editar)
        Runnable hidePlaceholder = () -> {
            Boolean visible = (Boolean) comp.getClientProperty("ui.placeholder.visible");
            if (visible != null && visible) {
                // dejar el documento vacío; esto sí debe pasar por el filtro (vacío suele permitirse)
                comp.setText("");
                comp.setForeground(defaultFg);
                comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
            }
        };

        // Focus listener
        comp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                hidePlaceholder.run();
            }

            @Override
            public void focusLost(FocusEvent e) {
                String txt = comp.getText();
                if (txt == null || txt.isEmpty()) {
                    showPlaceholder.run();
                } else {
                    // si quedó exactamente el placeholder (caso raro), restauramos fg
                    String ph = (String) comp.getClientProperty("ui.placeholder.text");
                    if (ph != null && ph.equals(txt)) {
                        comp.setForeground(placeholderFg);
                        comp.putClientProperty("ui.placeholder.visible", Boolean.TRUE);
                    } else {
                        comp.setForeground(defaultFg);
                        comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
                    }
                }
            }
        });

        // Reaccionar a cambios de editable/enabled
        comp.addPropertyChangeListener("editable", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean ed = Boolean.TRUE.equals(evt.getNewValue());
                if (!ed) {
                    comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
                    comp.setForeground(defaultFg);
                    String cur = comp.getText();
                    String ph = (String) comp.getClientProperty("ui.placeholder.text");
                    if (ph != null && ph.equals(cur)) {
                        // limpiar texto placeholder si estaba
                        comp.setText("");
                    }
                } else {
                    String cur = comp.getText();
                    if (cur == null || cur.isEmpty()) {
                        showPlaceholder.run();
                    }
                }
            }
        });

        comp.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean en = Boolean.TRUE.equals(evt.getNewValue());
                if (!en) {
                    comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
                    comp.setForeground(defaultFg);
                    String cur = comp.getText();
                    String ph = (String) comp.getClientProperty("ui.placeholder.text");
                    if (ph != null && ph.equals(cur)) {
                        comp.setText("");
                    }
                } else {
                    String cur = comp.getText();
                    if (cur == null || cur.isEmpty()) {
                        showPlaceholder.run();
                    }
                }
            }
        });

        // Inicial
        String current = comp.getText();
        if (current == null || current.isEmpty()) {
            showPlaceholder.run();
        } else {
            comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
            comp.setForeground(defaultFg);
        }
    }

    /**
     * Devuelve el texto real del componente; si muestra placeholder devuelve
     * cadena vacía.
     */
    public static String getText(final JTextComponent comp) {
        if (comp == null) {
            return "";
        }
        Object vis = comp.getClientProperty("ui.placeholder.visible");
        if (vis instanceof Boolean && (Boolean) vis) {
            return "";
        }
        String t = comp.getText();
        return t == null ? "" : t;
    }

    /**
     * Revalida el estado del placeholder (útil tras setText programático).
     */
    public static void updatePlaceholderState(final JTextComponent comp) {
        if (comp == null) {
            return;
        }
        String placeholder = (String) comp.getClientProperty("ui.placeholder.text");
        Color defaultFg = (Color) comp.getClientProperty("ui.placeholder.defaultFg");
        Color placeholderFg = (Color) comp.getClientProperty("ui.placeholder.placeholderFg");
        if (placeholder == null) {
            return;
        }
        if (!comp.isEditable() || !comp.isEnabled()) {
            comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
            comp.setForeground(defaultFg != null ? defaultFg : comp.getForeground());
            return;
        }

        String cur = comp.getText();
        if (cur == null || cur.isEmpty()) {
            setTextBypassFilter(comp, placeholder);
            comp.setForeground(placeholderFg != null ? placeholderFg : new Color(150, 150, 150));
            comp.putClientProperty("ui.placeholder.visible", Boolean.TRUE);
        } else {
            if (placeholder.equals(cur)) {
                comp.setForeground(placeholderFg != null ? placeholderFg : new Color(150, 150, 150));
                comp.putClientProperty("ui.placeholder.visible", Boolean.TRUE);
            } else {
                comp.setForeground(defaultFg != null ? defaultFg : comp.getForeground());
                comp.putClientProperty("ui.placeholder.visible", Boolean.FALSE);
            }
        }
    }

    /**
     * Escribe texto en el componente SIN que el DocumentFilter actual lo
     * procese. Esto permite escribir placeholders que contienen caracteres no
     * aceptados por filtros.
     */
    private static void setTextBypassFilter(final JTextComponent comp, final String text) {
        if (comp == null) {
            return;
        }

        javax.swing.text.Document doc = comp.getDocument();
        if (doc instanceof javax.swing.text.AbstractDocument) {
            javax.swing.text.AbstractDocument ad = (javax.swing.text.AbstractDocument) doc;
            // IMPORTANTE: usar el tipo javax.swing.text.DocumentFilter (no tu clase utilitaria DocumentFilters)
            javax.swing.text.DocumentFilter currentFilter = ad.getDocumentFilter();
            try {
                // desactivamos temporalmente para que setText no sea filtrado
                ad.setDocumentFilter(null);
                comp.setText(text);
            } finally {
                // restauramos el filtro original (puede ser null)
                ad.setDocumentFilter(currentFilter);
            }
        } else {
            comp.setText(text);
        }
    }
}
