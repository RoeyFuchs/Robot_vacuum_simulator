package loggers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import tools.Map;
import tools.Point;

public class Logger implements PropertyChangeListener {
    XMLGenerator xmlGenerator;
    Result result;

    public Logger(Result result) {
        this.xmlGenerator = new XMLGenerator(); //save log as xml
        this.result = result; //stream (file or system.out)
    }

    public void save() {
        this.xmlGenerator.save(this.result);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.xmlGenerator.newEntery(evt.getNewValue().toString());
    }

    private class XMLGenerator {
        Element rootElement;
        Document doc;
        Element currentPointElemnt;
        Point currentPoint;
        private BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        boolean stop = false;
        Object locker = new Object();

        public XMLGenerator() {
            this.create();
            Thread trd = new Thread(() -> this.management());
            trd.start();
        }

        //example from https://www.tutorialspoint.com/java_xml/java_dom_create_document.htm
        void create() {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                this.doc = dBuilder.newDocument();

                //root element
                this.rootElement = this.doc.createElement("path");
                doc.appendChild(this.rootElement);

            } catch (Exception e) {
                System.err.println("Error while create xml");
            }
        }

        //add new entry to queue
        void newEntery(String str) {
            this.queue.offer(str);
        }
        //this function continue to run while the program is running, and create real-time xml fields
        void management() {
            synchronized (this.locker) {
                while (!stop || !this.queue.isEmpty()) {
                    try {
                        String entery = this.queue.take();
                        if (entery.isEmpty()) { //if get an empty entry, that mean that we finished
                            this.stop = true;
                            continue;
                        }
                        Point curPoint = getCurrentPoint(entery);
                        if (this.currentPoint == null || !this.currentPoint.isSameLocation(curPoint)) {
                            newPoint(curPoint);
                        }
                        entery = entery.substring(entery.indexOf(')')+1).trim(); //remove the current location of the agent and leading spaces (trim())
                        //check if the entry is for checking
                        if (entery.startsWith(LoggerMessageMaker.CHECK)) {
                            entery = entery.replaceAll("[^0-9]+", " ");
                            List<String> nums = new LinkedList<>();
                            nums = Arrays.asList(entery.trim().split(" "));
                            this.addCheck(new Point(Integer.parseInt(nums.get(0)), Integer.parseInt(nums.get(1))));
                            continue;
                        }
                        //check if the entry is for comparing
                        if (entery.startsWith(LoggerMessageMaker.COMPARE)) {
                            entery = entery.replaceAll("[^0-9]+", " ");
                            List<String> nums = new LinkedList<>();
                            nums = Arrays.asList(entery.trim().split(" "));
                            this.addCompare(new Point(Integer.parseInt(nums.get(0)), Integer.parseInt(nums.get(1))), new Point(Integer.parseInt(nums.get(2)), Integer.parseInt(nums.get(3))));
                            continue;
                        }
                    } catch (InterruptedException e) {
                        continue;
                    }
                }
            }
        }
        //the function create new current point
        void newPoint(Point p) {
            this.currentPointElemnt = this.doc.createElement("Point");
            this.rootElement.appendChild(this.currentPointElemnt);
            Attr attr = doc.createAttribute("location");
            attr.setValue(getPointAsString(p));
            this.currentPointElemnt.setAttributeNode(attr);
            this.currentPoint = p;
        }

        //add check attribute
        void addCheck(Point p) {
            Element checkPoint = this.doc.createElement("Check");
            checkPoint.appendChild(doc.createTextNode(getPointAsString(p)));
            this.currentPointElemnt.appendChild(checkPoint);
        }

        //add compare attribute
        void addCompare(Point a, Point b) {
            Element checkPoint = this.doc.createElement("Compare");
            checkPoint.appendChild(doc.createTextNode(getPointAsString(a) + "," + getPointAsString(b)));
            this.currentPointElemnt.appendChild(checkPoint);
        }


        //saving the xml. must call this function
        void save(Result result) {
            newEntery("");
            synchronized (this.locker) {
                try {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(this.doc);
                    transformer.transform(source, result);
                } catch (Exception e) {
                    System.err.println("Error while saving XML file");
                }
            }
        }

        //get point by string
        Point getCurrentPoint(String str) {
            str = str.replaceAll("[^0-9]+", " ");
            List<String> nums = new LinkedList<>();
            nums = Arrays.asList(str.trim().split(" "));
            return new Point(Integer.parseInt(nums.get(0)), Integer.parseInt(nums.get(1)));
        }


        //get string of point from point object
        String getPointAsString(Point p) {
            return ("("+String.valueOf(p.getX()) + "," + String.valueOf(p.getY())+")");
        }

    }
}
