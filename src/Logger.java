import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Logger implements Observer {
    BufferedWriter br;

    public Logger(String fileName) {
        try {
            this.br = new BufferedWriter(new FileWriter(fileName));
        }catch (IOException e) {
            System.out.println("Error while create Map Logger");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            this.br.write(arg.toString());
        } catch (IOException e) {
            System.out.println("Error while write to file in Map Logger");
        }
    }

    public void save() {
        try {
            this.br.close();
        } catch (IOException e) {
            System.out.println("Error while save map Logger");
        }
    }
}
