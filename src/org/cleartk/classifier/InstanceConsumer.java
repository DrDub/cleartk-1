/** 
 * Copyright (c) 2009, Regents of the University of Colorado 
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
package org.cleartk.classifier;

import org.cleartk.CleartkException;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * <p>
 * 
 * @author Steven Bethard
 * @author Philip Ogren
 */
public interface InstanceConsumer<OUTCOME_TYPE> {

	public static final String PARAM_ANNOTATION_HANDLER = "org.cleartk.classifier.InstanceConsumer.PARAM_ANNOTATION_HANDLER";

	/**
	 * Consume the instance and return the outcome (classification) assigned to
	 * the instance. If the consumer does not assign outcomes to instances (e.g.
	 * a training data consumer), this method should return null.
	 * 
	 * @param instance
	 *            The instance (features and label) to be consumed.
	 * @return The assigned outcome (classification), or null if an outcome was
	 *         not assigned.
	 */
	public OUTCOME_TYPE consume(Instance<OUTCOME_TYPE> instance) throws CleartkException;

	/**
	 * This method provides an annotation handler (or anything else using an
	 * InstanceConsumer) a way to determine whether or not the instance consumer
	 * expects the Instances it consumes to have outcomes. For example, if your
	 * instance consumer corresponds to a training data writer, then it will
	 * expect outcomes. However, if your instance consumer is
	 * ClassifierAnnotator, then it will not expect outcomes and therefore your
	 * annotation handler should not bother worrying about whether the instances
	 * is passes to the consume method have outcomes or not.
	 * 
	 * This method says nothing about whether values are returned from consume.
	 * 
	 * @return true if the consumer expects the classification Instances to have
	 *         outcomes, and false otherwise.
	 */
	public abstract boolean expectsOutcomes();
}
