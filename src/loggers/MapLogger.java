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

    //if the constructor get empty string, the logger will print to system.out
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
    //must call save function when finish
    public void save(){
        this.printWriter.close();
    }
}
