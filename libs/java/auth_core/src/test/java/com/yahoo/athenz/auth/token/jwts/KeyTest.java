/*
 * Copyright The Athenz Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yahoo.athenz.auth.token.jwts;

import org.testng.annotations.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import static org.testng.Assert.*;

public class KeyTest {

    @Test
    public void testRSAKey() throws NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException {
        Key key = new Key();
        key.setKid("0");
        key.setAlg("RS256");
        key.setKty("RSA");
        key.setUse("sig");
        key.setE("AQAB");
        key.setN("AMV3cnZXxYJL-A0TYY8Fy245HKSOBCYt9atNAUQVtbEwx9QaZGj8moYIe4nXgx72Ktwg0Gruh8sS7GQLBizCXg7fCk62sDV_MZINnwON9gsKbxxgn9mLFeYSaatUzk-VRphDoHNIBC-qeDtYnZhsHYcV9Jp0GPkLNquhN1TXA7gT");

        assertNotNull(key.getPublicKey());

        assertEquals(key.getKid(), "0");
        assertEquals(key.getAlg(), "RS256");
        assertEquals(key.getKty(), "RSA");
        assertEquals(key.getUse(), "sig");
        assertEquals(key.getE(), "AQAB");
        assertEquals(key.getN(), "AMV3cnZXxYJL-A0TYY8Fy245HKSOBCYt9atNAUQVtbEwx9QaZGj8moYIe4nXgx72Ktwg0Gruh8sS7GQLBizCXg7fCk62sDV_MZINnwON9gsKbxxgn9mLFeYSaatUzk-VRphDoHNIBC-qeDtYnZhsHYcV9Jp0GPkLNquhN1TXA7gT");
    }

    @Test
    public void testECKey() throws NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException {
        Key key = new Key();
        key.setKid("eckey1");
        key.setAlg("ES256");
        key.setKty("EC");
        key.setUse("sig");
        key.setCrv("prime256v1");
        key.setX("AI0x6wEUk5T0hslaT83DNVy5r98XnG7HAjQynjCrcdCe");
        key.setY("ATdV2ebpefqBli_SXZwvL3-7OiD3MTryGbR-zRSFZ_s=");

        assertNotNull(key.getPublicKey());

        assertEquals(key.getKid(), "eckey1");
        assertEquals(key.getAlg(), "ES256");
        assertEquals(key.getKty(), "EC");
        assertEquals(key.getUse(), "sig");
        assertEquals(key.getCrv(), "prime256v1");
        assertEquals(key.getX(), "AI0x6wEUk5T0hslaT83DNVy5r98XnG7HAjQynjCrcdCe");
        assertEquals(key.getY(), "ATdV2ebpefqBli_SXZwvL3-7OiD3MTryGbR-zRSFZ_s=");
    }

    @Test
    public void testUnknownKey() {

        Key key = new Key();
        key.setKty("ATHENZ");

        try {
            key.getPublicKey();
            fail();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidParameterSpecException ex) {
            assertTrue(ex instanceof NoSuchAlgorithmException);
        }
    }

    @Test
    public void testParseRSAKey() {

        final String rsaKey = "{\n" +
                "            \"kty\":\"RSA\",\n" +
                "                \"kid\":\"0\",\n" +
                "                \"alg\":\"RS256\",\n" +
                "                \"use\":\"sig\",\n" +
                "                \"n\":\"AMV3cnZXxYJL-A0TYY8Fy245HKSOBCYt9atNAUQVtbEwx9QaZGj8moYIe4nXgx72Ktwg0Gruh8sS7GQLBizCXg7fCk62sDV_MZINnwON9gsKbxxgn9mLFeYSaatUzk-VRphDoHNIBC-qeDtYnZhsHYcV9Jp0GPkLNquhN1TXA7gT\",\n" +
                "                \"e\":\"AQAB\"\n" +
                "        },";

        try {
            Key key = Key.fromString(rsaKey);
            assertNotNull(key);
            assertNotNull(key.getPublicKey());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testParseECKey() {

        final String ecKey = "{\n" +
                "            \"kty\":\"EC\",\n" +
                "                \"kid\":\"eckey1\",\n" +
                "                \"alg\":\"ES256\",\n" +
                "                \"use\":\"sig\",\n" +
                "                \"crv\":\"P-256\",\n" +
                "                \"x\":\"AI0x6wEUk5T0hslaT83DNVy5r98XnG7HAjQynjCrcdCe\",\n" +
                "                \"y\":\"ATdV2ebpefqBli_SXZwvL3-7OiD3MTryGbR-zRSFZ_s=\"\n" +
                "        }";

        try {
            Key key = Key.fromString(ecKey);
            assertNotNull(key);
            assertNotNull(key.getPublicKey());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
