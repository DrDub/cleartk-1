/** 
 * Copyright (c) 2010, Regents of the University of Colorado 
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

package org.cleartk.classifier.feature.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * <br>Copyright (c) 2010, Regents of the University of Colorado 
 * <br>All rights reserved.
 *
 * @author Philip Ogren
 *
 */

public class NumericTypeUtilTest {

	@Test
	public void testContainsDigits() throws Exception {
		assertTrue(NumericTypeUtil.containsDigits("1"));
		assertTrue(NumericTypeUtil.containsDigits("1aa1"));
		assertTrue(NumericTypeUtil.containsDigits("11234"));
		assertTrue(NumericTypeUtil.containsDigits("oo0"));
		assertTrue(NumericTypeUtil.containsDigits("5-!@#$!@#$"));
		assertTrue(NumericTypeUtil.containsDigits("!@#$!@#$4"));
		assertTrue(NumericTypeUtil.containsDigits("    asdf asd 4 asdff aasdf "));
		assertFalse(NumericTypeUtil.containsDigits("l"));
		assertFalse(NumericTypeUtil.containsDigits("aa"));
		assertFalse(NumericTypeUtil.containsDigits("asdfasdf"));
		assertFalse(NumericTypeUtil.containsDigits("ooO"));
		assertFalse(NumericTypeUtil.containsDigits("!@#$!@#$"));
		assertFalse(NumericTypeUtil.containsDigits("    asdf asd asdff aasdf "));
		assertFalse(NumericTypeUtil.containsDigits(""));
	}
	
	@Test
	public void testIsDigits() throws Exception {
		assertTrue(NumericTypeUtil.isDigits("1"));
		assertFalse(NumericTypeUtil.isDigits("1aa1"));
		assertTrue(NumericTypeUtil.isDigits("11234"));
		assertFalse(NumericTypeUtil.isDigits("oo0"));
		assertFalse(NumericTypeUtil.isDigits("5-!@#$!@#$"));
		assertFalse(NumericTypeUtil.isDigits("!@#$!@#$4"));
		assertFalse(NumericTypeUtil.isDigits("    asdf asd 4 asdff aasdf "));
		assertTrue(NumericTypeUtil.isDigits("0123456789"));
	}

}
