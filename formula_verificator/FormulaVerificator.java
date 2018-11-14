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

//    private static XEvent traceViolatedEvent(Formula formula, XTrace trace){
//
//        String ltlFormula = formula.getLTLFormula();
//        List<ltl2aut.formula.Formula> formulaeParsed = new ArrayList<ltl2aut.formula.Formula>();
//        boolean violated = true;
//        XEvent event = null;
//
//        try {
//            formulaeParsed.add(new DefaultParser(ltlFormula).parse());
//            TreeFactory<ConjunctionTreeNode, ConjunctionTreeLeaf> treeFactory = DefaultTreeFactory.getInstance();
//            ConjunctionFactory<? extends GroupedTreeConjunction> conjunctionFactory = GroupedTreeConjunction
//                    .getFactory(treeFactory);
//            GroupedTreeConjunction conjunction = conjunctionFactory.instance(formulaeParsed);
//            Automaton aut = conjunction.getAutomaton().op.reduce();
//            ExecutableAutomaton execAut = new ExecutableAutomaton(aut);
//            execAut.ini();
//            PossibleNodes current = null;
//
//            if (formula instanceof SimpleFormula)
//            {
//                XEvent lastEvent=null;
//                for(XEvent e : trace)
//                {
//                    lastEvent=e;
//                    String label = ((XAttributeLiteral) e.getAttributes().get("concept:name")).getValue();
//                    violated = true;
//                    current = execAut.currentState();
//                    if(current!=null && !(current.get(0)==null))
//                    {
//                        for (Transition out : current.output())
//                        {
//                            if (out.parses(label))
//                            {
//                                violated = false;
//                                break;
//                            }
//                        }
//                    }
//                    if(!violated)
//                    {
//                        execAut.next(label);
//                    }
//                    else
//                    {
//                        event=e;
//                        break;
//                    }
//                    current = execAut.currentState();
//                    lastEvent=e;
//                }
//                if(!violated)
//                {
//                    if(current.isAccepting())
//                    {
//                        violated = false;
//                    }
//                    else
//                    {
//                        violated = true;
//                        event=lastEvent;
//                    }
//                }
//
//            }
//
//
//        } catch (SyntaxParserException e1) {
//            e1.printStackTrace();
//        }
//        if(!violated)
//        {
//            return null;
//        }
//        return event;
//    }


//    public static Boolean isFormulaVerified(  XTrace trace, Vector<Formula> formulas){
//		boolean violated = false;
//		for (Formula formula : formulas) {
//			violated = violated || isTraceViolated(formula, trace);
//		}
//		return new Boolean(!violated);
//	}

//	private static Long eventSatisfactionTime(Formula formula,XTrace trace){
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//		XEvent event;
//		Date violation=null;
//		event=traceViolatedEvent(formula, trace);
//		Formula notFormula=new SimpleFormula("!( "+formula.getLTLFormula()+" )");
//		if(event!=null) //if the formula is violated save the timestamp
//		{
//			try {
//					violation= (Date) format.parseObject(event.getAttributes().get(XTimeExtension.KEY_TIMESTAMP).toString());
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			return violation.getTime();
//		}
//
//		else	//else check the violation on (not formula) and save the timestamp
//		{
//			event=traceViolatedEvent(notFormula, trace);
//
//			if(event!=null) //if the formula is violated save the timestamp
//			{
//				try {
//						violation= (Date) format.parseObject(event.getAttributes().get(XTimeExtension.KEY_TIMESTAMP).toString());
//
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				return violation.getTime();
//			}
//		}
//		//otherwise return the last event
//		try {
//			violation = (Date) format.parseObject(trace.get(trace.size()-1).getAttributes().get(XTimeExtension.KEY_TIMESTAMP).toString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return violation.getTime();
//	}

//	public static Long eventViolationTime(XTrace trace, Vector<Formula> formulas){
//		List<Long> formulaTime = new ArrayList<>();
//
//		for (Formula formula : formulas) {
//			formulaTime.add(eventSatisfactionTime(formula,trace));
//		}
//
//		Long min=null;
//		for(Long time: formulaTime)
//		{
//			if(min==null)
//			{
//				min=time;
//			}
//			else if(time<min)
//			{
//				min=time;
//			}
//		}
//		return min;
//	}
}
