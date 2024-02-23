import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag4_Admin {
    JPanel admin_pag;
    private JButton cerrarButton;
    private JLabel logo;
    private JButton ingresarProductosButton;
    private JButton facturasButton;
    private JButton eliminarProductosButton;
    static JFrame frameAdminP = new JFrame("CICLOSHOP");

    public Pag4_Admin() {
        cerrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        ingresarProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.setVisible(false);
                Pag4i1_Ingreso.frameIngresoP.setContentPane(new Pag4i1_Ingreso().Ingreso);
                Pag4i1_Ingreso.frameIngresoP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag4i1_Ingreso.frameIngresoP.setSize(950, 800);
                Pag4i1_Ingreso.frameIngresoP.setVisible(true);
                Pag4i1_Ingreso.frameIngresoP.setLocationRelativeTo(null);
            }
        });
        eliminarProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.setVisible(false);
                Pag4i2_Eliminar.frameEliminarP.setContentPane(new Pag4i2_Eliminar().Eliminar_u);
                Pag4i2_Eliminar.frameEliminarP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag4i2_Eliminar.frameEliminarP.setSize(800, 800);
                Pag4i2_Eliminar.frameEliminarP.setVisible(true);
                Pag4i2_Eliminar.frameEliminarP.setLocationRelativeTo(null);
            }
        });
        facturasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.setVisible(false);
                Pag4i3_FacturasG.frameFacturasP.setContentPane(new Pag4i3_FacturasG().pag4Facturas);
                Pag4i3_FacturasG.frameFacturasP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag4i3_FacturasG.frameFacturasP.setSize(800, 800);
                Pag4i3_FacturasG.frameFacturasP.setVisible(true);
                Pag4i3_FacturasG.frameFacturasP.setLocationRelativeTo(null);
            }
        });
    }
}
