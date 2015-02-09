mkdir bin
javac -d bin/ *.java
cd bin
jar -cfe mccandlessMobileNode.jar MobileNode ..\MobileNode.java MobileNode.class ..\Frame.java Frame.class
jar -cfe mccandlessForeignAgent.jar ForeignAgent ..\ForeignAgent.java ForeignAgent.class ..\Frame.java Frame.class
@REM jar -cfe bin/millsHomeAgent.jar HomeAgent ..\HomeAgent.java HomeAgent.class Frame.java ..\Frame.class
@REM jar -cfe bin/milssCorrespondent.jar Correspondent ..\Correspondent.java Correspondent.class ..\Frame.java Frame.class