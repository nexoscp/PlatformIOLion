 * CI Integration: utilise CI-Server-Plugins
 * INI-File-Plugin for platformio.ini with specific schema
    https://github.com/av239/ini4idea
    https://youtrack.jetbrains.com/issues?q=%23%7BPlugin:%20ini4idea%7D%20
    https://plugins.jetbrains.com/plugin/6981-ini
 * new project wizard with data from 
   * platformio device list --json-output
   * platformio boards --json-output
   * platformio  platform frameworks --json-output > frameworks.json
   * platformio  platform search --json-output > platforms.json
 * integration of SCons Build-Tool, cause PlatformIO 4.0 uses this as build-tool
 * Facets implementation for Boards and Frameworks, Platform
 * platformio test only when ./test exist
 * platformio platform frameworks
 * sdkType
 * locator-Extension !!!!!
 * support for IDEA, CLion, Android Studio, App Someththing ... to conditionals in plugin.xml  

 * http://arhipov.blogspot.com/2011/04/code-snippet-intercepting-on-save.html
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/project_wizard.html
 * Setup Wizard: https://www.jetbrains.org/intellij/sdk/docs/tutorials/framework.html

 * BuildIntegration: https://www.jetbrains.org/intellij/sdk/docs/reference_guide/frameworks_and_external_apis/external_builder_api.html
 * com.intellij.openapi.fileTypes.FileTypeFactory deprecated https://www.jetbrains.org/intellij/sdk/docs/reference_guide/api_notable/api_notable_list_2019.html
 * http proxy support
 * Mark src/lib/... Folders
    https://docs.platformio.org/en/latest/projectconf/section_platformio.html#src-dir
    https://intellij-support.jetbrains.com/hc/en-us/community/posts/206768495-Mark-directory-as-sources-root-from-plugin
 
 * Library Dependency Finder (LDF)  http://docs.platformio.org/en/latest/librarymanager/ldf.html
 * use `PasswordSafe` for `www.platformio.org` credentials
 * add some actions to Welcome-Screen: https://intellij-support.jetbrains.com/hc/en-us/community/posts/360001164800-Add-plugin-action-to-Welcome-window
 
https://stackoverflow.com/questions/55403612/intellij-plugin-custom-compiler-not-invoked-despite-entry-in-meta-inf-services

https://github.com/JetBrains/intellij-community/tree/master/platform/external-system-api/src/com/intellij/openapi/externalSystem

https://intellij-support.jetbrains.com/hc/en-us/community/posts/360003290900-More-compete-documentation-and-or-examples-for-external-system-api

https://intellij-support.jetbrains.com/hc/en-us/community/posts/360003335979-External-Builder-API-Specific-Implementation-Details

https://github.com/ignatov/intellij-erlang/tree/master/src/org/intellij/erlang

https://github.com/pantsbuild/intellij-pants-plugin/blob/master/src/com/twitter/intellij/pants/PantsManager.java

