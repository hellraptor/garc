package control;

import javax.swing.*;

/**
 * Created by Svyatoslav_Yakovlev on 6/13/2016.
 */
public class ControlPanel {

    private JFrame frame;

    private JLabel w1;
    private JLabel w0;
    private JLabel w2;


    public void show() {
        // создаём главное окно приложения
        frame = new JFrame("Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // чтобы процесс завершался после закрытия окна

        JPanel panel = new JPanel();
        //добавление границы к панели
        panel.setBorder(BorderFactory.createTitledBorder("Wheels"));
        w0 = new JLabel("wheel 0:                ");
        panel.add(w0);
        w1 = new JLabel("wheel 1:                ");
        panel.add(w1);
        w2 = new JLabel("wheel 2:                ");
        panel.add(w2);
        //Добавление панели к фрейму
        frame.getContentPane().add(panel);
        //метод рack(); сообщает Swing о том,
        //что нужно придать компонентам необходимые размеры для
        //правильного помещения их в форму.
        //Другой способ - вызвать setSize(int width, int height).
        frame.setSize(400,200);
        frame.setVisible(true); // отображаем окно
    }

    public void updateWheelInfo(float w0, float w1, float w2) {
        this.w0.setText("wheel 0:" + w0);
        this.w1.setText("wheel 1:" + w1);
        this.w2.setText("wheel 2:" + w2);

    }

    public void close() {
        frame.dispose();
    }


}
