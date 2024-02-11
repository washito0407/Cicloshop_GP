import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pag1_Inicio {
    public JPanel login;
    private JButton LOGINButton;
    private JButton REGISTROButton;
    static JFrame frame = new JFrame("CICLOSHOP");

    public Pag1_Inicio() {
        REGISTROButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Pag2_Registro.frameRegistro.setVisible(true);
                Pag2_Registro.frameRegistro.setContentPane(new Pag2_Registro().registro);
                Pag2_Registro.frameRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag2_Registro.frameRegistro.setSize(600,400);
                Pag2_Registro.frameRegistro.setLocationRelativeTo(null);
            }
        });
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Pag3_Login.frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag3_Login.frameLogin.setContentPane(new Pag3_Login().baselogin);
                Pag3_Login.frameLogin.setSize(500,700);
                Pag3_Login.frameLogin.setVisible(true);
                Pag3_Login.frameLogin.setLocationRelativeTo(null);
            }
        });
    }
}
