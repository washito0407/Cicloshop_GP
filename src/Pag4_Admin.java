import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pag4_Admin {
    JPanel admin_pag;
    private JButton cerrarButton;
    private JLabel logo;
    private JButton productosButton;
    private JButton facturasButton;
    private JButton cajerosButton;
    private JPanel barraTop;
    static JFrame frameAdminP = new JFrame("CICLOSHOP");
    int xMouse, yMouse;

    public Pag4_Admin() {
        cerrarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        productosButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        facturasButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cajerosButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        barraTop.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xMouse = e.getX();
                yMouse = e.getY();
            }
        });
        barraTop.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                frameAdminP.setLocation(x - xMouse,y - yMouse);
            }
        });
        cerrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.setVisible(false);
                if (!Pag4i1_Ingreso.frameIngresoP.isUndecorated()){
                    Pag4i1_Ingreso.frameIngresoP.setUndecorated(true);
                }
                Pag4i1_Ingreso.frameIngresoP.setContentPane(new Pag4i1_Ingreso().Ingreso);
                Pag4i1_Ingreso.frameIngresoP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag4i1_Ingreso.frameIngresoP.setSize(900, 500);
                Pag4i1_Ingreso.frameIngresoP.setVisible(true);
                Pag4i1_Ingreso.frameIngresoP.setLocationRelativeTo(null);
            }
        });
        cajerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.setVisible(false);
                if (!Pag4i2_Eliminar.frameEliminarP.isUndecorated()){
                    Pag4i2_Eliminar.frameEliminarP.setUndecorated(true);
                }
                Pag4i2_Eliminar.frameEliminarP.setContentPane(new Pag4i2_Eliminar().Eliminar_u);
                Pag4i2_Eliminar.frameEliminarP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag4i2_Eliminar.frameEliminarP.setSize(800, 300);
                Pag4i2_Eliminar.frameEliminarP.setVisible(true);
                Pag4i2_Eliminar.frameEliminarP.setLocationRelativeTo(null);
            }
        });
        facturasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAdminP.setVisible(false);
                if (! Pag4i3_FacturasG.frameFacturasP.isUndecorated()){
                    Pag4i3_FacturasG.frameFacturasP.setUndecorated(true);
                }
                Pag4i3_FacturasG.frameFacturasP.setContentPane(new Pag4i3_FacturasG().pag4Facturas);
                Pag4i3_FacturasG.frameFacturasP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag4i3_FacturasG.frameFacturasP.setSize(800, 500);
                Pag4i3_FacturasG.frameFacturasP.setVisible(true);
                Pag4i3_FacturasG.frameFacturasP.setLocationRelativeTo(null);
            }
        });
    }
}
