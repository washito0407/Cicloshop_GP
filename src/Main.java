import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CICLOSHOP");
        //frame.setContentPane(new Pag3_Login().baselogin);
        //frame.setContentPane(new Pag2_Registro().registro);
        frame.setContentPane(new Pag1_Inicio().login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.pack();
        frame.setVisible(true);
    }
}