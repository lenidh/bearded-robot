# bearded-robot

## Build with Gradle

### Requirements

- Simple Java Compiler (SJC): http://fam-frenz.de/stefan/compiler.html
- QEMU: http://wiki.qemu.org/Main_Page


### Configuration

1. Download SJC.
2. Place SJC files in a directory sjc in the project root.
3. Install QEMU.
4. Perhaps your have to add qemu-system-i386 to your PATH.


### Run

1. Open a command line.
2. cd to the project root.
3. Execute ``./gradlew build``
4. Execute ``./gradlew boot``
