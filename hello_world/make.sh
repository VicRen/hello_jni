#!/bin/sh

# openbsd 4.9
# gcc 4.2.1
# openjdk 1.7.0

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.
javac HelloWorld.java
javah HelloWorld
gcc -I/Library/Java/JavaVirtualMachines/jdk1.8.0_191.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_191.jdk/Contents/Home/include/darwin -shared hello_world.c -o libHelloWorld.jnilib
java -classpath . -Djava.library.path=. HelloWorld