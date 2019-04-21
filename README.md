# enso

A program to help manipulate abstract puzzle designs into physical puzzles.

## Getting Started

If you are wanting to use enso, visit <a href="http://jeremyrayner.com/puzzles/enso">jeremyrayner.com/puzzles/enso</a> website for the User Guide.

The rest of these instructions are for anyone who wants to use the source code.
These instructions will get you a copy of **enso** up and running on your local machine for development and testing purposes. 

### Prerequisites

To develop the software you will need Java:

<u>Java 8 or above</u>

If you don't already have Java installed, download and unpack latest stable Java version for Windows, Mac or Linux from [https://jdk.java.net/](https://jdk.java.net/)

Alternatively, you can install the latest long-term-support JDK on debian based systems with:
```
sudo apt-get install openjdk-11-jdk
```

### Installing

To get a development environment up and running

```
git clone https://github.com/jeremyrayner/enso.git
cd enso
```

Then to run tests and build the software

```
./gradlew clean build
```

To verify it has worked try running this, you should then see the help screen.
```
java -jar build/lib/enso-1.0.1-SNAPSHOT-all.jar
```

### Coding Style

This project uses **spotbugs** to provide static analysis of the code, as an extra layer of assurance during the build

## Deployment

The final distributables are available in the folder:
```
build/distributions
```

## Built With

* [Gradle](https://gradle.org/) - Provides the build tool and dependency management
* [Commons CLI](https://commons.apache.org/proper/commons-cli/) - Used to parse command line options

## Authors

* **Jeremy Rayner** - *Initial work* - [@j6wbs](https://twitter.com/j6wbs)

## License

This project is licensed under the GNU GPLv3 License - see the [GPLv3 License](https://choosealicense.com/licenses/gpl-3.0/) for details

## Acknowledgments

* Thanks to Andreas Röver for the amazing [Burr Tools](http://burrtools.sourceforge.net/)
* Thanks to Aaron Siegel for creating [puzzlecad](https://www.thingiverse.com/thing:3198014)
* Thanks also to Richard Gain and Derek Bosch for their help and inspiration in 3d printing.
* Thanks to Alfons Eyckmans, Brian Menold, Guy Brette for making my puzzle designs in wood.
* Everyone at the Camden Puzzle Party and Midlands Puzzle Party, for their warm welcome and fun times.
* Everyone at the **Puzzle Photography** and **Puzzle Friends** groups on Facebook.
* Thanks to Clair and Charlie for their indefatigable support and enthusiasm.