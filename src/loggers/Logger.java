package loggers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
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

public class Logger implements Observer {
    XMLGenerator xmlGenerator;
    String fileName;

    public Logger(String fileName) {
        this.xmlGenerator = new XMLGenerator();
        this.fileName = fileName;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.xmlGenerator.newEntery(arg.toString());
    }

    public void save() {
        this.xmlGenerator.save(this.fileName);
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
                System.out.println("Error while create xml");
            }
        }

        void newEntery(String str) {
            this.queue.offer(str);
        }
        //this function continue to run while the programe is running, and create real-time xml fields
        void management() {
            synchronized (this.locker) {
                while (!stop || !this.queue.isEmpty()) {
                    try {
                        String entery = this.queue.take();
                        if (entery.isEmpty()) {
                            this.stop = true;
                            continue;
                        }
                        Point curPoint = getCurrentPoint(entery);
                        if (this.currentPoint == null || !this.currentPoint.isSameLocation(curPoint)) {
                            newPoint(curPoint);
                        }
                        entery = entery.substring(entery.indexOf(')')+1).trim(); //remove the current location of the agent and leading spaces (trim())
                        if (entery.startsWith(LoggerMessageMaker.CHECK)) {
                            entery = entery.replaceAll("[^0-9]+", " ");
                            List<String> nums = new LinkedList<>();
                            nums = Arrays.asList(entery.trim().split(" "));
                            this.addCheck(new Point(Integer.parseInt(nums.get(0)), Integer.parseInt(nums.get(1))));
                            continue;
                        }
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
        void newPoint(Point p) {
            this.currentPointElemnt = this.doc.createElement("Point");
            this.rootElement.appendChild(this.currentPointElemnt);
            Attr attr = doc.createAttribute("location");
            attr.setValue(getPointAsString(p));
            this.currentPointElemnt.setAttributeNode(attr);
            this.currentPoint = p;
        }

        void addCheck(Point p) {
            Element checkPoint = this.doc.createElement("Check");
            checkPoint.appendChild(doc.createTextNode(getPointAsString(p)));
            this.currentPointElemnt.appendChild(checkPoint);
        }

        void addCompare(Point a, Point b) {
            Element checkPoint = this.doc.createElement("Compare");
            checkPoint.appendChild(doc.createTextNode(getPointAsString(a) + "," + getPointAsString(b)));
            this.currentPointElemnt.appendChild(checkPoint);
        }


        void save(String fileName) {
            newEntery("");
            synchronized (this.locker) {
                try {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(this.doc);
                    StreamResult result = new StreamResult(new File(fileName));
                    transformer.transform(source, result);

                } catch (Exception e) {
                    System.out.println("Error while saving XML file");
                }
            }
        }

        Point getCurrentPoint(String str) {
            str = str.replaceAll("[^0-9]+", " ");
            List<String> nums = new LinkedList<>();
            nums = Arrays.asList(str.trim().split(" "));
            return new Point(Integer.parseInt(nums.get(0)), Integer.parseInt(nums.get(1)));
        }


        String getPointAsString(Point p) {
            return ("("+String.valueOf(p.getX()) + "," + String.valueOf(p.getY())+")");
        }

    }
}
