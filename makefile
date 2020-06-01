all:			Main.class Coordinator.class Node.class Resource.class

Coordinator.class: Coordinator.java
				@javac Coordinator.java

Resource.class:  Resource.java
				@javac Resource.java

Node.class:     Node.java
				@javac Node.java

Main.class:     Main.java
				@javac Main.java

clean:
				@rm -rf *.class *~