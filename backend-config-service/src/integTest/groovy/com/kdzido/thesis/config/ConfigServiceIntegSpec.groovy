package com.kdzido.thesis.config

import groovyx.net.http.RESTClient
import org.springframework.http.MediaType
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

import static org.awaitility.Awaitility.await

/**
 * @author krzysztof.dzido@gmail.com
 */
@Stepwise
class ConfigServiceIntegSpec extends Specification {

    final static EUREKASERVICE_URI_1 = System.getenv("EUREKASERVICE_URI_1")
    final static EUREKASERVICE_URI_2 = System.getenv("EUREKASERVICE_URI_2")
    final static CONFIGSERVICE_URI = System.getenv("CONFIGSERVICE_URI")

    def configServiceClient = new RESTClient("$CONFIGSERVICE_URI")

    def eurekapeer1Client = new RESTClient("$EUREKASERVICE_URI_1").with {
        setHeaders(Accept: MediaType.APPLICATION_JSON_VALUE)
        it
    }
    def eurekapeer2Client = new RESTClient("$EUREKASERVICE_URI_2").with {
        setHeaders(Accept: MediaType.APPLICATION_JSON_VALUE)
        it
    }

    def "that config service is registered in Eureka peers"() {
        expect:
        await().atMost(2, TimeUnit.MINUTES).pollInterval(1, TimeUnit.SECONDS).until({
            try {
                def resp = eurekapeer1Client.get(path: "/eureka/apps")
                resp.status == 200 &&
                    resp.headers.'Content-Type'.contains(MediaType.APPLICATION_JSON_VALUE) &&
                    resp.data.applications.application.any {it.name == "CONFIGSERVICE" }
            } catch (e) {
                return false
            }
        })

        and:
        await().atMost(2, TimeUnit.MINUTES).pollInterval(1, TimeUnit.SECONDS).until({
            try {
                def resp = eurekapeer2Client.get(path: "/eureka/apps")
                resp.status == 200 &&
                        resp.headers.'Content-Type'.contains(MediaType.APPLICATION_JSON_VALUE) &&
                        resp.data.applications.application.any {it.name == "CONFIGSERVICE" }
            } catch (e) {
                return false
            }
        })
    }

    @Unroll
    def "that config service returns configuration of #serviceName / #serviceProfile"() { // readable fail
        expect:
        await().atMost(3, TimeUnit.MINUTES).pollInterval(1, TimeUnit.SECONDS).until({
            try {
                def resp = configServiceClient.get(path: "/$serviceName/$serviceProfile")
                resp.status == 200 &&
                    resp.headers.'Content-Type'.contains(MediaType.APPLICATION_JSON_VALUE) && // WRONG??
                    resp.data.name == "todoservice" &&
                    resp.data.profiles == ["$serviceProfile"] &&
                    resp.data.propertySources.any {
                        it.name == "https://github.com/kdzido/thesis-config/todoservice/todoservice.yml"  &&
                                it.source.'todo.property' == "This is a Git-backed test property for the todoservice"
                    }
            } catch (e) {
                return false
            }
        })

        where:
        serviceName   | serviceProfile
        "todoservice" | "default"
    }


    // TODO test encryption / decryption endpoints
    // TODO test encryption / decryption endpoints
    // TODO test encryption / decryption endpoints


}
