import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Pag1_Inicio.frame.setContentPane(new Pag1_Inicio().login);
        Pag1_Inicio.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Pag1_Inicio.frame.setSize(600, 500);
        Pag1_Inicio.frame.setVisible(true);
        Pag1_Inicio.frame.setLocationRelativeTo(null);

    }
}