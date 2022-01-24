# ldapdecoder

ldapdecoder decodes a java TLS debug log file with `javax.net.debug=ssl,record,plaintext` and print LDAP operations in human-readable format. It may work for Java 1.8.0_272 and higher on Python>=3.7.

## prerequisites

ldapdecoder uses [asn1](https://github.com/andrivet/python-asn1). To install the module:
~~~
pip install asn1 --user
~~~

## usage

1. Ask your customer to run Java application which has the LDAP issue with the JVM option:
   ~~~
   -Djavax.net.debug=ssl,record,plaintext
   ~~~
   and ask the customer to send the debug message after reproducing the issue.

2. Attach ldapdecoder to the debug message.
   ~~~
   $ ./ldapdecoder server.log
   ~~~
