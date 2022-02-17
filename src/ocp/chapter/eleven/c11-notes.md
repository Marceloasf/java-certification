# Chapter 11 - Modules

- Understanding Modules
  - Describe the Modular JDK.
  - Declare modules and enable access between modules.
  - Describe how a modular project is compiled and run.

## Introducing Modules (p.454-457)

- Since Java 9, packages can be grouped into modules.
- When you have a big project that consist of hundred or thousand of classes grouped into packages, all these packages
  are grouped into Java archive (JAR) files. A JAR is a zip file with some extra info, and the extension is .jar.
- The main purpose of a module is to provide groups of related packages to offer a particular set of functionality to
  developers. It's like a JAR file except a developer chooses which packages are accessible outside the module.
- A complex chain of dependencies and minimum version is often referred to by the community as JAR hell.
- The Java Platform Module System (JPMS) includes the following:
  - A format for module JAR files.
  - Partitioning of the JDK into modules.
  - Additional command-line options for Java tools.
- A module is a group of one or more packages plus a special file called module-info.java.
- Modules have 'dependencies' between them, where one module can relie on code that is in another.
- Using modules is optional.

* Benefits of Modules
  - Better Access Control: Modules can act like as a fifth level of access control. They can expose packages within the
    modular JAR to specific packages.
  - Clearer Dependency Management: In a fully modular environment, each of the open source projects specify their dependencies
    in the module-info.java file. When launching the program, Java will complain that a library isn't in the module path in case
    it isn't and you'd know right away, instead of just blowing up on runtime with a message about not finding a required class
    if you forget to include a library in the classpath (nonmodular).
  - Custom Java Builds:
    - In the past, Java attempted to solve the size of JRE and JDK with a compact profile. The three compact profiles provided
      a subset of the built-in Java classes so there would be a smaller package for mobile and embedded devices. However, the
      compact profiles lacked flexibility, because many packages were included that developers were unlikely to use and at the
      same time, using other packages like Image I/O required the full JRE.
    - The JPMS allows developers to specify which modules they actually need. This makes it possible to create a smaller runtime
      image that is customized to what the application needs and nothing more. Users can run that image without having Java
      installed at all.
  - Improved Performance: Since Java now knows which modules are required, it only needs to look at those at class loading time.
    This improves startup time for big programs and requires less memory to run. This impacts mostly big applications.
  - Unique Package Enforcement:
    - Another manifestation of JAR hell is when the same package is in two JARs. There are number of causes of this problem
      including renaming JARs, or clever developers using a package name that is already taken and having two version of the
      same JAR on the classpath.
    - The JPMS prevents this scenario, by making a package allowed to be supplied by only one module. No more unpleasant surprises about
      a package at runtime.

## Creating, Compiling, Running and Packaging a Modular Program (p.458-464)

- Note: There is lots of practice in this chapter, so the .java files are located in subpackages of packages eleven and other.

  - There are a few key differences between a module-info file and a regular Java class:
    - The module-info file must be in the root directory of your MODULE. Regular Java classes should be in packages.
    - The module-info file must use the keyword module instead of class, interface or enum.
    - The module name follows the naming rules for package names. It often includes periods (.) in its name. Regular class and package
      names are not allowed to have dashes (-) and module names follows the same rule.
  - A module-info.java file can be empty. The compiler sees there isn't a class in there and exits without creating a .class file.
  - A module-info file can be empty (zero-bytes), but its required to be created
  - You can think of module-path as replacing the classpath option when you are working on a modular program.
  - Some valid commands that can be used to compile a module (using package eleven or single_module_c11 as the 'root' directory to
    execute and feeding as the module root):

    - `javac --module-path mods -d feeding feeding/zoo/animal/feeding/*.java feeding/module-info.java`
    - `javac -p mods -d feeding feeding/zoo/animal/feeding/*.java feeding/*.java`
    - `javac -p mods -d feeding feeding/zoo/animal/feeding/*.java feeding/module-info.java`
    - `javac -p mods -d feeding feeding/zoo/animal/feeding/Task.java feeding/module-info.java`
    - `javac -p mods -d feeding feeding/zoo/animal/feeding/Task.java feeding/*.java`

      - --module-path and -p are equivalent.
      - The usage of classpath is still available on Java 11, with it's three forms: -cp, --class-path and -classpath.

  - Options you need to know for using modules with 'javac':

  | Use for                    | Abbreviation | Long form            |
  | :------------------------- | :----------- | :------------------- |
  | Directory for .class files | -d <dir>     | n/a                  |
  | Module path                | -p <path>    | --module-path <path> |
  |                            |              |                      |

  - Running a module syntax example:

    - `java --module-path feeding --module zoo.animal.feeding/zoo.animal.feeding.Task`

      - "--module-path" is the location of modules
      - "zoo.animal.feeding" on the left side of the slash is the module name
      - "zoo.animal.feeding" on ther right side is the package name followed by the class name "Task",
        zoo.animal.feeding.Task (the fully qualified class name)
      - module name is followed by a slash (/) followed by the fully qualified class name

      * Note: Its common for the module name to match either the full package name or the beginning of it (convention).

  - "-module" has a short form too, is -m. So the following command is equivalent:

    - `java -p feeding -m zoo.animal.feeding/zoo.animal.feeding.Task`

  - Options you need to know for using modules with 'java':

  | Use for     | Abbreviation | Long form            |
  | :---------- | :----------- | :------------------- |
  | Module name | -m <name>    | --module             |
  | Module path | -p <path>    | --module-path <path> |
  |             |              |                      |

  - A module isn't much use if we can run it only in the folder it was created in. So we package it. Be sure to create a mods
    directory before running this command, because 'mods' is where the generated artifacts will be stored (.jar for example).

    - `jar -cvf mods/zoo.animal.feeding.jar -C feeding/ .`

  - With this command, we package everything under the feeding directory and store it in a JAR file named zoo.animal.feeding.jar
    under the mods folder. This represents how the module JAR will look to other code that wants to use it.

- Note: It is possible to version your module using the --module-version option, which is good to do when you are ready to share
  your module with others.

  - Now we can run the program using the mods directory instead of the loose classes:

    - `java -p mods -m zoo.animal.feeding/zoo.animal.feeding.Task`

      - Since a module path is used, a module JAR is being run.

- Note: Some commands can have their order inverted, like the 'java' command (java -m ... -p ...)

## Updating Our Example for Multiple Module (p.465-472)

- Note: The first version of the code until this section is located at the directory 'other/single_module_c11', to execute those files you need
  to use the single_module_c11 folder as the 'root'. The multiple module example is under directory 'eleven'.

  - The exports keyword is used to indicate that a module intends for those packages to be used by Java code outside the module:

    - `exports zoo.animal.feeding;`

  - You can reuse the same javac and jar commands ran previously to recompile and repackage the module inside the .jar file, so it'll update the
    dependencies exports and requires.
  - Remember that all modules must have a module-info file on the module root and inside of this file are located the dependencies of the module.
  - The requires statement specifies that a module is needed. So a module X depends on the Y:

    - `requires zoo.animal.feeding;`

  - Its intentional that the packages names begin with the same prefix as the module name.
  - Compiling more than one package command example:

    - `javac -p mods -d care care/zoo/animal/care/details/*.java care/zoo/animal/care/medical/*.java (...) care/module-info.java`

    * The --module-path (-p) directory must be the path where the modules jars were compiled, so Java can require the dependencies from the other modules.

    * Note that order matters when compiling a module. For example, the module-info can't go first on the command to compile, because Java will not find the
      packages that are being exported or required from the module-info files, a compiler error will be thrown since those packages haven't been compiled yet.

  - Remember that you need to repackage the modules using the jar command to refresh changes.

    - `jar -cvf mods/zoo.animal.feeding.jar -C feeding/ .`

## Diving into the module-info File (p.472-476)

- In these sections we will look at 'exports', 'requires', 'provides', 'uses' and 'opens'. All these keywords can appear in any order in the module-info file.
- Java is a bit sneaky here. These 'keywords' are only keywords inside a module-info.java file. In other files, like classes and interfaces, you are free to name
  your variable exports for example. These special keywords are called directives. You might ask why they are like this, its all about backward compatibility.

* exports:

  - Exports a package to other modules. It's also possible to export a package to a specific module. For example:

    `module zoo.animal.talks { exports zoo.animal.talks.content to zoo.staff; // Exports 'content' only to 'staff' module. exports zoo.animal.talk.media; requires zoo.animal.feeding; }`

  - On the module-info of zoo.staff, nothing changes (requires...). However, no other modules are allowed to access 'content' package.
  - When exporting a package, all public classes, interfaces and enums are exported.
  - Any public and protected fields and methods in those files are visible, but any fields and methods that are private or package-private are not visible.
  - Access control with modules:

  | Level                     | Within module code                             | Outside module                                       |
  | :------------------------ | :--------------------------------------------- | :--------------------------------------------------- |
  | private                   | Available only within class                    | No access                                            |
  | default (package-private) | Available only within packages                 | No access                                            |
  | protected                 | Available only within package or to subclasses | Accessible to subclasses only if package is exported |
  | public                    | Available to all classes                       | Accessible only if package is exported               |
  |                           |                                                |                                                      |

* requires and requires transitive:

  - requires specifies that the current module X depends on another module Y.
  - requires transitive specifies that any module X that requires a module Y that requires transitive Z will also depend on Z.
  - Example:
    `
    module zoo.animal.feeding {
    exports zoo.animal.feeding;
    }

    module zoo.animal.care {
    exports zoo.animal.care;
    requires transitive zoo.animal.feeding;
    }

    module zoo.animal.talks {
    exports zoo.animal.talks.content to zoo.staff;
    exports zoo.animal.talks.media;
    exports zoo.animal.talks.schedule;

    // requires zoo.animal.feeding - no longer needed
    // requires zoo.animal.care - no longer needed
    requires transitive zoo.animal.care;
    }

    module zoo.animal.staff {
    // requires zoo.animal.feeding - no longer needed
    // requires zoo.animal.care - no longer needed;
    requires zoo.animal.talks;
    }
    `

  - Applying the transitive modifier has the following effects:
    - Module zoo.animal.talks can optionally declare it requires the zoo.animal.feeding module, but is not required. Kind of reduntant.
    - Module zoo.animal.care cannot be compiled or executed without access to the zoo.animal.feeding module.
    - Module zoo.animal.talks cannot be compiled or executed without access to the zoo.animal.feeding module.
  - Remember that without the transitive modifier, all modules would need to explicitly use requires in order to reference any other packages.
  - Java doesn't allow you to repeat the same module in a requires clause at the same module (requires and requires transitive). It's reduntant.

* provides, uses and opens:

  - For 1Z0-815 exam you only need to be aware of them, on 1Z0-816 chapters will be more content about them.
  - The provides keyword specifies that a class provides an implementation of a service. To use it, you supply the API and class name that
    implements the API:

    - `provides zoo.staff.ZooApi with zoo.staff.ZooImpl;`

  - The uses keyword specifies a module is relying on a service. To code it, you supply the API you want to call:

    - `uses zoo.staff.ZooApi;`

  - Java allows callers to inspect and call code at runtime with a technique called 'reflection'. This is a powerful approach that allows calling
    code that might not be available at compile time. It can even be used to subvert access control. But don't worry, you don't need to know how to
    write code using reflection for the exam.
  - Since reflection can be dangerous, the module system requires developers to explicitly allow reflection in the module-info if they want, calling
    modules to be allowed to use it. For example:

    - `opens zoo.animal.talks.schedule;`
    - `opens zoo.animal.talks.media to zoo.staff;`

  - The first example allows any module using this one to use reflection. The second example only gives that privilege to the zoo.staff package.

## Discovering Modules (p.477-482)

- Since Java 9, the classes built into the JDK were modularized as well.
- For this section is important to know the syntax of the commands and what they do.

* The java Command:

  - Has three module-related options. One describes a module, another lists the available modules, and the third shows the module resolution logic.
  - It is also possible to add modules, exports and more at the command line with java command, but don't do that, is confusing and hard to maintain.

  1. Describing a Module

  - If you want to to know about a module structure, you can "unjar" it and open the module-info file. But you can also use the 'java' command to describe
    the module. The following two commands are equivalent:

    - `java -p mods -d zoo.animal.feeding`
    - `java -p mods -describe-module zoo.animal.feeding`

  - Each prints information about the module. For example, it might print this:
    `zoo.animal.feeding file:///C:/Dev/java-certification/src/ocp/chapter/eleven/mods/zoo.animal.feeding.jar exports zoo.animal.feeding requires java.base mandated `
  - The java.base module is special. It is automatically added as a dependency to all modules. This module has frequently used packages like java.util.
    Works like the java.lang package, that is automatically imported on classes whether you type it or not.
  - When printing information about a module that is structured like this:
    `module zoo.animal.care { exports zoo.animal.care.medical to zoo.staff; requires transitive zoo.animal.feeding; }`
    You'll get this result:
    `zoo.animal.care file:///C:/Dev/java-certification/src/ocp/chapter/eleven/mods/zoo.animal.care.jar requires zoo.animal.feeding transitive requires java.base mandated qualified exports zoo.animal.care.medical to zoo.staff contains zoo.animal.care.details`
  - The qualified exports is the full name of exporting to a specific module.
  - The contains means that there is a package in the module that is not exported at all. This is true, because our module has two packages and one is
    available only to code inside the module (details).

  2. Listing Available Modules

  - The simplest form lists all the modules that are part of the JDK and their version numbers:

    - `java --list-modules` // Result has a lot of lines and they look like this: java.base@11.0.2

  - You can print all the modules of the JDK plus some custom modules:

    - `java -p mods --list-modules` // Will print the same result as before plus our coded modules (zoo, talks...)

  - This command just prints the observable modules and exits, it does not run the program.

  3. Showing Module Resolution:

  - Using the --show-module-resolution option its kind of a way of debugging modules. It spits out a lot of output when the program starts up. Then it
    runs the program. Example:

    - `java --show-module-resolution -p feeding -m zoo.animal.feeding/zoo.animal.feeding.Task`

  - The output starts out by listing the root module you choose to run. Then it lists many lines of packages included by the mandatory java.base module.
    Then after a while, it lists modules that have dependencies. Finally, it outputs the result of the program (class Task).

* The jar Command:

  - Can describe a module too. Both of these commands are equivalent:

    - `jar -f mods/zoo.animal.feeding.jar -d`
    - `jar --file mods/zoo.animal.feeding.jar --describe-module`

  - The output is slightly different from when we used the java command. It prints a `/!module-info.class` on the ending of the first line.

* The jdeps Command:

  - Gives you info about dependencies within a module. Unlike describing a module, it looks at the code in addition to the module-file. this tells you
    what dependencies are actually used rather than simply declared.
  - Asking for a summary of the dependencies in zoo.animal.feeding. Both of these commands give the same output:

    - `jdeps -s mods/zoo.animal.feeding.jar`
    - `jdeps -summary mods/zoo.animal.feeding.jar`

  - Notice that the is one dash (-) before the -summary rather than two.
  - The output tells you that there is only one package and it depends on the built-in java.base module.

    `zoo.animal.feeding -> java.base`

  - Alternatively you can ask for the dependencies in zoo.animal.feeding without summary and get the long form of output:

    - `jdeps mods/zoo.animal.feeding.jar` // Outputs shows the module filename and path, lists the required java.base dependency and version number,
      // then finally prints the specific packages within the java.base modules that are used by feeding.

  - If you want to print modules that have dependencies on other custom modules, you need to specify the module path so jdeps knows where to find the info
    about the dependent module. We don't need to do this for modules built into the JDK like java.base. Syntax examples:

    - `jdeps -s --module-path mods mods/zoo.animal.care.jar`
    - `jdeps -summary --module-path mods mods/zoo.animal.care.jar`
    - `jdeps --module-path mods mods/zoo.animal.care.jar`

      - There is no short form of --module-path in the jdeps command.

  - Presenting the result of the last one in order of appearance, without -summary:
    1. Filename and required dependencies.
    2. Summary showing the two module dependencies with an arrow.
    3. Package-level dependencies.

* The jmod Command:
  - You might think a JMOD file is a Java module file, but not quite. Oracle recommends using JAR files for most modules. JMOD files are recommended only
    when you have native libraries or something that can't go inside a JAR file. This is unlikely to affect you in the real world.
  - The most important thing to remember is that jmod is only for working with the JMOD files. You don't need to memorize the syntax for jmod.
  - Some common modes using jmod:
    - create: Creates a JMOD file.
    - extract: Extracts all files from the JMOD. Works like unzipping.
    - describe: Prints the module details such as requires.
    - list: Lists all files in the JMOD file.
    - hash: Shows a long string that goes with the file.

## Reviewing Command-Line Options (p.483-485)

- The command lines you should expect to encounter on the exam (1Z0-815):

| Description             | Syntax                                                                                |
| :---------------------- | :------------------------------------------------------------------------------------ |
| Compile nonmodular code | javac -cp classpath -d directory classesToCompile                                     |
|                         | javac --class-path classpath -d directory classesToCompile                            |
|                         | javac -classpath classpath -d directory classesToCompile                              |
|                         |
| Run nonmodular code     | java -cp classpath package.className                                                  |
|                         | java -classpath classpath package.className                                           |
|                         | java --class-path classpath package.className                                         |
|                         |
| Compile a module        | javac -p moduleFolderName -d directory classesToCompileIncludingModuleInfo            |
|                         | javac --module-path moduleFolderName -d directory classesToCompileIncludingModuleInfo |
|                         |
| Run a module            | java -p moduleFolderName -m moduleName/package.className                              |
|                         | java --module-path moduleFolderName --module moduleName/package.className             |
|                         |
| Describe a module       | java -p moduleFolderName -d moduleName                                                |
|                         | java --module-path moduleFolderName --describe-module moduleName                      |
|                         | jar --file jarName --describe-module                                                  |
|                         | jar -f jarName -d                                                                     |
|                         |
| List available modules  | java --module-path moduleFolderName --list-modules                                    |
|                         | java -p moduleFolderName --list-modules                                               |
|                         | java --list-modules                                                                   |
|                         |
| View dependencies       | jdeps -summary --module-path moduleFolderName jarName                                 |
|                         | jdeps -s --module-path moduleFolderName jarName                                       |
|                         |
| Show module resolution  | java --show-module-resolution -p moduleFolderName -m moduleName                       |
|                         | java --show-module-resolution --module-path moduleFolderName --module moduleName      |

- Now some options that you need to know for the exam.

* 'javac' options:

| Option                   | Description                              |
| :----------------------- | :--------------------------------------- |
| -cp <classpath>          | Location of JARs in a nonmodular program |
| -classpath <classpath>   |                                          |
| --class-path <classpath> |                                          |
|                          |
| -d <dir>                 | Directory to place generated class files |
|                          |
| -p <path>                | Location of JARs in a modular program    |
| --module-path <path>     |                                          |
|                          |

- 'java' options:

| Option                   | Description                                        |
| :----------------------- | :------------------------------------------------- |
| -p <path>                | Location of JARs in a modular program              |
| --module-path <path>     |                                                    |
|                          |
| -m <name>                | Module name to run                                 |
| --module <name>          |                                                    |
|                          |
| -d                       | Describes the details of a module                  |
| --describe-module        |                                                    |
|                          |
| --list-modules           | Lists observable modules without running a program |
|                          |
| --show-module-resolution | Shows modules when running a program               |
|                          |

- 'jar' options:

| Option            | Description                                             |
| :---------------- | :------------------------------------------------------ |
| -c                | Create a new JAR file                                   |
| --create          |                                                         |
|                   |
| -v                | Prints details when working with JAR files              |
| --verbose         |                                                         |
|                   |
| -f                | JAR filename                                            |
| --file            |                                                         |
|                   |
| -C                | Directory containing files to be used to create the JAR |
|                   |
| -d                | Describes the details of a module                       |
| --describe-module |                                                         |
|                   |

- 'jdeps' options:

| Option               | Description                           |
| :------------------- | :------------------------------------ |
| --module-path <path> | Location of JARs in a modular program |
|                      |
| -s                   | Summarizes output                     |
| -summary             |                                       |
|                      |

## Summary (p.485-486)

- The JPMS organizes code at a higher level than packages. Each module contains one or more packages and a module-info file.
- Advantages of the JPMS include better access control, clearer dependency management, custom runtime images, improved performance
  and unique package enforcement.
- The module info file suports a number of keywords:
  - exports: Specifies that a package should be accessible outside the module. It can optionally restrict that export to a specific package (to Y).
  - requires: Is used when a module depends on code in another module. Additionally, 'requires transitive' can be used when all modules that require
    one module should always require another.
  - provides: Is used when exposing an API.
  - uses: Is used when consuming an API.
  - opens: Is used for allowing access via reflection.
- The 'java' command can also describe a module, list available modules or show the module resolution.
- The 'jar' command can also describe a module similar how the 'java' command does.
- The 'jdeps' command prints details about a module and packages.
- The 'jmod' command provides various modes for working with JMOD files rather than JAR files.
