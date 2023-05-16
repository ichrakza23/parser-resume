package com.parser;


import java.io.*;

import gate.*;
import gate.creole.SerialAnalyserController;
import gate.util.*;
import gate.util.persistence.PersistenceManager;

public class Annie {

	private CorpusController annieController;

	public void initAnnie() throws GateException, IOException {
//		File gateHome = Gate.getGateHome();
//		File annieGapp = new File(gateHome, "ANNIEResumeParser.gapp");
		// initialise the GATE library
		 
	annieController = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController",
		    Factory.newFeatureMap(), Factory.newFeatureMap(), "ANNIE_" + Gate.genSym());
	
}

	public void setCorpus(Corpus corpus) {
		annieController.setCorpus(corpus);
	}

	public void execute() throws GateException {
		annieController.execute();
	}
}
