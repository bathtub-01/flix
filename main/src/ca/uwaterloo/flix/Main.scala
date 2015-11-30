package ca.uwaterloo.flix

import java.nio.file.{Files, Paths, InvalidPathException}

import ca.uwaterloo.flix.util.{Verbosity, Debugger, Options, Validation}

/**
 * The main entry point for the Flix compiler and runtime.
 */
object Main {

  /**
   * The main method.
   */
  def main(argv: Array[String]): Unit = {

    val paths = argv.filter(p => p.endsWith(".flix") || p.endsWith(".flix.zip")).toList
    val args = argv.filter(a => a.startsWith("-"))

    // check that each path is valid.
    for (path <- paths) {
      if (!isValidPath(path)) {
        Console.println(s"Error: '$path' is not a valid path.")
        System.exit(1)
      }
    }

    // check that each argument is valid.
    var options = Options.Default
    for (arg <- args) {
      arg match {
        case "-d" => options = options.copy(debugger = Debugger.Enabled)
        case "--debugger" => options = options.copy(debugger = Debugger.Enabled)
        case "-v" => options = options.copy(verbosity = Verbosity.Verbose)
        case "--verbose" => options = options.copy(verbosity = Verbosity.Verbose)
        case "-s" => options = options.copy(verbosity = Verbosity.Silent)
        case "--silent" => options = options.copy(verbosity = Verbosity.Silent)
        case _ =>
          Console.println(s"Error: '$arg' is not a valid argument.")
          System.exit(1)
      }
    }

    // run flix
    Flix.mkPath(paths map (s => Paths.get(s)), options) match {
      case Validation.Success(model, errors) =>
        errors.foreach(e => println(e.format))
        if (options.verbosity != Verbosity.Silent) {
          model.print()
        }
      case Validation.Failure(errors) =>
        errors.foreach(e => println(e.format))
    }

  }

  /**
   * Returns `true` iff the given string `s` is a path to a readable file.
   */
  private def isValidPath(s: String): Boolean = try {
    val path = Paths.get(s)
    Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path)
  } catch {
    case e: InvalidPathException => false
  }

}
