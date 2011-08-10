/** 
 * Copyright (c) 2011, Regents of the University of Colorado 
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
package org.cleartk.classifier.baseline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * 
 * <br>
 * Copyright (c) 2011, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Steven Bethard
 */
public abstract class MostFrequentValueClassifierBuilder<OUTCOME_TYPE> extends
    SingleOutcomeClassifierBuilder<OUTCOME_TYPE> {

  @Override
  public void trainClassifier(File dir, String... args) throws Exception {
    Map<OUTCOME_TYPE, Integer> counts = new HashMap<OUTCOME_TYPE, Integer>();
    for (String outcomeString : Files.readLines(this.getTrainingDataFile(dir), Charsets.US_ASCII)) {
      OUTCOME_TYPE outcome = this.parseOutcome(outcomeString);
      Integer count = counts.get(outcome);
      counts.put(outcome, count == null ? 0 : count + 1);
    }
    OUTCOME_TYPE maxOutcome = null;
    int maxCount = Integer.MIN_VALUE;
    for (OUTCOME_TYPE outcome : counts.keySet()) {
      int count = counts.get(outcome);
      if (count > maxCount) {
        maxCount = count;
        maxOutcome = outcome;
      }
    }
    this.writeOutcome(dir, maxOutcome);
  }
}
