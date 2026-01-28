package vista.components;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

/**
 * PaginationPanel Panel reutilizable con botones de navegación y controles de
 * tamaño/ir a página.
 */
public class PaginationPanel extends JPanel {

    private final JButton btnFirst;
    private final JButton btnPrev;
    private final JButton btnNext;
    private final JButton btnLast;
    private final JLabel lblInfo;
    private final JComboBox<Integer> cboPageSize;
    private final JTextField txtGoTo;
    private final JButton btnGo;

    public PaginationPanel() {

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnFirst = new JButton("|<");
        btnPrev = new JButton("<");
        lblInfo = new JLabel("Página 1 de 1");
        btnNext = new JButton(">");
        btnLast = new JButton(">|");
        cboPageSize = new JComboBox<>(new Integer[]{10, 20, 50, 100});
        cboPageSize.setSelectedItem(20);
        txtGoTo = new JTextField(4);
        btnGo = new JButton("Ir");

        add(btnFirst);
        add(btnPrev);
        add(lblInfo);
        add(btnNext);
        add(btnLast);
        add(new JLabel(" | Tamaño:"));
        add(cboPageSize);
        add(new JLabel(" | Ir a:"));
        add(txtGoTo);
        add(btnGo);

    }

    // Listeners (expuestos para que el Frame conecte la lógica)
    public void onFirst(ActionListener l) {
        btnFirst.addActionListener(l);
    }

    public void onPrev(ActionListener l) {
        btnPrev.addActionListener(l);
    }

    public void onNext(ActionListener l) {
        btnNext.addActionListener(l);
    }

    public void onLast(ActionListener l) {
        btnLast.addActionListener(l);
    }

    public void onPageSize(ActionListener l) {
        cboPageSize.addActionListener(l);
    }

    public void onGoTo(ActionListener l) {
        btnGo.addActionListener(l);
    }

    public void setInfo(String text) {
        lblInfo.setText(text);
    }

    public int getSelectedPageSize() {
        return (Integer) cboPageSize.getSelectedItem();
    }

    public String getGoToText() {
        return txtGoTo.getText().trim();
    }

    public void clearGoTo() {
        txtGoTo.setText("");
    }

    public void enableAll(boolean enabled) {
        btnFirst.setEnabled(enabled);
        btnPrev.setEnabled(enabled);
        btnNext.setEnabled(enabled);
        btnLast.setEnabled(enabled);
        cboPageSize.setEnabled(enabled);
        txtGoTo.setEnabled(enabled);
        btnGo.setEnabled(enabled);
    }

}
