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
To develop on the plugin you'll need IntelliJ (14). Configure it as a "IntelliJ Platform Plugin SDK" in the Project Structure dialog. Mine is named "IDEA IU-139.1117.1".

Make sure the following plugins are enabled:

 * Scala
 * Plugin DevKit
 * Grammar-Kit

Read the [Guidelines for plugin development](https://www.jetbrains.com/idea/help/plugin-development-guidelines.html) for info on writing a plugin.

Building
--------

Import the module in IntelliJ. There is no build script currently.

License
=======

This plugin is licensed under [Apache License, V2.0].

Feel free to fork and submit pull requests. I'll try to get them into the mainline ASAP.

[Apache License, V2.0]: http://www.apache.org/licenses/LICENSE-2.0
