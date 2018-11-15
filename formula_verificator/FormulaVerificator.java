package formula_verificator;

import formula_verificator.form.SimpleFormula;
import ltl2aut.automaton.Automaton;
import ltl2aut.automaton.Transition;
import ltl2aut.formula.DefaultParser;
import ltl2aut.formula.conjunction.*;
import ltl2aut.ltl.SyntaxParserException;
import org.processmining.plugins.declareminer.ExecutableAutomaton;
import org.processmining.plugins.declareminer.PossibleNodes;

import java.util.ArrayList;
import java.util.List;


public class FormulaVerificator {

    // returns true if trace violated
    public static boolean isTraceViolated(String formula, ArrayList<String> trace) {
        return traceViolatedEventSimplified(formula, trace) != null;
    }


    private static String traceViolatedEventSimplified(String formula, ArrayList<String> trace) {

        // The next line basically doesn't do anything. It takes a String formula and saves it as String ltlFormula
        String ltlFormula = new SimpleFormula(formula).getLTLFormula();
        List<ltl2aut.formula.Formula> formulaeParsed = new ArrayList<>();
        boolean violated = true;
        String event = null;

        try {
            formulaeParsed.add(new DefaultParser(ltlFormula).parse());
            TreeFactory<ConjunctionTreeNode, ConjunctionTreeLeaf> treeFactory = DefaultTreeFactory.getInstance();
            ConjunctionFactory<? extends GroupedTreeConjunction> conjunctionFactory = GroupedTreeConjunction
                    .getFactory(treeFactory);
            GroupedTreeConjunction conjunction = conjunctionFactory.instance(formulaeParsed);
            Automaton aut = conjunction.getAutomaton().op.reduce();
            ExecutableAutomaton execAut = new ExecutableAutomaton(aut);
            execAut.ini();
            PossibleNodes current = null;

            String lastEvent = null;
            for (String e : trace) {
                violated = true;
                current = execAut.currentState();
                if (current != null && !(current.get(0) == null)) {
                    for (Transition out : current.output()) {
                        if (out.parses(e)) {
                            violated = false;
                            break;
                        }
                    }
                }
                if (!violated) {
                    execAut.next(e);
                } else {
                    event = e;
                    break;
                }
                current = execAut.currentState();
                lastEvent = e;
            }
            if (!violated) {
                if (current.isAccepting()) {
                    violated = false;
                } else {
                    violated = true;
                    event = lastEvent;
                }
            }


        } catch (SyntaxParserException e1) {
            e1.printStackTrace();
        }
        if (!violated) {
            return null;
        }
        return event;
    }
}
