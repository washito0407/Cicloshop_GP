import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag5i1_Factura {
    private JTable table1;
    private JFormattedTextField formattedTextField1;
    private JButton FACTURARButton;
    private JButton GENERARFACTURAButton;
    public JPanel pag5FacturaPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton regresarButton;
    static JFrame frameFacturaCompra = new JFrame("Compra facturada");

    public Pag5i1_Factura() {
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameFacturaCompra.setVisible(false);
                Pag5_Compra.frameCompra.setVisible(true);
            }
        });
    }
}
