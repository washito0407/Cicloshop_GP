import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag4i2_Eliminar {
    public JPanel Eliminar_u;
    private JButton regresarButton;
    private JLabel icono;
    private JTable table1;
    private JButton editarButton;
    private JButton eliminarButton;
    static JFrame frameEliminarP = new JFrame("Eliminar Productos");

    public Pag4i2_Eliminar() {
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameEliminarP.setVisible(false);
                Pag4_Admin.frameAdminP.setVisible(true);
            }
        });
    }
}
