@mkdir bin > nul
javac -d bin/ *.java
cd bin
jar -cfe mccandlessMobileNode.jar MobileNode ..\MobileNode.java MobileNode.class ..\Frame.java Frame.class ..\FrameHandler.java FrameHandler.class
jar -cfe mccandlessForeignAgent.jar ForeignAgent ..\ForeignAgent.java ForeignAgent.class ..\Frame.java Frame.class ..\FrameHandler.java FrameHandler.class
jar -cfe millsHomeAgent.jar HomeAgent ..\HomeAgent.java HomeAgent.class ..\Frame.java Frame.class ..\FrameHandler.java FrameHandler.class
jar -cfe millsCorrespondent.jar Correspondent ..\Correspondent.java Correspondent.class ..\Frame.java Frame.class ..\FrameHandler.java FrameHandler.class
@cd ..