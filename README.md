+---------------------------------------------+
|                CORE-API README              |
+---------------------------------------------+

Purpose
-------

This module only defines the interface for all plugins in the system.

It contains no implementation — just the shared contract (`Plugin` interface)
that all plugins must implement.

Usage
-----

Other modules (e.g., main-app, plugins like ClockPlugin) must depend on this
module to compile and function properly.

Make sure to build this module first before building any plugin:

    mvn clean install

+---------------------------------------------+
