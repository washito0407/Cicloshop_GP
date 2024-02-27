import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag3_Login {
    public JPanel baselogin;
    private JTextField nombreTextField;
    private JPasswordField passwordField;
    private JButton INGRESARButton;
    private JButton REGRESARButton;
    static JFrame frameLogin = new JFrame("Login CICLOSHOP");
    ConexionDB conexionDB = new ConexionDB();

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
                try {
                    Connection connection = conexionDB.ConexionLocal();
                    boolean usuarioEncontrado=false;

                    //SQL para la tabla de Usuarios
                    PreparedStatement ps = connection.prepareStatement("SELECT nombre_usr, password_usr FROM Usuarios");
                    ResultSet resultSetUsuarios = ps.executeQuery();
                    while (resultSetUsuarios.next()&&!usuarioEncontrado){
                        if (resultSetUsuarios.getString("nombre_usr").equals(nombreTextField.getText())
                                && resultSetUsuarios.getString("password_usr").equals(String.valueOf(passwordField.getPassword())))
                        {
                            usuarioEncontrado=true;
                            frameLogin.dispose();
                            Pag5_Compra.frameCompra.setContentPane(new Pag5_Compra().pag5CompraPanel);
                            Pag5_Compra.frameCompra.setSize(1200,700);
                            Pag5_Compra.frameCompra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            Pag5_Compra.frameCompra.setVisible(true);
                            Pag5_Compra.frameCompra.setLocationRelativeTo(null);
                        }
                    }

                    //SQL para la tabla de Administradores
                    PreparedStatement psAdmins = connection.prepareStatement("SELECT nombre_adm, password_adm FROM Administradores");
                    ResultSet resultSetAdmins= psAdmins.executeQuery();
                    while (resultSetAdmins.next()&&!usuarioEncontrado){
                        if (resultSetAdmins.getString("nombre_adm").equals(nombreTextField.getText())
                                && resultSetAdmins.getString("password_adm").equals(String.valueOf(passwordField.getPassword())))
                        {
                            usuarioEncontrado=true;
                            frameLogin.dispose();
                            Pag4_Admin.frameAdminP.setContentPane(new Pag4_Admin().admin_pag);
                            Pag4_Admin.frameAdminP.setSize(800,600);
                            Pag4_Admin.frameAdminP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            Pag4_Admin.frameAdminP.setVisible(true);
                            Pag4_Admin.frameAdminP.setLocationRelativeTo(null);
                        }
                    }
                    if (!usuarioEncontrado){
                        JOptionPane.showMessageDialog(null,"Ingrese datos correctos");
                        nombreTextField.setText("");
                        passwordField.setText("");
                    }
                } catch (Exception exception){
                    System.out.println(exception.getMessage());
                }


            }
        });
    }
}
