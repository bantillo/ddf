/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.security.idp.plugin.namequalifier

import ddf.security.samlp.SamlProtocol
import org.apache.wss4j.common.saml.OpenSAMLUtil
import org.opensaml.saml.saml2.core.Assertion
import org.opensaml.saml.saml2.core.AuthnRequest
import org.opensaml.saml.saml2.core.Issuer
import org.opensaml.saml.saml2.core.Response
import org.opensaml.saml.saml2.core.Subject
import org.opensaml.saml.saml2.core.NameID
import spock.lang.Specification
import org.apache.wss4j.common.saml.builder.SAML2Constants
import org.opensaml.saml.saml2.core.NameIDPolicy
import org.opensaml.saml.saml2.core.NameID

class NameQualifierPresignPluginSpec extends Specification {

    NameQualifierPresignPlugin plugin = new NameQualifierPresignPlugin()

    // general mocks
    AuthnRequest authNReq = Mock(AuthnRequest)
    Issuer issuer = Mock(Issuer)

    List<String> spMetadata = new ArrayList<>()
    Set<SamlProtocol.Binding> bindings = new HashSet<>()

    // set of mocks and concrete objects
    Response response = Mock(Response)

    List<Assertion> assertions = new ArrayList<>()
    Assertion assertion = Mock(Assertion)

    Subject subject = Mock(Subject)
    NameID nameID = Mock(NameID)
    NameID nameIDNeg = Mock(NameID)

    NameIDPolicy nameIdPolicy = Mock(NameIDPolicy)

    String format = new String(SAML2Constants.NAMEID_FORMAT_PERSISTENT)

    def NAME_QUALIFIER = 'https://localhost:8993/services/idp/login'

    def PERSISTENT = 'urn:oasis:names:tc:SAML:2.0:nameid-format:persistent'

    def NEGATIVE_TEST = 'negative test'

    def setupSpec() {
        OpenSAMLUtil.initSamlEngine()
    }

    def setup() {

        // set up authNReq
        authNReq.getIssuer() >> issuer

        // set up subject
        assertion.getIssuer() >> issuer

        // set up basic response
        response.assertions >> assertions

        // set up the name id policy
        authNReq.getNameIDPolicy() >> nameIdPolicy;

    }

     def 'test name qualifier when issuer format is not null and is persistent'() {

        setup:
        response.getIssuer() >> issuer
        response.getIssuer().getFormat() >> format
        assertions.add(assertion)
        assertion.getSubject() >> subject
        assertion.getSubject().getNameID() >> nameID
        nameID.getFormat() >> PERSISTENT

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        1 * nameID.getFormat()
        2 * issuer.setNameQualifier(NAME_QUALIFIER)

    }

    def 'test name qualifier when nameid format is null and is persistent'() {

        setup:
        response.getIssuer() >> null
        assertions.add(assertion)
        assertion.getSubject() >> subject
        assertion.getSubject().getNameID() >> nameID
        nameID.getFormat() >> PERSISTENT

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        1 * nameID.getFormat()
        0 * issuer.setNameQualifier(NAME_QUALIFIER)

    }

    def 'test name qualifier when no assertions'() {

        setup:
        response.getIssuer() >> issuer
        response.getIssuer().getFormat() >> format
        assertion.getSubject() >> subject
        assertion.getSubject().getNameID() >> nameID
        nameID.getFormat() >> PERSISTENT

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        0 * nameID.setNameQualifier(NAME_QUALIFIER)
        0 * nameID.getFormat()

    }


    def 'test name qualifier when issuer format is null'() {

        setup:
        response.getIssuer() >> issuer
        response.getIssuer().getFormat() >> null
        assertions.add(assertion)

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        0 * nameID.getFormat()

    }

    def 'test name qualifier when issuer format is not persistent'() {

        setup:
        response.getIssuer() >> issuer
        response.getIssuer().getFormat() >>  NEGATIVE_TEST
        assertions.add(assertion)

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        0 * nameID.getFormat()

    }

    def 'test name qualifier when assertion subject is null'() {

        setup:
        response.getIssuer() >> issuer
        response.getIssuer().getFormat() >> format
        assertions.add(assertion)
        assertion.getSubject() >> null

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        0 * nameID.getFormat()

    }

    def 'test name qualifier when assertion subject is not null and name id is persistent'() {

        setup:
        response.getIssuer() >> issuer
        response.getIssuer().getFormat() >> format
        assertions.add(assertion)
        assertion.getSubject() >> subject
        assertion.getSubject().getNameID() >> nameID

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        1 * nameID.getFormat()

    }

    def 'test name qualifier when assertion subject is not null and name id is not persistent'() {

        setup:
        response.getIssuer() >> issuer
        response.getIssuer().getFormat() >> format
        assertions.add(assertion)
        assertion.getSubject() >> subject
        assertion.getSubject().getNameID() >> nameIDNeg
        nameIDNeg.getFormat() >> NEGATIVE_TEST

        when:
        plugin.processPresign(response, authNReq, spMetadata, bindings)

        then:
        0 * nameID.getFormat()

    }

}
