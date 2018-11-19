package main.formula_verificator;

import org.deckfour.xes.in.*;
import org.deckfour.xes.model.XLog;

import java.io.File;

public class XLogReader {
    public static XLog openLog(String inputLogFileName) throws Exception {
        XLog log = null;
        XParser parser;

        String lowercaseName = inputLogFileName.toLowerCase();

        if (lowercaseName.contains("mxml.gz")) {
            parser = new XMxmlGZIPParser();
        } else if (lowercaseName.contains("mxml") || lowercaseName.contains("xml")) {
            parser = new XMxmlParser();
        } else if (lowercaseName.contains("xes.gz")) {
            parser = new XesXmlGZIPParser();
        } else if (lowercaseName.contains("xes")) {
            parser = new XesXmlParser();
        } else {
            throw new Exception("File extension not recognized");
        }

        if (parser.canParse(new File(inputLogFileName))) {
            try {
                log = parser.parse(new File(inputLogFileName)).get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (log == null) {
            throw new Exception("Parsed log is null");
        }

        return log;
    }
}
