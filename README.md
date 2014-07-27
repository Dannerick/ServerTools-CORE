Server Tools
========

Servertools is a server-side minecraft mod that adds a variety of utilities and commands. This is the core repository and other repositories exist for backups, permission systems, etc.

[Minecraft Forums Thread] (http://www.minecraftforum.net/topic/1835347-)

[Curse Forge Page] (http://www.curse.com/mc-mods/minecraft/forgeservertools)

##Depending on ServerTools
```groovy
repositories {
    mavenCentral()
    maven {
        name 'Matthews Maven Repo'
        url 'http://maven.matthewprenger.com'
    }
}

dependencies {
    compile 'com.matthewprenger.servertools:ServerTools-CORE:1.7.10-2.1.0.41:deobf'
}
```

Just add the above code to your build.gradle, refresh your project, and you will have access to ServerTools code.


[License](LICENSE)
-------
	Copyright 2014 Matthew Prenger

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

Building
--------
If you just want to build the mod, run `gradlew setupCIWorkspace build` and grab your jar from `build/libs`
