package control;

import javax.swing.*;

/**
 * Created by Svyatoslav_Yakovlev on 6/13/2016.
 */
public class ControlPanel {

    public JButton button1;
    private JFrame frame;

    public void show() {
        // создаём главное окно приложения
        frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // чтобы процесс завершался после закрытия окна
        // создаём кнопку
        button1 = new JButton("Button 1");
        frame.add(button1); // добавляем кнопку на окно
        frame.pack(); // автоматически настраиваем размер окна под содержимое
        frame.setVisible(true); // отображаем окно
    }

    public void close() {
        frame.dispose();
    }


}
