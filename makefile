JAVA_LIST=$(addprefix sim/, SimMain.java Class_1.java FXMLPackagesControl.java SaxXML.java Class_2.java SmartPost.java Class_3.java SmartPostManager.java FXMLMainControl.java Item.java Storage.java AbstractPackage.java)

# Archlinux fix version.
# TODO: Other OSes.
JAVAFX_OPTION=--module-path /usr/lib/jvm/default/lib/javafx.media.jar:/usr/lib/jvm/default/lib/javafx.web.jar:/usr/lib/jvm/default/lib/javafx.fxml.jar:/usr/lib/jvm/default/lib/javafx.base.jar:/usr/lib/jvm/default/lib/javafx.controls.jar:/usr/lib/jvm/default/lib/javafx.graphics.jar --add-modules=javafx.media,javafx.controls,javafx.web,javafx.fxml

all:
	javac $(JAVAFX_OPTION) -Xmaxerrs 1 $(JAVA_LIST)

run:
	java $(JAVAFX_OPTION) sim.SimMain

clear:
	rm sim/*.class
