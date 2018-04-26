package com.kdzido.thesis.config

import io.restassured.RestAssured
import io.restassured.http.ContentType
import spock.lang.Requires
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

import static org.awaitility.Awaitility.await

/**
 * @author krzysztof.dzido@gmail.com
 */
@Requires({ env['EUREKASERVICE_URI_1'] && env['EUREKASERVICE_URI_2'] })
@Stepwise
class ConfigServiceIntegSpec extends Specification {

    @Unroll
    def "that eureka peers are up: #peer1, #peer2"() { // readable fail
        expect:
        await().atMost(2, TimeUnit.MINUTES).until({ is200(peer1) })
        await().atMost(2, TimeUnit.MINUTES).until({ is200(peer2) })

        where:
        peer1                                | peer2
        System.getenv("EUREKASERVICE_URI_1") | System.getenv("EUREKASERVICE_URI_2")
    }

    @Unroll
    def "that eureka peers are aware of each other: #peer1, #peer2"() { // readable fail
        expect:
        await().atMost(3, TimeUnit.MINUTES).until({ isEurekaRegisteredInCluster(peer1) })
        await().atMost(3, TimeUnit.MINUTES).until({ isEurekaRegisteredInCluster(peer2) })

        where:
        peer1                                | peer2
        System.getenv("EUREKASERVICE_URI_1") | System.getenv("EUREKASERVICE_URI_2")
    }

    static getEurekaApps(eurekaBaseUri) {
        def response = RestAssured.given().when()
                .accept(ContentType.JSON)
                .get("$eurekaBaseUri/apps")
                .then()
                .extract().response()
        return response
    }

    static is200(eurekaBaseUri) {
        try {
            def response = getEurekaApps(eurekaBaseUri)
            return response.statusCode() == 200
        } catch (e) {
            return false
        }
    }

    static isEurekaRegisteredInCluster(eurekaBaseUri) {
        try {
            def response = getEurekaApps(eurekaBaseUri)
            return response.statusCode() == 200 &&
                   response.body().jsonPath().get("applications.application.name") == ["EUREKASERVICE"] &&
                   response.body().jsonPath().get("applications.application.instance.app") ==  [["EUREKASERVICE", "EUREKASERVICE"]]
        } catch (e) {
            return false
        }
    }

}
