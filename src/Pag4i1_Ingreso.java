import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag4i1_Ingreso {
    public JPanel Ingreso;
    private JButton regresarButton;
    private JLabel logo;
    private JTable table1;
    private JButton modificarButton;
    private JButton ingresarButtonU;
    private JButton eliminarButton;
    static JFrame frameIngresoP = new JFrame("Ingreso de Productos");

    public Pag4i1_Ingreso() {
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameIngresoP.setVisible(false);
                Pag4_Admin.frameAdminP.setVisible(true);
            }
        });
    }

    private void createUIComponents() {
        table1 = new JTable();
    }
}
