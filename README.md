Introduction
===========

This is an [IntelliJ] plugin for [FitNesse].

Current Status
===========
[![Build Status][travis-ci-badge]][travis-ci] [![Codacy Badge][codacy-badge]][codacy] [![Dependency Status][versioneye-badge]][versioneye]


The plugin fully functional and supports the following features:

 * Syntax highlighting
 * Links to other wiki pages
 * Supports Slim Decision, Query, Script, Scenario and Table tables
 * Syntax checking: warning for missing references
 * Execution of test suites, with readable console output
 * Folding of collapsible sections
 * Create wiki page action

This plugin is available via the [IntelliJ plugin repository].

Building the plugin
===================

Run `./gradlew dist` to build the plugin distribution.

Participating
=============

If you want to help out with the plugin, please check the [wiki] for tasks that are still to be done.

To develop on the plugin you'll need IntelliJ (14).

Make sure the following plugins are enabled:

 * Scala
 * Plugin DevKit
 * UI Designer

Run `./gradlew idea` to generate the module and project files. Because this is a IntelliJ plugin project, you should
not try to import the gradle file directly in IntelliJ.

Before opening the project in IntelliJ, make sure you've created a IDEA SDK configuration:

   File -> Project Structure...

Goto `Platform Settings / SDKs` and add (`+`) a IntelliJ Platform Plugin SDK. Point it to `{project-root}/lib/sdk/idea-IC-139.1603.1`.

Now you can open the project, build it and run it as a Plugin.

Of course you can also build the plugin with Gradle: `./gradlew build`.

I use the following VM options for the plugin: `-Xms128m -Xmx750m -XX:MaxPermSize=350m -XX:ReservedCodeCacheSize=225m -XX:+UseCompressedOops`, for what it's worth.

Read the [Guidelines for plugin development] for info on writing a plugin.

Try their [Custom language support tutorial].


License
=======

This plugin is licensed under [Apache License, V2.0].

Feel free to fork and submit pull requests. I'll try to get them into the mainline ASAP.

[IntelliJ]: http://www.jetbrains.com/idea/
[Fitnesse]: http://www.fitnesse.org/
[IntelliJ plugin repository]: https://plugins.jetbrains.com/plugin/7908
[Guidelines for plugin development]: https://www.jetbrains.com/idea/help/plugin-development-guidelines.html
[Custom language support tutorial]: https://confluence.jetbrains.com/display/IntelliJIDEA/Custom+Language+Support
[wiki]: https://github.com/amolenaar/idea-fitnesse/wiki
[Apache License, V2.0]: http://www.apache.org/licenses/LICENSE-2.0

[travis-ci-badge]: https://travis-ci.org/gshakhn/idea-fitnesse.svg
[travis-ci]: https://travis-ci.org/gshakhn/idea-fitnesse
[codacy-badge]: https://www.codacy.com/project/badge/655882f037764ee195733a479e0eaaa6
[codacy]: https://www.codacy.com/app/gshakhn/idea-fitnesse
[versioneye-badge]: https://www.versioneye.com/user/projects/554989f65d4f9a0b990012e5/badge.svg?style=flat
[versioneye]: https://www.versioneye.com/user/projects/554989f65d4f9a0b990012e5

