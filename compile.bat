javac *.java
jar -cfe mccandlessMobileNode.jar MobileNode MobileNode.java MobileNode.class Frame.java Frame.class
jar -cfe mccandlessForeignAgent.jar ForeignAgent ForeignAgent.java ForeignAgent.class Frame.java Frame.class
@REM jar -cfe millsHomeAgent.jar HomeAgent HomeAgent.java HomeAgent.class Frame.java Frame.class
@REM jar -cfe milssCorrespondent.jar Correspondent Correspondent.java Correspondent.class Frame.java Frame.class