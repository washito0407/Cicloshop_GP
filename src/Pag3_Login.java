import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag3_Login {
    public JPanel baselogin;
    private JTextField nombreTextField;
    private JPasswordField passwordField;
    private JButton INGRESARButton;
    private JButton REGRESARButton;
    static JFrame frameLogin = new JFrame("Login CICLOSHOP");
    public Pag3_Login() {
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameLogin.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        INGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioAdmin = "admin";
                String passAdmin = "admin";
                String usuarioCajero = "cajero";
                String passCajero = "123";
                if (usuarioAdmin.equals(nombreTextField.getText()) && passAdmin.equals(String.valueOf(passwordField.getPassword()))){
                    frameLogin.dispose();
                    Pag4_Admin.frameAdminP.setContentPane(new Pag4_Admin().admin_pag);
                    Pag4_Admin.frameAdminP.setSize(900,700);
                    Pag4_Admin.frameAdminP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Pag4_Admin.frameAdminP.setVisible(true);
                    Pag4_Admin.frameAdminP.setLocationRelativeTo(null);
                } else if (usuarioCajero.equals(nombreTextField.getText()) && passCajero.equals(String.valueOf(passwordField.getPassword()))){
                    frameLogin.dispose();
                    Pag5_Compra.frameCompra.setContentPane(new Pag5_Compra().pag5CompraPanel);
                    Pag5_Compra.frameCompra.setSize(900,700);
                    Pag5_Compra.frameCompra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Pag5_Compra.frameCompra.setVisible(true);
                    Pag5_Compra.frameCompra.setLocationRelativeTo(null);
                }else {
                    JOptionPane.showMessageDialog(null,"Ingrese datos correctos");
                    nombreTextField.setText("");
                    passwordField.setText("");
                }
            }
        });
    }
}
