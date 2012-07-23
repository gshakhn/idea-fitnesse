Introduction
===========

This is an [IntelliJ] plugin for [Fitnesse].

[IntelliJ]: http://www.jetbrains.com/idea/
[Fitnesse]: http://www.fitnesse.org/


Building the plugin
===================

Prerequisites
-------------
I am building the plugin locally using IntelliJ 11.1.2 (Build 117.418). To build it locally on your machine, modify idea.version and idea.build in pom.xml to match your local install. Unfortunately, I'm unable to find any versions of OpenAPI in Maven past 7.0.3. You'll have to install the various Intellij jars located in the lib folder of your IntelliJ install into your local Maven repository by running:

    ./install-intellij-libs.sh <path to IntelliJ 11.1.2>

You'll also need the patched [maven-jflex-plugin]. See that readme for installation instructions.

[maven-jflex-plugin] https://github.com/gshakhn/maven-jflex-plugin

Building
--------

After you install all the jars this plugin needs into your local repo, just run

    mvn package

The resulting zip file will be located in the target folder.

License
=======

This plugin is licensed under [LGPL Version 3].

Feel free to fork and submit pull requests. I'll try to get them into the mainline ASAP.


This plugin is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

In other words, this plugin won't break your computer as far as I know, but use at your own risk.

[LGPL Version 3]: http://www.gnu.org/licenses/lgpl-3.0.txt