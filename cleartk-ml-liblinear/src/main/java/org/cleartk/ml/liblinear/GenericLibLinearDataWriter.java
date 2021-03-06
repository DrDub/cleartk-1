/*
 * Copyright (c) 2013, Regents of the University of Colorado 
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
package org.cleartk.ml.liblinear;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import org.cleartk.ml.CleartkProcessingException;
import org.cleartk.ml.jar.DataWriter_ImplBase;
import org.cleartk.ml.liblinear.encoder.FeatureNodeArrayEncoder;

import de.bwaldvogel.liblinear.FeatureNode;

/**
 * <br>
 * Copyright (c) 2013, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Steven Bethard
 */
public abstract class GenericLibLinearDataWriter<CLASSIFIER_BUILDER_TYPE extends GenericLibLinearClassifierBuilder<? extends GenericLibLinearClassifier<OUTCOME_TYPE>, OUTCOME_TYPE>, OUTCOME_TYPE>
    extends DataWriter_ImplBase<CLASSIFIER_BUILDER_TYPE, FeatureNode[], OUTCOME_TYPE, Integer> {

  public GenericLibLinearDataWriter(File outputDirectory) throws FileNotFoundException {
    super(outputDirectory);
    this.setFeaturesEncoder(new FeatureNodeArrayEncoder());
  }

  @Override
  protected void writeEncoded(FeatureNode[] features, Integer outcome)
      throws CleartkProcessingException {
    this.trainingDataWriter.print(outcome);
    for (FeatureNode featureNode : features) {
      this.trainingDataWriter.format(Locale.US, " %d:%.7f", featureNode.index, featureNode.value);
    }
    this.trainingDataWriter.println();
  }
}
