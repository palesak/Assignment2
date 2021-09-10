# Palesa Khoali
# KHLPAL002
# CSC2002S

.SUFFIXES: .java .class
JAVAC = javac -g
.java.class:
	$(JAVAC) $*.java

CLASSES = \
	Score.java \
	WordApp.java \
	WordPanel.java \
	TheGame.java \
	WordRecord.java \
	WordDictionary.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm *.class

run:
	java $(MAIN_CLASS)