package loggers;

import tools.Map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class MapLogger implements Observer {
    PrintWriter printWriter;

    public MapLogger(String fileName){
        if(!fileName.isEmpty()) {
            try {
                this.printWriter = new PrintWriter(new FileOutputStream(fileName));
            } catch (Exception e) {
                System.err.println("Error while create map logger file");
            }
        }
        else {
            this.printWriter = new PrintWriter(System.out);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.printWriter.println(arg.toString());
    }
    public void save(){
        this.printWriter.close();
    }
}
