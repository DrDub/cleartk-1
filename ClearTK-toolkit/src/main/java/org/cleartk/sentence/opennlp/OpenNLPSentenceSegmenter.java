 /** 
 * Copyright (c) 2007-2008, Regents of the University of Colorado 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 * Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
*/
package org.cleartk.sentence.opennlp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.maxent.MaxentModel;
import opennlp.maxent.io.SuffixSensitiveGISModelReader;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.type.Sentence;


/**
 * <br>Copyright (c) 2007-2008, Regents of the University of Colorado 
 * <br>All rights reserved.

 *
 * @author Philip Ogren
 *
 *This sentence segmenter is a simple wrapper around the OpenNLP SentenceDetector with 
 *additional sentence detection added that handles multiple newlines (i.e. if multiple
 *newlines appear (separated only by whitespace) together, then this is treated as 
 *a sentence delimiter.  
 *
 * @see SentenceDetector
 */

public class OpenNLPSentenceSegmenter extends JCasAnnotator_ImplBase
{ 
	/**
	 * "org.cleartk.sentence.opennlp.OpenNLPSentenceSegmenter.PARAM_SENTENCE_MODEL_FILE"
	 * is a single, required, string parameter that provides the path of the
	 * OpenNLP sentence segmenter model file, e.g.
	 *   resources/models/OpenNLP.Sentence.English.bin.gz
	 * @see opennlp.maxent.io.SuffixSensitiveGISModelReader
	 */
	public static final String PARAM_SENTENCE_MODEL_FILE = "org.cleartk.sentence.opennlp.OpenNLPSentenceSegmenter.PARAM_SENTENCE_MODEL_FILE";
	public static final String multipleNewlinesRegex = "\\s*\\n\\s*\\n\\s*";

	SentenceDetector sentenceDetector;
	Pattern multipleNewlinesPattern;
	Pattern leadingWhitespacePattern;
	Pattern trailingWhitespacePattern;
	@Override
	public void initialize(UimaContext uimaContext) throws ResourceInitializationException
	{
		super.initialize(uimaContext);
		try {
			String sentenceModelFile = (String) uimaContext.getConfigParameterValue(PARAM_SENTENCE_MODEL_FILE);
			MaxentModel model = new SuffixSensitiveGISModelReader(new File(sentenceModelFile)).getModel();
			sentenceDetector = new SentenceDetectorME(model);
			multipleNewlinesPattern = Pattern.compile(multipleNewlinesRegex, Pattern.MULTILINE | Pattern.DOTALL);
			leadingWhitespacePattern = Pattern.compile("^\\s+");
			trailingWhitespacePattern = Pattern.compile("\\s+$");
		}catch(IOException ioe) {
			throw new ResourceInitializationException(ioe);
		}
	}
	
	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException 
	{
		String text = jCas.getDocumentText();
		List<Integer> sentenceOffsets = getSentenceOffsets(text);

		int begin = 0;
		int end = 0;

		//advance the first sentence to first non-whitespace char
		Matcher matcher;
		matcher = leadingWhitespacePattern.matcher(text);
		if(matcher.find()) {
			begin+= matcher.group().length();
		}

		for(Integer offset : sentenceOffsets) {
			end = offset;  //offset is really the beginning of the next sentence so we may adjust this below.
			String sentenceText = text.substring(begin, end);
			//it is possible that there is a duplicate offset or that getSentenceOffsets returned the first sentence offset 
			if(sentenceText.trim().length() > 0) {
				matcher = trailingWhitespacePattern.matcher(sentenceText);
				if(matcher.find()) {
					end -= matcher.group().length();
				}
				Sentence sentence = new Sentence(jCas, begin,end);
				sentence.addToIndexes();
			}
			begin = offset; //we need to advance begin regardless of whether a sentence was created.
		}
		//take the remaining text if there is any and add it to a sentence.  
		//this code will not execute if the text ends with a sentence detected by
		//SentenceDetector because it actually returns an offset corresponding to the end
		//of the last sentence (see note on getSentenceOffsets)
		if(begin < text.length()) {
			String sentenceText = text.substring(begin, text.length());
			end = text.length();
			if(sentenceText.trim().length() > 0) {
				matcher = trailingWhitespacePattern.matcher(sentenceText);
				if(matcher.find()) {
					end -= matcher.group().length();
				}
				Sentence sentence = new Sentence(jCas, begin, end);
				sentence.addToIndexes();
			}
		}
	}

	/**
	 * returns a list of the beginnings of sentences - except (possibly) the 
	 * first sentence - from both the OpenNLP sentence detector and the 
	 * multiple newlines regex. It is possible for this method to return 
	 * duplicate values.  The sentence detector will return an offset
	 * corresponding to the end of the text if the last non-whitespace character
	 * was classified an end of sentence character.  
	 */ 
	private List<Integer> getSentenceOffsets(String text) {
		Matcher matcher = multipleNewlinesPattern.matcher(text);
		List<Integer> offsets = new ArrayList<Integer>();
		while(matcher.find()) {
			offsets.add(matcher.end());
		}
		
		int[] sentenceOffsetsML = sentenceDetector.sentPosDetect(text);
		for(int i=0; i<sentenceOffsetsML.length; i++) {
			offsets.add(sentenceOffsetsML[i]);
		}
		Collections.sort(offsets);
		return offsets;
	}
}