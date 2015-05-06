Introduction
===========

This is an [IntelliJ] plugin for [Fitnesse].

[IntelliJ]: http://www.jetbrains.com/idea/
[Fitnesse]: http://www.fitnesse.org/

Current Status
===========

[![Build Status](https://travis-ci.org/gshakhn/idea-fitnesse.svg)](https://travis-ci.org/gshakhn/idea-fitnesse)

[![Codacy Badge](https://www.codacy.com/project/badge/655882f037764ee195733a479e0eaaa6)](https://www.codacy.com/app/gshakhn/idea-fitnesse)

[![Dependency Status](https://www.versioneye.com/user/projects/554989f65d4f9a0b990012e5/badge.svg?style=flat)](https://www.versioneye.com/user/projects/554989f65d4f9a0b990012e5)

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
To develop on the plugin you'll need IntelliJ (14).

Make sure the following plugins are enabled:

 * Scala
 * Plugin DevKit
 * Grammar-Kit

Read the [Guidelines for plugin development](https://www.jetbrains.com/idea/help/plugin-development-guidelines.html) for info on writing a plugin.

Try their [Custom language support tutorial](https://confluence.jetbrains.com/display/IntelliJIDEA/Custom+Language+Support)

Building
--------

Run `gradle idea` to generate the module and project files.

Before opening the project in IntelliJ, make sure you've created a IDEA SDK configuration:

   File -> Project Structure...

Goto `JDKs` and add (`+`) a IntelliJ Platform Plugin SDK. Point it to `lib/sdk/idea-IC-139.1603.1`.

Now you can savely open the project, build it and run it as a Plugin.

Of course you can also build the plugin with Gradle.

License
=======

This plugin is licensed under [Apache License, V2.0].

Feel free to fork and submit pull requests. I'll try to get them into the mainline ASAP.

[Apache License, V2.0]: http://www.apache.org/licenses/LICENSE-2.0
