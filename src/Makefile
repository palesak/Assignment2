JFLAGS = -g
JC = javac
MAIN_CLASS = WordApp
CONCURRENT_WORDS = 4
TOTAL_WORDS = 25
DICT_FILE = example_dict.txt

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Score.java \
    	WordApp.java \
    	WordPanel.java \
    	WordThread.java \
    	TheGame.java \
    	WordRecord.java \
    	WordDictionary.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run:
	java $(MAIN_CLASS) $(TOTAL_WORDS) $(CONCURRENT_WORDS) $(FILE)

run-default:
	java $(MAIN_CLASS) $(TOTAL_WORDS) $(CONCURRENT_WORDS) ../$(DICT_FILE)

run-no-file:
	java $(MAIN_CLASS) $(TOTAL_WORDS) $(CONCURRENT_WORDS) None