package de.idsma.tt.preprocessing.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.util.CasIOUtil;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
public class Preprocessing {
	
	/*
	public static void readCas (CAS aCas, File aFile) throws IOException {
		String lowerCaseFileName = aFile.getName().toLowerCase();
		if (lowerCaseFileName.endsWith(".xmi")){
			CasIOUtil.readXmi(aCas, aFile);
		} else {
			throw new IllegalArgumentException("Unknown file extension: [" + aFile + "]");
		}
	}
	
	public static void readXmi (CAS aCas, File aFile) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(aFile);
			XmiCasDeserializer.deserialize(inputStream, aCas);
		} catch (SAXException e) {
			IOException ioexcep = new IOException(e.getMessage());
			ioexcep.initCause(e);
			throw ioexcep;	
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	*/
	// E4 DI OBJECTS
	
	//prints the tokenized text
	static String[] printTokenize (String[] tokensArr) {
        for (String token : tokensArr) {
            System.out.format("[%s]", token);
        }
        System.out.println();
        return tokensArr;
    }

	private void processXMIFiles() {
		/*
		int dirCounter = 0;
		int indirCounter = 0;
		int freeIndirCounter = 0;
		int repCounter = 0;
		*/
		
		
		int dirTokensCounter = 0;
		int inDirTokensCounter = 0;
		int freeIndirTokensCounter = 0;
		int repTokensCounter = 0;
		int documentTokensCounter = 0;
		
		int frameID = 0;
		int intExprID = 0;
		int stwrID = 0;
		
		String s = "";
		//File f = new File ("annotatedFiles/erz");
		//File f = new File ("annotatedFiles/zeit");
		File f = new File ("annotatedFiles/famz");
		//File f = new File ("annotatedFiles/valta");
	    //File f = new File ("annotatedFiles/peterek");
		//File f = new File ("annotatedFiles/konsens");

		File[] inputDir = f.listFiles();
	
		for (File xmiFile : inputDir) {
			if (xmiFile.getName().endsWith(".xmi"))
			{
				//System.out.print(xmiFile.getName() + "     ");
				TypeSystemDescription tsd;
				tsd = TypeSystemDescriptionFactory.createTypeSystemDescription(new File ("annotatedFiles/redeWiedergabeTypesystem_compare").toString());
			 	try {
			 		CAS cas = CasCreationUtils.createCas(tsd, null, null);
					try {
						CasIOUtil.readCas(cas, xmiFile);
						//documentTokensCounter+=cas.getDocumentText().length();
						//System.out.println("The document has: " + documentTokensCounter + " tokens.");
						
						/*
						int dirTokensCounter = 0;
						int inDirTokensCounter = 0;
						int freeIndirTokensCounter = 0;
						int repTokensCounter = 0;
						int documentTokensCounter = 0;
						*/
						/*
						int dirCounter = 0;
						int indirCounter = 0;
						int freeIndirCounter = 0;
						int repCounter = 0;
						*/
						
						Type stwrType = cas.getTypeSystem().getType("de.idsma.rw.Stwr");	
						Type intExprType = cas.getTypeSystem().getType("de.idsma.rw.IntExpression");
						Type frameType = cas.getTypeSystem().getType("de.idsma.rw.Frame");
						
						Feature mediumFeat = stwrType.getFeatureByBaseName("Medium");
						Feature rTypeFeat = stwrType.getFeatureByBaseName("RType");
						Feature stwrIdFeat = stwrType.getFeatureByBaseName("StwrID");
						Feature frameIdFeat = frameType.getFeatureByBaseName("FrameID");
						Feature intExprIdFeat = intExprType.getFeatureByBaseName("IntExpressionID");
						Feature posFeat = frameType.getFeatureByBaseName("Pos");
						
						
						//Pattern p = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
						
						String [] tokensArray =  printTokenize(cas.getDocumentText().split(
								"\\s+"));
				
						
						documentTokensCounter += tokensArray.length;
						
						
						//counts the total amount of STWR for each text genre (not medium-specific)
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList) {
							if (a.getFeatureValueAsString(rTypeFeat).equals("direct"))
							{
								String [] dirTokens = printTokenize(a.getCoveredText().split(
										//"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
										"\\s+"));
								dirTokensCounter+=dirTokens.length;
							}	
							else if (a.getFeatureValueAsString(rTypeFeat).equals("indirect"))
							{
								String [] inDirTokens =  printTokenize(a.getCoveredText().split("\\s+"));
								inDirTokensCounter+=inDirTokens.length;
							}
							else if (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect"))
							{
								String [] freeInDirTokens =  printTokenize(a.getCoveredText().split("\\s+"));
								freeIndirTokensCounter+=freeInDirTokens.length;
							}
							else if (a.getFeatureValueAsString(rTypeFeat).equals("reported"))
							{
								String [] repTokens = printTokenize(a.getCoveredText().split("\\s+"));
								repTokensCounter+=repTokens.length;
							}
						}
						
						
						/*
						//counts the amount of RTypes (not medium-specific) in tokens per file
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList) {
							if (a.getFeatureValueAsString(rTypeFeat).equals("direct"))
							{
								String [] dirTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								dirTokensCounter+=dirTokens.length;
							}	
							else if (a.getFeatureValueAsString(rTypeFeat).equals("indirect"))
							{
								String [] inDirTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								inDirTokensCounter+=inDirTokens.length;
							}
							else if (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect"))
							{
								String [] freeInDirTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								freeIndirTokensCounter+=freeInDirTokens.length;
							}
							else if (a.getFeatureValueAsString(rTypeFeat).equals("reported"))
							{
								String [] repTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								repTokensCounter+=repTokens.length;
							}
						}
						
						System.out.print(dirTokensCounter + "     ");
						System.out.print(inDirTokensCounter + "     ");
						System.out.print(freeIndirTokensCounter + "     ");
						System.out.print(repTokensCounter + "     ");
						System.out.print(documentTokensCounter + "     ");
						System.out.println();
						*/
						/*
						//counts the total amount of RTypes (medium-specific) per file
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList){
							if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("direct")))
							{
								dirCounter++;
							}	
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("indirect")))
							{
								indirCounter++;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect")))
							{
								freeIndirCounter++;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("reported")))
							{
								repCounter++;
							}
						}
						
					 	System.out.println("The total amount of direct writing: " + dirCounter);
					 	System.out.println("The total amount of indirect writing: " + indirCounter);
					 	System.out.println("The total amount of freeIndirect writing: " + freeIndirCounter);
					 	System.out.println("The total amount of reported writing: " + repCounter);
						*/
						
						/*
						//counts the amount of STWR (medium-specific) in tokens per file
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList) {
							if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("direct")))
							{
								String [] dirTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								dirTokensCounter+=dirTokens.length;
							}	
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("indirect")))
							{
								String [] inDirTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								inDirTokensCounter+=inDirTokens.length;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect")))
							{
								String [] freeInDirTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								freeIndirTokensCounter+=freeInDirTokens.length;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("reported")))
							{
								String [] repTokens = a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])");
								repTokensCounter+=repTokens.length;
							}
						}
						
						System.out.println(dirTokensCounter);
						System.out.println(inDirTokensCounter);
						System.out.println(freeIndirTokensCounter);
						System.out.println(repTokensCounter);
						*/
						
						/*
						//counts the total amount of STWR for each text genre in tokens
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList) {
							if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("direct")))
							{
								String [] dirTokens = tokenize (a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])"));
								dirTokensCounter+=dirTokens.length;
							}	
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("indirect")))
							{
								String [] inDirTokens = tokenize (a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])"));
								inDirTokensCounter+=inDirTokens.length;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect")))
							{
								String [] freeInDirTokens = tokenize (a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])"));
								freeIndirTokensCounter+=freeInDirTokens.length;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("reported")))
							{
								String [] repTokens = tokenize (a.getCoveredText().split(
										"(?=(?!^)[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])|(?<=[>\\.\\!\\?\"»«\\s,\\;_\\:\\(\\)\\[\\]\\-\\'<\\„])"));
								repTokensCounter+=repTokens.length;
							}
						}
						*/
						
						/*
						// print annotated intExpr for a certain pos
						ArrayList <AnnotationFS> frameList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(frameType)){
							frameList.add(anno);
						}
						
						ArrayList <AnnotationFS> intExprList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(intExprType)){
							intExprList.add(anno);
						}
						
						ArrayList <AnnotationFS> stwrList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							stwrList.add(anno);
						}
						
						for (AnnotationFS a : frameList) {
							frameID = Integer.parseInt(a.getFeatureValueAsString(frameIdFeat));
							for (AnnotationFS b : intExprList)
							{
								intExprID = Integer.parseInt(b.getFeatureValueAsString(intExprIdFeat));
								for (AnnotationFS c : stwrList)
								{
									stwrID = Integer.parseInt(c.getFeatureValueAsString(stwrIdFeat));
									if ((frameID == intExprID) && (frameID == stwrID) && a.getFeatureValueAsString(posFeat).equals("start") && c.getFeatureValueAsString(mediumFeat).equals("speech")
										&& c.getFeatureValueAsString(rTypeFeat).equals("direct")){
											System.out.print(b.getCoveredText() + ", ");
									}
								}
							}
						}
						*/
	
						/*
						//counts the total amount of RTypes (medium-specific) per file
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList){
							if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("direct")))
							{
								dirCounter++;
							}	
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("indirect")))
							{
								indirCounter++;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect")))
							{
								freeIndirCounter++;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("reported")))
							{
								repCounter++;
							}
						}
						
					 	System.out.println(dirCounter);
					 	System.out.println(indirCounter);
					 	System.out.println(freeIndirCounter);
					 	System.out.println(repCounter);
						*/
						
						/*
						//counts the total amount of STWR for each text genre
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList) {
							if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("direct")))
							{
								dirTokensCounter+=a.getCoveredText().length();
							}	
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("indirect")))
							{
								inDirTokensCounter+=a.getCoveredText().length();
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect")))
							{
								freeIndirTokensCounter+=a.getCoveredText().length();
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("reported")))
							{
								repTokensCounter+=a.getCoveredText().length();
							}
						}
						*/
				
						/*
						//counts the total amount of rTypes (medium-specific) per STWR for each text genre	 
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(stwrType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList){
							if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("direct")))
							{
								dirCounter++;
							}	
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("indirect")))
							{
								indirCounter++;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("freeIndirect")))
							{
								freeIndirCounter++;
							}
							else if ((a.getFeatureValueAsString(mediumFeat).equals("writing")) && (a.getFeatureValueAsString(rTypeFeat).equals("reported")))
							{
								repCounter++;
							}
						}
						*/
						
						/*
						// print annotated intExpr
						ArrayList <AnnotationFS> annoList= new ArrayList <AnnotationFS>();
						for (AnnotationFS anno :  cas.getAnnotationIndex(intExprType)){
							annoList.add(anno);
						}
						
						for (AnnotationFS a : annoList) {
							System.out.println(a.getCoveredText());
						}
						*/
						
						//hier engines aufrufen
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (ResourceInitializationException e) {
					e.printStackTrace();
					}	
			 	
			 	/*
			 	System.out.println("Direct writing: " + dirCounter);
			 	System.out.println("Indirect writing: " + indirCounter);
			 	System.out.println("FreeIndirect writing: " + freeIndirCounter);
			 	System.out.println("Reported writing: " + repCounter);
			 	*/
			 	
			 	
			 	System.out.println("The total amount of tokens in each document is: " + documentTokensCounter);
				System.out.println("The total amount of tokens in direct is: " + dirTokensCounter);
				System.out.println("The total amount of tokens in indirect is: " + inDirTokensCounter);
				System.out.println("The total amount of tokens in freeIndirect is: " + freeIndirTokensCounter);
				System.out.println("The total amount of tokens in reported is: " + repTokensCounter);
				
			 }
			else {
				continue;
			} 
			}
	}
	
	public static void main(String[] args) {
		Preprocessing xmiReader = new Preprocessing();
		xmiReader.processXMIFiles();
	}
}
