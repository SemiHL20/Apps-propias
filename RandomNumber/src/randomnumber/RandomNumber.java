package randomnumber;
import java.util.Random;
import javax.swing.JOptionPane;
/**
 *
 * @author Jose Miguel Lopez Sanchez
 */
public class RandomNumber {

    public static void main(String[] args) {
        String user_num = JOptionPane.showInputDialog("Introduce el número límite");
        Random rn = new Random();
        
        int num = Integer.parseInt(user_num);
        int res = rn.nextInt(1, num);
        
        JOptionPane.showMessageDialog(null, "Resultado: "+res);
        //System.out.println(res);
    }   
}
