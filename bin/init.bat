javac sealed/*.java
timeout /t 5
rmic sealed.SealedFactoryImpl
timeout /t 5
start rmiregistry 2000
timeout /t 5
start java -Djava.security.policy=sealed/policy.txt sealed.SealedServer 127.0.0.1:2000
timeout /t 5
java -Djava.security.policy=sealed/policy.txt sealed.SealedClient 127.0.0.1:2000