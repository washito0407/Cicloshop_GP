import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag4i3_FacturasG {
    private JButton REGRESARButton;
    private JTable table1;
    private JButton MOSTRARButton;
    private JButton IMPRIMIRButton;
    private JButton ELIMINARButton;
    public JPanel pag4Facturas;
    static JFrame frameFacturasP = new JFrame("Facturas");

    public Pag4i3_FacturasG() {
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameFacturasP.setVisible(false);
                Pag4_Admin.frameAdminP.setVisible(true);
            }
        });
    }
}
