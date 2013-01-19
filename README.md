Introduction
===========

This is an [IntelliJ] plugin for [Fitnesse].

[IntelliJ]: http://www.jetbrains.com/idea/
[Fitnesse]: http://www.fitnesse.org/

Current Status
===========

The plugin has very minimal features and is a work in progress.

Working Features
----------------
* Highlighting of WikiWords and table cell text. (Default color scheme is super ugly. Suggestions welcome).
* Links to other test pages.
* Find test by file name (CTRL-SHIFT-N and type in test name).

Planned Features
----------------
* Goto Java class/method from Fitnesse.
* Find Fitnesse references in Java code (i.e. Which Fitnesse test uses this fixture?)
* Goto/Reference support for Scenario Libraries.
* Auto complete in tables.
* Run the Fitnesse server from IntelliJ. Have a goto wiki action on a file to open the browser with the test loaded.

Maybe Features
--------------
* Make the formatting in IntelliJ match the wiki.
* Run tests in IntelliJ and show results inline.
* Create test/suite action in IntelliJ so you don't have to copy/paste files or goto the wiki
* Edit test/suite properties in IntelliJ.

Building the plugin
===================

Prerequisites
-------------
I am building the plugin locally using IntelliJ 12.0.2(Build 123.123). To build it locally on your machine, modify idea.version and idea.build in pom.xml to match your local install. Unfortunately, I'm unable to find any versions of OpenAPI in Maven past 7.0.3. You'll have to install the various Intellij jars located in the lib folder of your IntelliJ install into your local Maven repository by running:

    ./install-intellij-libs.sh <path to IntelliJ 12.0.2>

You'll also need the patched [maven-jflex-plugin]. See that readme for installation instructions.

[maven-jflex-plugin]: https://github.com/gshakhn/maven-jflex-plugin

Building
--------

After you install all the jars this plugin needs into your local repo, just run

    mvn package

The resulting zip file will be located in the target folder.

License
=======

This plugin is licensed under [Apache License, V2.0].

Feel free to fork and submit pull requests. I'll try to get them into the mainline ASAP.

[Apache License, V2.0]: http://www.apache.org/licenses/LICENSE-2.0