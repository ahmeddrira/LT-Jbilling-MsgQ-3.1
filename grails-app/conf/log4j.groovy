/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vbodani
 */
'log4j:configuration'("xmlns:log4j": "http://jakarta.apache.org/log4j/", debug: "false") {

    def catalinaHome = System.properties.getProperty('catalina.home')
    if (!catalinaHome) catalinaHome = '..\\'   // just in case
    def logDirectory = "${catalinaHome}/logs"

  /**
   * Appender: single entry - file
   */
  appender(name: "FILE", 'class': "org.apache.log4j.DailyRollingFileAppender") {
    errorHandler 'class': "org.apache.log4j.helpers.OnlyOnceErrorHandler"
    param name: "File", value: "${logDirectory}/server.log"
    param name: "Append", value: "false"
    param name: "Threshold", value: "INFO"
    param name: "DatePattern", value: "'.'yyyy-MM-dd"
    layout('class': "org.apache.log4j.PatternLayout") {
      param name: "ConversionPattern", value: "%d %-5r %-5p [%c] (%t:%x) %m%n"
    }
  }

   /**
   * Appender: A time/date based rolling appender 
   */
  appender(name: "SQL-FILE", 'class': "org.apache.log4j.DailyRollingFileAppender") {
    errorHandler 'class': "org.apache.log4j.helpers.OnlyOnceErrorHandler"
    param name: "File", value: "${logDirectory}/sql.log"
    param name: "Append", value: "false"
    param name: "Threshold", value: "DEBUG"
    param name: "DatePattern", value: "'.'yyyy-MM-dd"
    layout('class': "org.apache.log4j.PatternLayout") {
      param name: "ConversionPattern", value: "%d %-5r %-5p [%c] (%t:%x) %m%n"
    }
  }

  /**
  * Appender: jBilling Messages
  */
 appender(name: "jbillingAppender", 'class': "org.apache.log4j.DailyRollingFileAppender") {
   param name: "File", value: "${logDirectory}/jbilling.log"
   param name: "Append", value: "false"
   param name: "Threshold", value: "DEBUG"
   param name: "DatePattern", value: "'.'yyyy-MM-dd"
   layout('class': "org.apache.log4j.PatternLayout") {
      param name: "ConversionPattern", value: "%d %-5p [%c] %m%n"
   }
 }
 
   /**
   * Appender: A time/date based rolling appender
   */
  appender(name: "CONSOLE", 'class': "org.apache.log4j.ConsoleAppender") {
    param name: "Target", value: "System.out"
    param name: "Threshold", value: "INFO"
    layout('class': "org.apache.log4j.PatternLayout") {
      param name: "ConversionPattern", value: "%d{ABSOLUTE} %-5p [%c{1}] %m%n"
    }
  }

  /**
   * Category - shared definition by log level
   */
  def levels = [:]
  levels.'warn' = ['org.jgroups']
  levels.'info' = ['org.apache', 'org.quartz', 'org.jboss', 'org.springframework', 'org.codehaus', 'org.mortbay'] +
          ['groovy', 'grails']
  levels.'debug' = []

  levels.each {level, packages ->
    packages.each {
      'category'(name: it, additivity: 'false') {
        'priority'(value: level.toUpperCase())
      }
    }
  }
  
  def jbilling =['com.sapienter.jbilling']
  jbilling.each {
	  'category'(name: it, additivity: 'true') { 'priority'(value: 'DEBUG'); 'appender-ref'('ref': "jbillingAppender")}
	}
  
  def mchange =['com.mchange']
  mchange.each {
	  'category'(name: it, additivity: 'true') { 'priority'(value: 'INFO'); 'appender-ref'('ref': "FILE")}
	}

  /**
   * Category - root definition
   */
  'root' {    
    'appender-ref' ref: "CONSOLE"
    'appender-ref' ref: "FILE"
  }
}
