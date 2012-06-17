This module calls BIRCH install scripts.

It is designed to be callable at the command line, but also to be imported and called as a module.

It is also jython compatible, so it works with getbirch. All getbirch has to do is add this directory and the lib directory to the PYTHONPATH at runtime, then call the bootstrapper, specifying getbirch-main as the argument for main.
