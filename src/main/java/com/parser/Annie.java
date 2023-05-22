package com.parser;


import java.io.IOException;

import gate.Corpus;
import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

public class Annie {

	private CorpusController annieController;

	public void initAnnie() throws GateException, IOException {
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
