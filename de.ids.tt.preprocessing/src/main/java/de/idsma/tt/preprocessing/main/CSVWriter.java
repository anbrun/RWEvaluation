package de.idsma.tt.preprocessing.main;

import org.apache.uima.fit.descriptor.ConfigurationParameter;

public class CSVWriter {
	public static final String PARAM_OUTPUTDIR = "Output directory for csvData";
	@ConfigurationParameter(name = PARAM_OUTPUTDIR, defaultValue = "csvData", description = "Output directcory for csvData")
	private String outputdir;
	
	/*
	//instead of .txt maybe .csv?
	private void writeSamplesToFiles(JCas aJCas, String doc) throws Exception {
		Iterator<Sample> samples = JCasUtil.iterator(aJCas, Sample.class);
		while (samples.hasNext()) {
			Sample s = samples.next();

			String filename = this.outputdir + File.separator + doc + "_" + s.getId() + ".txt";
			this.writeFile(filename, s.getCoveredText());
		}
	}	
	*/
}
