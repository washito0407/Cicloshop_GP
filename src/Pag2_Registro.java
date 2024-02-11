import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag2_Registro {
    private JButton REGRESARButton;
    private JTextField rgnombretextField1;
    private JTextField rgapellidotextField2;
    private JTextField rgcedulatextField3;
    private JTextField rgdirecciontextField4;
    private JButton REGISTRARSEButton;
    public JPanel registro;
    static JFrame frameRegistro = new JFrame("Registro");

    public Pag2_Registro() {
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameRegistro.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        REGISTRARSEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Te has registrado correctamente (Prueba)");
                frameRegistro.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
    }
}
