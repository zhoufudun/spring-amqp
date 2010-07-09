package org.springframework.build

class RabbitUtils {
    def static rabbitServerIsRunning() {
        try {
            def port = 5672
            print "is rabbitmq-server running on ${port}: "
            new java.net.Socket("localhost", port)
            println "RUNNING"
            return true
        } catch (java.net.ConnectException ex) {
            println "NOT RUNNING"
            println "tests will be skipped as rabbitmq-server is not running"
            println "run with '-PforceServerTests' to force tests to run"
            return false;
        }
    }
}
