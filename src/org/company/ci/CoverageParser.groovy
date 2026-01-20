package org.company.ci

class CoverageParser {
    static String extractLineRate(String file) {
        def xml = new XmlSlurper().parse(new File(file))
        def rate = (xml.@'line-rate'.toDouble() * 100)
        return String.format('%.2f', rate)
    }
}
