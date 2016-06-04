/*
 * Copyright 2016 David Lukacs
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
package org.khannex.action;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.khannex.io.CardIO;
import org.khannex.io.Response;

public class GetLocation extends Command {

    @Override
    public Response execute(Context context) {
        Response retval = null;

        CardIO cardio = new CardIO();
        try {
            cardio.connect();
            cardio.transmitReset().assertSuccessful();
            cardio.transmitSelectDf(new byte[]{0x50, 0x11}).assertSuccessful();
            cardio.transmitSelectDf(new byte[]{(byte) 0x90, 0x01}).assertSuccessful();
            cardio.transmitSelectCertificate().assertSuccessful();
            byte[] payload = cardio.transmitGetBinary().assertSuccessful().getData();

            retval = response().withResult(getCertificateLocality(payload)).build();
        } catch (CertificateException | InvalidNameException ex) {
            context.setException(ex);

            retval = response().build();
        } finally {
            cardio.disconnect();
        }

        return retval;
    }

    private String getCertificateLocality(byte[] cert) throws CertificateException, InvalidNameException {
        String retval = null;

        CertificateFactory factory = CertificateFactory.getInstance("X509");
        X509Certificate x509 = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(cert));
        LdapName subject = new LdapName(x509.getSubjectDN().toString());
        for (Rdn rdn : subject.getRdns()) {
            if ("L".equalsIgnoreCase(rdn.getType())) {
                retval = rdn.getValue().toString();
                break;
            }
        }

        return retval;
    }

}
