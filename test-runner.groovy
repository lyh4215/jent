#!/usr/bin/env groovy
this.class.classLoader.addClasspath("src")
this.class.classLoader.addClasspath("test")

import org.company.ci.FailureClassifierTest

FailureClassifierTest.run()
//groovy -cp src:test test-runner.groovy