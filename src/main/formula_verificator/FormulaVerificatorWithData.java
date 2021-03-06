package main.formula_verificator;

import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.declareanalyzer.DeclareAnalyzerSingleTracePlugin;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;

import java.util.ArrayList;
import java.util.Date;


public class FormulaVerificatorWithData {

    // the plugin for checking the conformance of a trace
    private DeclareAnalyzerSingleTracePlugin analyzer = new DeclareAnalyzerSingleTracePlugin();
    private DeclareMap declareModel = null;
    private String declarePath = "";

    // reads the declare model
    private DeclareMap getModel(String fileName) {
        if (!declarePath.equals(fileName)) {
            declarePath = fileName;
            AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(fileName);
            AssignmentModel model = broker.readAssignment();
            declareModel = new DeclareMap(model, null, null, null, broker, null);
        }
        return declareModel;
    }

    // returns whether the trace is conformant
    // analyzer does the actual analyzing and returns a boolean -> true if trace is conformant
    public boolean verifyTrace(String modelFile, String traceId, ArrayList<String> activities, ArrayList<String> groups, ArrayList<Date> times) {
        DeclareMap model = getModel(modelFile);
        XTrace trace = Tester.genXtrace(traceId, activities, groups, times);

        return analyzer.analyze(trace, model);
    }

    // returns whether the trace is conformant
    // analyzer does the actual analyzing and returns a boolean -> true if trace is conformant
    public boolean verifyTraceWithTime(String modelFile,
                               String traceId,
                               ArrayList<String> activities,
                               ArrayList<String> groups,
                               ArrayList<String> elapsed_times,
                               ArrayList<Date> times) {
        DeclareMap model = getModel(modelFile);
        XTrace trace = Tester.genXtraceWithTime(traceId, activities, groups, elapsed_times, times);

        return analyzer.analyze(trace, model);
    }
}
