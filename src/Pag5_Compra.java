import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag5_Compra {
    private JTable table1;
    private JButton FACTURARButton;
    private JButton REGRESARButton;
    private JFormattedTextField formattedTextField1;
    private JButton SELECCIONARButton;
    public JPanel pag5CompraPanel;
    static JFrame frameCompra = new JFrame("Compra");

    public Pag5_Compra() {
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCompra.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        FACTURARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCompra.setVisible(false);
                Pag5i1_Factura.frameFacturaCompra.setContentPane(new Pag5i1_Factura().pag5FacturaPanel);
                Pag5i1_Factura.frameFacturaCompra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag5i1_Factura.frameFacturaCompra.setSize(800, 800);
                Pag5i1_Factura.frameFacturaCompra.setVisible(true);
                Pag5i1_Factura.frameFacturaCompra.setLocationRelativeTo(null);
            }
        });
    }
}
