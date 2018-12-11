package main.formula_verificator;

import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.declareanalyzer.DeclareAnalyzerSingleTracePlugin;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Tester {

    public static void main(String[] args) throws Exception {
//		String logFile = "/media/sf_SharedFolder/logs/xes/vasyl_log_processed.xes.gz";
        String modelFile = "/media/sf_SharedFolder/declaremodels/accommodated_models/vasyl_log_test_set_converted_trial.xml";

//		XParser parser = new XesXmlGZIPParser();
//		XLog log = parser.parse(new File(logFile)).get(0);

        DeclareMap model = getModel(modelFile);

        DeclareAnalyzerSingleTracePlugin analyzer = new DeclareAnalyzerSingleTracePlugin();
//		for (XTrace trace : log) {
//			analyzer.analyze(trace, model);
//		}

        String traceId = "Trace 1";
        ArrayList<String> activities = new ArrayList<>(Arrays.asList("A1", "A2", "A4"));
        ArrayList<String> groups = new ArrayList<>(Arrays.asList("G1", "G1", "G3"));
        ArrayList<Date> times = new ArrayList<>(Arrays.asList(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2017/06/22 16:33:31"),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2017/06/22 17:33:31"),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2017/06/22 18:33:31")));
        XTrace trace = genXtrace(traceId, activities, groups, times);

        analyzer.analyze(trace, model);
    }

    private static DeclareMap getModel(String fileName) {
        AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(fileName);
        AssignmentModel model = broker.readAssignment();
        return new DeclareMap(model, null, null, null, broker, null);
    }

    // Generate XTrace from lists of attributes
    public static XTrace genXtrace(String traceId, ArrayList<String> activities, ArrayList<String> groups, ArrayList<Date> times) {

        XTrace newTrace = XFactoryRegistry.instance().currentDefault().createTrace();
        XAttributeMap attributeMap = XFactoryRegistry.instance().currentDefault().createAttributeMap();
        XAttribute theTraceId = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("concept:name", traceId, null);
        attributeMap.put("concept:name", theTraceId);
        newTrace.setAttributes(attributeMap);

        for (int i = 0; i < activities.size(); i++) {
            XEvent newEvent = XFactoryRegistry.instance().currentDefault().createEvent();
            XAttributeMap newAttributeMap = XFactoryRegistry.instance().currentDefault().createAttributeMap();
            XAttribute newAttribute1 = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("concept:name", activities.get(i), null);
            XAttribute newAttribute2 = XFactoryRegistry.instance().currentDefault().createAttributeTimestamp("time:timestamp", times.get(i), null);
            XAttribute newAttribute3 = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("org:resource", groups.get(i), null);
            XAttribute newAttribute4 = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("lifecycle:transition", "complete", null);
            newAttributeMap.put("concept:name", newAttribute1);
            newAttributeMap.put("time:timestamp", newAttribute2);
            newAttributeMap.put("org:resource", newAttribute3);
            newAttributeMap.put("lifecycle:transition", newAttribute4);
            newEvent.setAttributes(newAttributeMap);
            newTrace.add(newEvent);
        }

        return newTrace;
    }

    // Generate XTrace from lists of attributes
    public static XTrace genXtraceWithTime(String traceId, ArrayList<String> activities, ArrayList<String> groups, ArrayList<String> elapsed_times, ArrayList<Date> times) {

        XTrace newTrace = XFactoryRegistry.instance().currentDefault().createTrace();
        XAttributeMap attributeMap = XFactoryRegistry.instance().currentDefault().createAttributeMap();
        XAttribute theTraceId = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("concept:name" , traceId, null);
        attributeMap.put("concept:name", theTraceId);
        newTrace.setAttributes(attributeMap);

        for (int i = 0; i < activities.size(); i++){
            XEvent newEvent = XFactoryRegistry.instance().currentDefault().createEvent();
            XAttributeMap newAttributeMap = XFactoryRegistry.instance().currentDefault().createAttributeMap();
            XAttribute newAttribute1 = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("concept:name" , activities.get(i), null);
            XAttribute newAttribute2 = XFactoryRegistry.instance().currentDefault().createAttributeTimestamp("time:timestamp" , times.get(i), null);
            XAttribute newAttribute3 = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("org:resource" , groups.get(i), null);
            XAttribute newAttribute4 = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("elapsedtime:seconds" , elapsed_times.get(i), null);
            //XAttribute newAttribute5 = XFactoryRegistry.instance().currentDefault().createAttributeLiteral("lifecycle:transition" , "complete", null);
            newAttributeMap.put("concept:name", newAttribute1);
            newAttributeMap.put("time:timestamp", newAttribute2);
            newAttributeMap.put("org:resource", newAttribute3);
            newAttributeMap.put("elapsedtime:seconds", newAttribute4);
            //newAttributeMap.put("lifecycle:transition", newAttribute5);
            newEvent.setAttributes(newAttributeMap);
            newTrace.add(newEvent);
        }

        return newTrace;
    }
}
